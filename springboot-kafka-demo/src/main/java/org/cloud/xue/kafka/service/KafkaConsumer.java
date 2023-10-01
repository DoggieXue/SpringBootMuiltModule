package org.cloud.xue.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.cloud.xue.kafka.constants.KafkaConstant;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: Kafka Consumer服务
 * @Author: xuexiao
 * @Date: 2023年10月01日 13:45:02
 **/
@Slf4j
@Service
public class KafkaConsumer {
//    @KafkaListener(topics = KafkaConstant.TOPIC)
    public void consumer(Object msg) {
        log.info("当前线程：{},消费Kafka消息： {}",Thread.currentThread().getId(), msg.toString());
    }


    /**
     * 批量消费
     * @param records
     */
    @KafkaListener(topics = KafkaConstant.TOPIC, groupId = KafkaConstant.GROUP_ID_CONFIG, containerFactory = "batchConsumeFactory")
    public void consumerBatch(List<ConsumerRecord<Integer, Object>> records) {
        log.info("当前线程：{},消费Kafka消息： {}",Thread.currentThread().getId(), records.toString());
    }

    @KafkaListener(topics = KafkaConstant.TOPIC, containerFactory = "batchConsumeFactory")
    public void concurrentConsume(List<ConsumerRecord<Integer, Object>> records) {
        log.info("当前线程ID：{},消费Kafka消息： {}",Thread.currentThread().getId(), records.toString());
    }
}
