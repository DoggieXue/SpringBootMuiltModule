package org.cloud.xue.simplespringboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.cloud.xue.simplespringboot.entity.Student;
import org.cloud.xue.simplespringboot.kafka.KafkaConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Controller;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.ExecutionException;

/**
 * @Description:
 * @Author: xuexiao
 * @Date: 2023年09月21日 22:18:25
 **/
@Slf4j
@RequestMapping("/api/kafka")
@Controller
public class SpringKafkaSendJsonController {
    private KafkaTemplate<String, Student> kafkaTemplate;
    @Autowired
    public SpringKafkaSendJsonController(KafkaTemplate<String, Student> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/json")
    public Student sendJsonMsg(@RequestBody Student student) {
        ListenableFuture<SendResult<String, Student>> future = kafkaTemplate.send(KafkaConstants.TOPIC, student);
        try {
            SendResult<String, Student> sendResult = future.get();
            RecordMetadata metadata = sendResult.getRecordMetadata();
            log.info("同步上送Kafka消息成功！发送的主题：{}, 发送的分区：{}, 发送的偏移量：{}", metadata.topic(),metadata.partition(),metadata.offset());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return student;
    }
}
