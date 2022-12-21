package com.fastmq.infrastructure.prop;

import com.fastmq.infrastructure.constant.FastMQConstant;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Data
@ConfigurationProperties(prefix = FastMQConstant.PREFIX)
public class FastMQProperties {
    /**
     * 每次拉取数据的量--全局设置，消费者也可通过注解定制化
     */
    private Integer fetchMessageSize = 5;
    /**
     * 检查consumer不活跃的门槛（单位秒）
     */
    private Idle idle = new Idle();
    /**
     * 每次拉取PendingList的大小
     */
    private Integer checkPendingListSize = 100;
    /**
     * 死信门槛
     */
    private Integer deadLetterThreshold = 32;
    /**
     * 认领门槛
     */
    private Claim claim = new Claim();
    /**
     * 是否从头开始订阅消息
     */
    private Boolean isStartFromHead = true;

    /**
     * 整理stream的最大上限
     */
    private Integer trimThreshold = 10000;
    /**
     * 线程池配置
     */
    private Executor executor = new Executor();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Claim{
        /**
         * 认领门槛
         */
        private Integer claimThreshold = 3600;
        /**
         * 时间单元
         */
        private TimeUnit timeUnit = TimeUnit.SECONDS;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Idle{
        /**
         * 检查consumer不活跃的门槛（单位秒）
         */
        private Integer pendingListIdleThreshold = 10;
        /**
         * 时间单元
         */
        private TimeUnit timeUnit = TimeUnit.SECONDS;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Executor{
        /**
         * 核心线程数
         */
        private Integer executorCoreSize = Runtime.getRuntime().availableProcessors() * 2;
        /**
         * 拉取信息的周期
         */
        private Integer pullHealthyMessagesPeriod = 1;

        /**
         * 检查PendingList周期
         */
        private Integer checkPendingListsPeriod = 10;

        /**
         * 第一次延迟执行的时间
         */
        private Integer initialDelay = 1;

        /**
         * 时间单元
         */
        private TimeUnit timeUnit = TimeUnit.SECONDS;
    }
}
