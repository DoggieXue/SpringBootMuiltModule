package org.cloudxue.simplespringboot.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName KafkaConsumer
 * @Description: 基于spring-kafka
 * @Author: Doggie
 * @Date: 2023年09月20日 08:57:17
 * @Version 1.0
 **/
@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = {KafkaConstants.TOPIC})
    public void consumeMsg(ConsumerRecord<?,?> record) {
        log.info("简单消费：topic：{},partition：{}, value：{}",record.topic(), record.partition(), record.value());
    }
}
