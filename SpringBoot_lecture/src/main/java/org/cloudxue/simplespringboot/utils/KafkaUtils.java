package org.cloudxue.simplespringboot.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.cloudxue.simplespringboot.kafka.ProducerCreator;

import java.util.concurrent.ExecutionException;

/**
 * @ClassName KafkaUtils
 * @Description:
 * @Author: Doggie
 * @Date: 2023年08月09日 13:58:25
 * @Version 1.0
 **/
@Slf4j
public class KafkaUtils {
    /**
     * 向kafka发送消息
     * @param topic
     * @param msg
     * @return
     */
    public static RecordMetadata sendMsg(String topic, String msg) {
        Producer<String, String> producer = ProducerCreator.createProducer();
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, msg);
        RecordMetadata metadata = null;
        try {
            metadata = producer.send(record).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return metadata;
    }

}
