package io.github.fastmq.domain.service;

import io.github.fastmq.domain.consumer.instantaneous.FastMQMessageListener;
import io.github.fastmq.infrastructure.constant.FastMQConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public interface MQService {
    Logger logger = LoggerFactory.getLogger(MQService.class);

    default String getRandConsumerName(Map<String, Long> consumerNames, FastMQMessageListener fastMQMessageListener) {
        //过滤掉原有的consumerName
        List<Map.Entry<String, Long>> entries = consumerNames.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(Objects.isNull(fastMQMessageListener) ? FastMQConstant.DEFAULT_CONSUMER
                        : fastMQMessageListener.consumeName()))
                .collect(Collectors.toList());
        //随机一个消费者进行消费：后期可以采用多策略去完成此工作
        ThreadLocalRandom current = ThreadLocalRandom.current();
        int i1 = current.nextInt(entries.size());
        return entries.get(i1).getKey();
    }

}
