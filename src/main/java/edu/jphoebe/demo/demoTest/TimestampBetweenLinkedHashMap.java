package edu.jphoebe.demo.demoTest;

import cn.auntec.framework.components.core.common.serializable.SerializableBean;
import cn.auntec.framework.components.util.constant.StringPoolConst;
import cn.auntec.framework.components.util.value.data.CollectionUtil;
import cn.auntec.framework.components.util.value.data.DateUtil;
import cn.auntec.framework.components.util.value.data.ObjectUtil;
import cn.auntec.framework.components.util.value.data.StrUtil;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 蒋时华
 * @date 2021-04-25 13:21:44
 */
public class TimestampBetweenLinkedHashMap<T> {
    private static final long serialVersionUID = -915782401028519482L;

    public final static long MAX_END_TIME = Long.MAX_VALUE;
    public final static long MIN_START_TIME = Long.MIN_VALUE;
    public final static long UNKNOWN_TIME = -1;

    private final static float FLOAT_TIME = 0.1F;

    private LinkedHashMap<TimeKey, List<T>> linkedHashMap;

    public TimestampBetweenLinkedHashMap() {
        this.linkedHashMap = new LinkedHashMap<>();
    }

    public TimestampBetweenLinkedHashMap(int initialCapacity, float loadFactor) {
        this.linkedHashMap = new LinkedHashMap<>(initialCapacity, loadFactor);
    }

    public TimestampBetweenLinkedHashMap(int initialCapacity) {
        this.linkedHashMap = new LinkedHashMap<>(initialCapacity);
    }

    public void sortByStartTime() {
        LinkedHashMap<TimeKey, List<T>> newResult = new LinkedHashMap<>();
        this.linkedHashMap.entrySet().stream()
                .sorted(Comparator.comparingLong(item -> item.getKey().getStartTime()))
                .forEach(item -> newResult.put(item.getKey(), item.getValue()));
        this.linkedHashMap = newResult;
    }

    /**
     * add
     *
     * @param startTime
     * @param endTime
     * @param value
     * @return
     */
    public List<T> put(Long startTime, Long endTime, T value) {
        TimeKey timeKey = TimeKey.builder()
                .startTime(startTime)
                .endTime(endTime)
                .build();
        List<T> ts = this.get(startTime, endTime);
        if (ts == null) {
            ts = new ArrayList<>();
        }
        ts.add(value);
        List<T> put = this.linkedHashMap.put(timeKey, ts);
        return put;
    }

    /**
     * add
     *
     * @param startTime
     * @param endTime
     * @param value
     * @return
     */
    public List<T> put(LocalDateTime startTime, LocalDateTime endTime, T value) {
        return this.put(DateUtil.getTimeMilli(startTime), DateUtil.getTimeMilli(endTime), value);
    }

    /**
     * remove
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public List<T> remove(Long startTime, Long endTime) {
        TimeKey timeKey = TimeKey.builder()
                .startTime(startTime)
                .endTime(endTime)
                .build();
        return this.linkedHashMap.remove(timeKey);
    }

    public boolean containsKey(Long startTime, Long endTime) {
        TimeKey timeKey = TimeKey.builder()
                .startTime(startTime)
                .endTime(endTime)
                .build();
        return this.linkedHashMap.containsKey(timeKey);
    }

    public List<T> get(Long startTime, Long endTime) {
        TimeKey timeKey = TimeKey.builder()
                .startTime(startTime)
                .endTime(endTime)
                .build();
        return this.linkedHashMap.get(timeKey);
    }

    public Map<TimeKey, List<T>> rangeInTime(long time) {
        this.sortByStartTime();
        LinkedHashMap<TimeKey, List<T>> result = new LinkedHashMap<>();
        for (Map.Entry<TimeKey, List<T>> entry : this.linkedHashMap.entrySet()) {
            TimeKey key = entry.getKey();
            if (!key.rangeInTime(time)) {
                continue;
            }
            result.put(key, entry.getValue());
        }
        return result;
    }

    public List<T> rangeInTimeValue(LocalDateTime time) {
        return this.rangeInTimeValue(DateUtil.getTimeMilli(time));
    }

    public List<T> rangeInTimeValue(BigDecimal time) {
        this.sortByStartTime();
        List<T> result = new ArrayList<>();
        for (Map.Entry<TimeKey, List<T>> entry : this.linkedHashMap.entrySet()) {
            TimeKey key = entry.getKey();
            if (!key.rangeInTime(time)) {
                continue;
            }
            result.addAll(entry.getValue());
        }
        return result;
    }

    public List<T> rangeInTimeValue(long time) {
        this.sortByStartTime();
        List<T> result = new ArrayList<>();
        for (Map.Entry<TimeKey, List<T>> entry : this.linkedHashMap.entrySet()) {
            TimeKey key = entry.getKey();
            if (!key.rangeInTime(time)) {
                continue;
            }
            result.addAll(entry.getValue());
        }
        return result;
    }

    private LinkedHashMap<Long, TimeSegmentationValue<T>> addTimeMap(LinkedHashMap<Long, TimeSegmentationValue<T>> allTimeMap
            , Long key
            , TimeKey timeKey
            , TimeKey originTimeKey
            , byte keyType) {
        if (allTimeMap == null) {
            allTimeMap = new LinkedHashMap<>();
        }
        TimeSegmentationValue<T> timeSegmentationValue = allTimeMap.get(key);
        if (timeSegmentationValue == null) {
            // 计算values, 并赋值
            allTimeMap.put(key, TimeSegmentationValue.<T>builder()
                    .timeType(keyType)
                    .timeKey(timeKey)
                    .originTimeKey(originTimeKey)
                    .build());
        } else if (timeSegmentationValue.getTimeType() == TimeSegmentationValue.TIME_TYPE_START
                && keyType == TimeSegmentationValue.TIME_TYPE_START) {
            // 当前插入的开始时间 与 已存在的开始时间冲突
            allTimeMap.remove(key);
            this.addTimeMap(allTimeMap, key, timeKey, timeSegmentationValue.getOriginTimeKey(), keyType);

            TimeKey repeatTimeKey = timeSegmentationValue.getTimeKey();
            Long newKey;
            TimeKey newTimeKey;
            // 将重复时间点拆分成两个
            if (timeKey.getEndTime() > repeatTimeKey.getEndTime()) {
                // 当前的授权时间更长
                /// repeat endTime value为当前授权的value
                newKey = repeatTimeKey.getEndTime();
                newTimeKey = TimeKey.builder()
                        .startTime(newKey)
                        .endTime(timeKey.getEndTime())
                        .build();
            } else {
                // 重复的授权时间更长
                /// repeat endTime value为重复授权的value
                newKey = timeKey.getEndTime();
                newTimeKey = TimeKey.builder()
                        .startTime(newKey)
                        .endTime(repeatTimeKey.getEndTime())
                        .build();
            }
            // 递归处理，当前分割后添加的时间点，依旧可能是重复的
            allTimeMap.remove(newKey);
            this.addTimeMap(allTimeMap, newKey, newTimeKey, timeSegmentationValue.getOriginTimeKey(), TimeSegmentationValue.TIME_TYPE_START);

            TimeSegmentationValue<T> temp = allTimeMap.get(newTimeKey.getEndTime());
            byte timeType = ObjectUtil.isNull(temp) ? keyType : temp.getTimeType();
            allTimeMap.remove(newTimeKey.getEndTime());
            this.addTimeMap(allTimeMap, newTimeKey.getEndTime(), newTimeKey, timeSegmentationValue.getOriginTimeKey(), timeType);
        } else if (timeSegmentationValue.getTimeType() == TimeSegmentationValue.TIME_TYPE_START
                && keyType == TimeSegmentationValue.TIME_TYPE_END) {
            // 这种情况不存在，因为linkedHashMap已经按照开始时间排序了
        } else if (timeSegmentationValue.getTimeType() == TimeSegmentationValue.TIME_TYPE_END
                && keyType == TimeSegmentationValue.TIME_TYPE_START) {
            // 当前插入的开始时间 与 已存在的截止时间冲突
            allTimeMap.remove(key);
            this.addTimeMap(allTimeMap, key, timeKey, timeSegmentationValue.getOriginTimeKey(), keyType);
        } else if (timeSegmentationValue.getTimeType() == TimeSegmentationValue.TIME_TYPE_END
                && keyType == TimeSegmentationValue.TIME_TYPE_END) {
            // 当前插入的截止时间 与 已存在的截止时间冲突
            allTimeMap.remove(key);
            timeKey.setStartTime(UNKNOWN_TIME);
            this.addTimeMap(allTimeMap, key, timeKey, timeSegmentationValue.getOriginTimeKey(), TimeSegmentationValue.TIME_TYPE_END);
        }
        return allTimeMap;
    }

    public Map<TimeKey, List<T>> toTimeSegmentationValues() {
        LinkedHashMap<TimeKey, List<T>> result = new LinkedHashMap<>();
        if (this.linkedHashMap.isEmpty()) {
            return result;
        }
        this.sortByStartTime();
        // 所有时间点
        // time (startTime、endTime) -> TimeSegmentationValue
        LinkedHashMap<Long, TimeSegmentationValue<T>> allTimeMap = new LinkedHashMap<>();
        List<Map.Entry<TimeKey, List<T>>> mapEntryList = new ArrayList<>(this.linkedHashMap.entrySet());
        int size = mapEntryList.size();
        for (int i = 0; i < size; i++) {
            Map.Entry<TimeKey, List<T>> entry = mapEntryList.get(i);
            TimeKey key = entry.getKey();
            List<T> value = entry.getValue();
            // 开始时间
            this.addTimeMap(allTimeMap, key.getStartTime(), key, key, TimeSegmentationValue.TIME_TYPE_START);
            // 查看下一个日期的开始时间是否小于当前的截止时间
            if (i == (size - 1)) {
                // 当前是最后一个时间节点
                // 截止时间
                this.addTimeMap(allTimeMap, key.getEndTime(), key, key, TimeSegmentationValue.TIME_TYPE_END);
                break;
            }
            Map.Entry<TimeKey, List<T>> nextEntry = mapEntryList.get(i + 1);
            TimeKey nextTimeKey = nextEntry.getKey();
            if (nextTimeKey.startTime > key.getEndTime()) {
                this.addTimeMap(allTimeMap, key.getEndTime(), key, key, TimeSegmentationValue.TIME_TYPE_END);
            } else {
                this.addTimeMap(allTimeMap, key.getEndTime(), key, key, TimeSegmentationValue.TIME_TYPE_START);
            }
        }

        // 按照key排序
        List<Map.Entry<Long, TimeSegmentationValue<T>>> collect = allTimeMap.entrySet().stream()
                .sorted(Comparator.comparingLong(entry -> entry.getKey()))
                .collect(Collectors.toList());
        for (int i = 0; i < collect.size() - 1; i++) {
            Map.Entry<Long, TimeSegmentationValue<T>> entry = collect.get(i);
            TimeSegmentationValue<T> timeSegmentationValue = entry.getValue();
            if (timeSegmentationValue.getTimeType() == TimeSegmentationValue.TIME_TYPE_END) {
                continue;
            }
            // 当前时间点是开始时间, 下一个时间节点就是截止时间
            Map.Entry<Long, TimeSegmentationValue<T>> nextEntry = collect.get(i + 1);
            // TODO: 2021/4/25 太多次遍历，性能不好，后续看看有什么更好的方法解决
            List<T> values = this.rangeInTimeValue(new BigDecimal(entry.getKey()).add(new BigDecimal(FLOAT_TIME)));
            if (CollectionUtil.isEmpty(values)) {
                continue;
            }
            result.put(TimeKey.builder()
                            .startTime(entry.getKey())
                            .endTime(nextEntry.getKey())
                            .build()
                    , values);
        }
        return result;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class TimeSegmentationValue<T> extends SerializableBean {
        private static final long serialVersionUID = 1663922274515365650L;

        /*** 开始时间 */
        public final static byte TIME_TYPE_START = 10;
        /*** 截止时间 */
        public final static byte TIME_TYPE_END = 20;

        private byte timeType;
        private TimeKey timeKey;
        private TimeKey originTimeKey;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class TimeKey extends SerializableBean {
        private static final long serialVersionUID = 8385202312668963403L;

        private Long startTime;
        private Long endTime;

        private void setDefault() {
            if (startTime == null) {
                startTime = MIN_START_TIME;
            }
            if (endTime == null) {
                endTime = MAX_END_TIME;
            }
        }

        public boolean rangeInTime(long time) {
            this.setDefault();
            if (startTime <= time && time <= endTime) {
                return true;
            }
            return false;
        }

        public boolean rangeInTime(BigDecimal time) {
            this.setDefault();
            if (time.compareTo(new BigDecimal(startTime)) > -1 && time.compareTo(new BigDecimal(endTime)) < 1) {
                return true;
            }
            return false;
        }

        public String getKey() {
            this.setDefault();
            return startTime + StringPoolConst.COMMA + endTime;
        }

        @Override
        public int hashCode() {
            this.setDefault();
            int result = 17;
            result = 31 * result + startTime.hashCode();
            result = 31 * result + endTime.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof TimeKey)) {
                return false;
            }
            return StrUtil.equals(this.getKey(), ((TimeKey) obj).getKey());
        }
    }


    public static void main(String[] args) {

        // TODO: 2021/4/25 测试代码
        TimestampBetweenLinkedHashMap timestampBetweenLinkedHashMap = new TimestampBetweenLinkedHashMap();
        timestampBetweenLinkedHashMap.put(DateUtil.parseLocalDateTime("2021-05-03 16:11:01"), DateUtil.parseLocalDateTime("2021-05-04 12:11:01"), 10);
        timestampBetweenLinkedHashMap.put(DateUtil.parseLocalDateTime("2021-05-02 20:11:01"), DateUtil.parseLocalDateTime("2021-05-03 16:11:01"), 10);
        timestampBetweenLinkedHashMap.put(DateUtil.parseLocalDateTime("2021-05-02 00:11:01"), DateUtil.parseLocalDateTime("2021-05-02 20:11:01"), 10);
        timestampBetweenLinkedHashMap.put(DateUtil.parseLocalDateTime("2021-05-01 04:11:01"), DateUtil.parseLocalDateTime("2021-05-02 00:11:01"), 10);
        timestampBetweenLinkedHashMap.put(DateUtil.parseLocalDateTime("2021-04-30 08:11:01"), DateUtil.parseLocalDateTime("2021-05-01 04:11:01"), 10);
        timestampBetweenLinkedHashMap.put(DateUtil.parseLocalDateTime("2021-04-29 12:11:01"), DateUtil.parseLocalDateTime("2021-04-30 08:11:01"), 10);
        timestampBetweenLinkedHashMap.put(DateUtil.parseLocalDateTime("2021-04-28 16:11:01"), DateUtil.parseLocalDateTime("2021-04-29 12:11:01"), 10);
        timestampBetweenLinkedHashMap.put(DateUtil.parseLocalDateTime("2021-04-27 20:11:01"), DateUtil.parseLocalDateTime("2021-04-28 16:11:01"), 10);
        timestampBetweenLinkedHashMap.put(DateUtil.parseLocalDateTime("2021-04-27 00:11:01"), DateUtil.parseLocalDateTime("2021-04-27 20:11:01"), 10);

        List list = timestampBetweenLinkedHashMap.rangeInTimeValue(LocalDateTime.now().plusDays(1));
        Map map1 = timestampBetweenLinkedHashMap.toTimeSegmentationValues();

        System.out.println("bbbb");
    }


}
