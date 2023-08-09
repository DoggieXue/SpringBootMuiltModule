package org.cloudxue.simplespringboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.cloudxue.simplespringboot.entity.Result;
import org.cloudxue.simplespringboot.entity.Student;
import org.cloudxue.simplespringboot.kafka.ProducerCreator;
import org.cloudxue.simplespringboot.utils.KafkaUtils;
import org.cloudxue.simplespringboot.utils.TimeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

/**
 * @ClassName KafkaController
 * @Description: Kafka生产者测试
 * @Author: Doggie
 * @Date: 2023年08月07日 15:09:27
 * @Version 1.0
 **/
@Slf4j
@RequestMapping("/kafka")
@RestController
public class KafkaController {

    private static final String TOPIC = "quickstart-events";

    @RequestMapping(value = "/sendMsg", produces = "application/json; charset=UTF-8")
    public String sendMsg() throws ExecutionException, InterruptedException {
        Producer<String, String> producer = ProducerCreator.createProducer();
        String sendInfo = "Hello, kafka, Current Time is " + TimeUtils.getCurrentTime();
        log.info("发送给Kafka的消息内容是： " + sendInfo);
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, sendInfo);
        RecordMetadata metadata = null;
        try {
            metadata = producer.send(record).get();
            log.info("Record sent to " + TOPIC + "，topic, In partition: " + metadata.partition() + ", with offset " + metadata.offset());
        } catch (ExecutionException e) {
            log.error("Error in sending record");
            e.printStackTrace();
        }
        producer.close();
        return sendInfo;
    }

    @RequestMapping(value = "/send/{input}")
    public String sendMessage(@PathVariable("input") String input) {
        RecordMetadata metadata = KafkaUtils.sendMsg(TOPIC, input);
        log.info("Record sent to " + TOPIC + "，topic, In partition: " + metadata.partition() + ", with offset " + metadata.offset());
        return "Hello, " + input;
    }

    @RequestMapping(value = "/produce", method = RequestMethod.POST)
    public Result producerMsg(@RequestBody Student student) {
        RecordMetadata metadata = KafkaUtils.sendMsg(TOPIC, student.toString());
        log.info("Record sent to " + TOPIC + "，topic, In partition: " + metadata.partition() + ", with offset " + metadata.offset());
        return Result.success(student);
    }

    /**
     * 通过统一包装处理类 ResponseAdvice 进行响应结果的处理
     * @param student
     * @return
     */
    @RequestMapping(value = "/sendEntity", method = RequestMethod.POST)
    public Student sendEntity(@RequestBody Student student) {
        RecordMetadata metadata = KafkaUtils.sendMsg(TOPIC, student.toString());
        log.info("Record sent to " + TOPIC + "，topic, In partition: " + metadata.partition() + ", with offset " + metadata.offset());
        return student;
    }
}
