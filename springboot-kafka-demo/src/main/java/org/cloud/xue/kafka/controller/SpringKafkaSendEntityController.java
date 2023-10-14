package org.cloud.xue.kafka.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.cloud.xue.kafka.constants.KafkaConstant;
import org.cloud.xue.kafka.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

/**
 * @Description: 使用spirng-kafka发送复杂消息对象
 *               key: org.apache.kafka.common.serialization.StringDeserializer
 *               value: org.springframework.kafka.support.serializer.JsonDeserializer
 * @Author: xuexiao
 * @Date: 2023年10月01日 12:46:47
 **/
@Slf4j
@RestController
@RequestMapping("/api/kafka")
public class SpringKafkaSendEntityController {

    private final KafkaTemplate<Integer, Object> kafkaTemplate;

    @Autowired
    public SpringKafkaSendEntityController(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/json")
    public Student sendJsonMsg(@RequestBody Student student) {

        ListenableFuture<SendResult<Integer, Object>> future = kafkaTemplate.send(KafkaConstant.TOPIC, student);
        try {
            SendResult<Integer, Object> sendResult = future.get();
            RecordMetadata metadata = sendResult.getRecordMetadata();
            log.info("同步上送Kafka消息成功！发送的主题：{}, 发送的分区：{}, 发送的偏移量：{}",
                    metadata.topic(),metadata.partition(),metadata.offset());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return student;
    }

    @PostMapping("/batch")
    public String sendBatch(@RequestParam String name) {

        Student student = new Student();
        for (int i = 0; i < 10; i++) {
            student.setName(name + "_" + i);
            student.setSex("女");
            student.setAge(3 + i);
            student.setEmail("damifu@gmail.com");
            student.setAddress("幸福谷123号");
            ListenableFuture<SendResult<Integer, Object>> future = kafkaTemplate.send(KafkaConstant.TOPIC, student);
            try {
                SendResult<Integer, Object> sendResult = future.get();
                RecordMetadata metadata = sendResult.getRecordMetadata();
                log.info("同步上送Kafka消息成功！发送的主题：{}, 发送的分区：{}, 发送的偏移量：{}",
                        metadata.topic(),metadata.partition(),metadata.offset());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        return "success";
    }

}
