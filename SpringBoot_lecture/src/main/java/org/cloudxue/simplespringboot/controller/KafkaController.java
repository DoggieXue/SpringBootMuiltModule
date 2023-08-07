package org.cloudxue.simplespringboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.cloudxue.simplespringboot.kafka.ProducerCreator;
import org.cloudxue.simplespringboot.utils.TimeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * @ClassName KafkaController
 * @Description:
 * @Author: Doggie
 * @Date: 2023年08月07日 15:09:27
 * @Version 1.0
 **/
@Slf4j
@RequestMapping("/kafka")
@RestController
public class KafkaController {

    private static final String TOPIC = "quickstart-events";



    @RequestMapping("/sendMsg")
    public String sendMsg() throws ExecutionException, InterruptedException {
        Producer<String, String> producer = ProducerCreator.createProducer();
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, "Hello, kafka, Current Time is " + TimeUtils.getCurrentTime());
        RecordMetadata metadata = null;
        try {
            metadata = producer.send(record).get();
            log.info("Record sent to " + TOPIC + "topic, In partition: " + metadata.partition() + ", with offset " + metadata.offset());
        } catch (ExecutionException e) {
            log.error("Error in sending record");
            e.printStackTrace();
        }
        producer.close();
        return metadata.toString();
    }
}
