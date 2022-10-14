package edu.jphoebe.demo.kafka.partitioner;

import cn.hutool.core.util.RandomUtil;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author 蒋时华
 * @date 2022-09-23 18:53:45
 */
public class CustomPartitioner1 implements Partitioner {


    private Random random;

    public void configure(Map<String, ?> configs) {
        //做必要的初始化工作
        random = new Random();
    }

    //分区策略
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        String keyObj = (String) key;
        List<PartitionInfo> partitionInfoList = cluster.availablePartitionsForTopic(topic);
        int partitionCount = partitionInfoList.size();
        int index = RandomUtil.randomInt(0, partitionCount);
        System.out.println("分区：" + index);
        return index;
    }

    public void close() {
        //清理工作
    }
}
