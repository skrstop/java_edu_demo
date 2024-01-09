package edu.jphoebe.demo.flink;

import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.data.NumberUtil;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.LocalEnvironment;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisConfigBase;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;
import org.apache.flink.streaming.runtime.operators.util.AssignerWithPeriodicWatermarksAdapter;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author 蒋时华
 * @date 2022-06-30 17:31:08
 */
public class DemoWordCount {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoWordCount.class);
    private static final int FLINK_PARALLELISM = 1;

    public static void main(String[] args) throws Exception {
        demo1();
    }

    public static void demo2() throws Exception {
        LocalEnvironment env = LocalEnvironment
                .createLocalEnvironment();
        DataSet<String> inputLineDataSet = env.fromCollection(CollectionUtil.newArrayList(
                "hello world",
                "hello world",
                "hello0 world",
                "hello1 world",
                "hello1 world",
                "hello2 world",
                "hello2 world",
                "hello2 world",
                "hello3 world",
                "hello4 world"
        ));
        // 对数据集进行多个算子处理，按空白符号分词展开，并转换成(word, 1)二元组进行统计
        DataSet<Tuple2<String, Integer>> resultSet = inputLineDataSet
                .flatMap(
                        new FlatMapFunction<String, Tuple2<String, Integer>>() {
                            public void flatMap(String line, Collector<Tuple2<String, Integer>> out)
                                    throws Exception {
                                // 按空白符号分词
                                String[] wordArray = line.split("\\s");
                                // 遍历所有word，包成二元组输出
                                for (String word : wordArray) {
                                    out.collect(Tuple2.of(word, 1));
                                }
                            }
                        })
                // 返回的是一个一个的(word,1)的二元组，按照第一个位置的word分组
                .groupBy(0)
                // 将第二个位置上的freq=1的数据求和
                .sum(1);
        // 打印出来计算出来的(word,freq)的统计结果对

        resultSet.print();
    }

    public static void demo1() throws Exception {
        // 创建 execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setRestartStrategy(RestartStrategies.noRestart());//声明使用eventTime
        env.enableCheckpointing(TimeUnit.SECONDS.toMillis(10));

        // 通过连接 socket 获取输入数据，这里连接到本地9000端口，如果9000端口已被占用，请换一个端口
        // nc -lk 9000
//        DataStream<String> text = env.socketTextStream("localhost", 9000, "\n");
        DataStreamSource<String> text = env.fromCollection(CollectionUtil.newArrayList(
                "1 1",
                "1 5",
                "1 10",
                "1 11",
                "1 17",
                "1 20",
                "1 45"
        ));

        // 解析数据，按 word 分组，开窗，聚合
        DataStream<Tuple4<String, Integer, Long, Long>> dataStream = text
                // 获取数据中的event时间戳进行判断
                .assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarksAdapter.Strategy<>(new BoundedOutOfOrdernessTimestampExtractor<String>(Time.seconds(0)) {
                    @Override
                    public long extractTimestamp(String element) {
                        return NumberUtil.toInt(element.split("\\s")[1]) * 1000;
                    }
                }))
//                .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
//                    @Override
//                    public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
//                        String[] split = value.split("\\s");
//                        out.collect(Tuple2.of(split[0], 1));
//                    }
//                })
                .map(new MapFunction<String, Tuple2<String, Integer>>() {

                    @Override
                    public Tuple2<String, Integer> map(String value) throws Exception {
                        String[] split = value.split("\\s");
                        return Tuple2.of(split[0], 1);
                    }
                })
//                .keyBy(item -> item.f0)
                .keyBy(new KeySelector<Tuple2<String, Integer>, String>() {
                    @Override
                    public String getKey(Tuple2<String, Integer> value) throws Exception {
                        return value.f0;
                    }
                })
                // 滚动窗口：窗口之间相互隔离，无重叠，2参数表示时间向后延迟
//                .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
//                .window(TumblingProcessingTimeWindows.of(Time.seconds(5), Time.seconds(1)))
                // 滑动窗口：1定义窗口大小，2定义窗口之间的时间间隔
//                .window(SlidingProcessingTimeWindows.of(Time.seconds(5), Time.seconds(5)))
                // 会话窗口：参数表示session停止时间
                .window(EventTimeSessionWindows.withGap(Time.seconds(5)))
//                .sum(1)
                .process(new ProcessWindowFunction<Tuple2<String, Integer>, Tuple4<String, Integer, Long, Long>, String, TimeWindow>() {
                    @Override
                    public void process(String s, ProcessWindowFunction<Tuple2<String, Integer>, Tuple4<String, Integer, Long, Long>, String, TimeWindow>.Context context, Iterable<Tuple2<String, Integer>> elements, Collector<Tuple4<String, Integer, Long, Long>> out) throws Exception {
                        Tuple4<String, Integer, Long, Long> tp4 = new Tuple4<>();
                        int count = 0;
                        for (Tuple2<String, Integer> item : elements) {
                            count++;
                            tp4.f0 = item.f0;
                            tp4.f1 = count;
                        }
                        tp4.f2 = context.window().getStart();
                        tp4.f3 = context.window().getEnd();
                        out.collect(tp4);
                    }
                });
        // 将结果打印到控制台，注意这里使用的是单线程打印，而非多线程
//        dataStream.print().setParallelism(1);


        // sink redis
        FlinkJedisConfigBase conf = new FlinkJedisPoolConfig.Builder()
                .setHost("10.10.20.105")
                .setDatabase(15)
                .build();
        // bahir
        dataStream
                .addSink(new RedisSink<Tuple4<String, Integer, Long, Long>>(conf, new RedisMapper<Tuple4<String, Integer, Long, Long>>() {
                    @Override
                    public RedisCommandDescription getCommandDescription() {
//                        return new RedisCommandDescription(RedisCommand.HSET, "HASH_NAME");
                        return new RedisCommandDescription(RedisCommand.SET, "HASH_NAME");
                    }

                    @Override
                    public String getKeyFromData(Tuple4<String, Integer, Long, Long> value) {
                        return value.f0;
                    }

                    @Override
                    public String getValueFromData(Tuple4<String, Integer, Long, Long> value) {
                        return value.toString();
                    }
                })).setParallelism(4);

        // sink filesystem
//        String path = "/Users/jphoebe/Downloads/flink/";
//        dataStream
////                .rescale()
//                .addSink(StreamingFileSink.forRowFormat(new Path(path), new SimpleStringEncoder<Tuple4<String, Integer, Long, Long>>("UTF-8"))
//                        /**
//                         * 设置桶分配政策
//                         * DateTimeBucketAssigner --默认的桶分配政策，默认基于时间的分配器，每小时产生一个桶，格式如下yyyy-MM-dd--HH
//                         * BasePathBucketAssigner ：将所有部分文件（part file）存储在基本路径中的分配器（单个全局桶）
//                         */
//                        .withBucketAssigner(new DateTimeBucketAssigner<>())
//                        /**
//                         * 有三种滚动政策
//                         *  CheckpointRollingPolicy
//                         *  DefaultRollingPolicy
//                         *  OnCheckpointRollingPolicy
//                         */
//                        .withRollingPolicy(
//                                /**
//                                 * 滚动策略决定了写出文件的状态变化过程
//                                 * 1. In-progress ：当前文件正在写入中
//                                 * 2. Pending ：当处于 In-progress 状态的文件关闭（closed）了，就变为 Pending 状态
//                                 * 3. Finished ：在成功的 Checkpoint 后，Pending 状态将变为 Finished 状态
//                                 *
//                                 * 观察到的现象
//                                 * 1.会根据本地时间和时区，先创建桶目录
//                                 * 2.文件名称规则：part-<subtaskIndex>-<partFileIndex>
//                                 * 3.在macos中默认不显示隐藏文件，需要显示隐藏文件才能看到处于In-progress和Pending状态的文件，因为文件是按照.开头命名的
//                                 *
//                                 */
//                                DefaultRollingPolicy.builder()
//                                        //每隔多久（指定）时间生成一个新文件
//                                        .withRolloverInterval(TimeUnit.SECONDS.toMillis(1))
//                                        //数据不活动时间 每隔多久（指定）未来活动数据，则将上一段时间（无数据时间段）也生成一个文件
//                                        .withInactivityInterval(TimeUnit.SECONDS.toMillis(1))
//                                        //每个文件大小
//                                        .withMaxPartSize(1024 * 1024 * 1024)
//                                        .build())
//                        // 桶检查间隔，这里设置为1s
//                        .withBucketCheckInterval(10L)
//                        /**
//                         * 设置sink的前缀和后缀
//                         * 文件的头和文件扩展名
//                         */
//                        .withOutputFileConfig(OutputFileConfig
//                                .builder()
//                                .withPartPrefix("wordCount")
//                                .withPartSuffix(".txt")
//                                .build())
//                        .build())
//                .setParallelism(1);


        env.execute();
    }


}
