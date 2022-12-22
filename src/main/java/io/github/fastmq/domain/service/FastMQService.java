package io.github.fastmq.domain.service;

import io.github.fastmq.domain.consumer.instantaneous.FastMQListener;
import io.github.fastmq.domain.consumer.instantaneous.FastMQMessageListener;
import org.redisson.api.RStream;
import org.redisson.api.StreamMessageId;

import java.util.Map;
import java.util.Set;

public interface FastMQService extends MQService{
    /**
     * 异步消费空闲超时信息进行重传
     *
     * @param idleIds        超时列表
     * @param fastMQListener the fast mq listener
     */
    void consumeIdleMessages(Set<StreamMessageId> idleIds, FastMQListener fastMQListener);

    /**
     * 检查消费一直消费失败的信息（达到最大重试次数后会加入死信队列、通知管理员）
     *
     * @param deadLetterIds  死信ID列表
     * @param fastMQListener the fast mq listener
     */
    void consumeDeadLetterMessages(Set<StreamMessageId> deadLetterIds, FastMQListener fastMQListener);

    /**
     * 认领空闲过久的消息
     *
     * @param fastMQListener the fast mq listener
     */
    void claimIdleConsumer(FastMQListener fastMQListener);

    /**
     * 消费消息
     *
     * @param res
     * @param data
     * @param stream
     * @param fastMQMessageListener
     */
    void consumeMessages(Map<StreamMessageId, Map<Object, Object>> res, FastMQListener data, RStream<Object, Object> stream, FastMQMessageListener fastMQMessageListener);

    /**
     * 处理异常消息方法
     *
     * @param fastMQListener
     */
    void checkPendingList(FastMQListener fastMQListener);


    /**
     * 消费者消费
     * @param fastMQListener
     */
    void consumeFastMQListeners(FastMQListener fastMQListener);
}
