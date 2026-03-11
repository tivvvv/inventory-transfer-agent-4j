package com.tiv.inventory.transfer.graph.saver;

import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.checkpoint.Checkpoint;
import com.alibaba.cloud.ai.graph.checkpoint.savers.RedisSaver;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 支持TTL的RedisSaver实现
 */
@Slf4j
public class TtlRedisSaver extends RedisSaver {

    private final RedissonClient redisson;

    private final Long ttl;

    private final TimeUnit timeUnit;

    private static final String PREFIX = "graph:checkpoint:content:";

    public TtlRedisSaver(RedissonClient redisson, Long ttl, TimeUnit timeUnit) {
        super(redisson);
        this.redisson = redisson;
        this.ttl = ttl;
        this.timeUnit = timeUnit;
    }

    @Override
    public RunnableConfig put(RunnableConfig config, Checkpoint checkpoint) throws Exception {
        // 调用父类方法保存checkpoint
        RunnableConfig result = super.put(config, checkpoint);

        // 设置TTL
        if (config.threadId().isPresent()) {
            String threadId = config.threadId().get();
            setExpire(PREFIX + threadId);
        }

        return result;
    }

    /**
     * 为指定key设置过期时间
     */
    private void setExpire(String key) {
        try {
            RBucket<String> bucket = redisson.getBucket(key);
            if (bucket.isExists()) {
                Duration duration = Duration.ofMillis(timeUnit.toMillis(ttl));
                bucket.expire(duration);
            }
            log.info("TtlRedisSaver--setExpire--为key: {}, 设置TTL: {} {}", key, ttl, timeUnit);
        } catch (Exception e) {
            log.error("TtlRedisSaver--setExpire--设置TTL失败, key: {}", key, e);
        }
    }

}