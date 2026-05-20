package com.nursery.common;

public class SnowflakeIdGenerator {

    /**
     * 起始时间戳（2020-01-01 00:00:00）
     */
    private static final long EPOCH = 1577836800000L;

    /**
     * 机器ID所占位数
     */
    private static final long WORKER_ID_BITS = 5L;

    /**
     * 数据中心ID所占位数
     */
    private static final long DATA_CENTER_ID_BITS = 5L;

    /**
     * 序列号所占位数
     */
    private static final long SEQUENCE_BITS = 12L;

    /**
     * 最大机器ID (0-31)
     */
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    /**
     * 最大数据中心ID (0-31)
     */
    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);

    /**
     * 序列号掩码
     */
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    /**
     * 机器ID左移位数
     */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    /**
     * 数据中心ID左移位数
     */
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * 时间戳左移位数
     */
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    /**
     * 数据中心ID
     */
    private final long dataCenterId;

    /**
     * 机器ID
     */
    private final long workerId;

    /**
     * 序列号
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间戳
     */
    private long lastTimestamp = -1L;

    /**
     * 构造函数
     * @param workerId 工作ID (0-31)
     * @param dataCenterId 数据中心ID (0-31)
     */
    public SnowflakeIdGenerator(long workerId, long dataCenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("Worker ID can't be greater than " + MAX_WORKER_ID + " or less than 0");
        }
        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException("Data center ID can't be greater than " + MAX_DATA_CENTER_ID + " or less than 0");
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    /**
     * 生成下一个ID
     * @return 19位数字ID
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过，抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
                            lastTimestamp - timestamp));
        }

        // 如果是同一时间生成的，则进行序列号递增
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            // 序列号溢出
            if (sequence == 0) {
                // 阻塞到下一毫秒，获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 时间戳改变，序列号重置
            sequence = 0L;
        }

        // 更新上次生成ID的时间戳
        lastTimestamp = timestamp;

        // 生成ID
        return ((timestamp - EPOCH) << TIMESTAMP_LEFT_SHIFT)
                | (dataCenterId << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 阻塞到下一毫秒，直到获得新的时间戳
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }
}

