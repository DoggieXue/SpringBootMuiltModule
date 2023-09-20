package org.cloudxue.simplespringboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.cloudxue.simplespringboot.kafka.KafkaConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * @ClassName SpringKafkaController
 * @Description: 基于spring-kafka包
 * @Author: Doggie
 * @Date: 2023年09月20日 08:45:23
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/api/kafka")
public class SpringKafkaController {

    @Autowired
    private KafkaTemplate<Integer, String> kafkaTemplate;

    /**
     * 同步发送
     * 发送简单字符串消息，@PathVariable注解传参
     * @param normalMsg
     */
    @GetMapping("/sync/{msg}")
    public void sendMsg(@PathVariable("msg") String normalMsg) {
        ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate.send(KafkaConstants.TOPIC, normalMsg);
        try {
            SendResult<Integer, String> sendResult = future.get();

            RecordMetadata metadata = sendResult.getRecordMetadata();
            log.info("同步上送Kafka消息成功！发送的主题：{}, 发送的分区：{}, 发送的偏移量：{}", metadata.topic(),metadata.partition(),metadata.offset());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置回调函数，异步等待Kafka Broker端的返回
     * 可以监控消息是否发送成功，失败时做出补偿
     * @param msg
     */
    @GetMapping("/async/{msg}")
    public void sendMsgAsync(@PathVariable String msg) {
        //同步发送消息
        ListenableFuture<SendResult<Integer, String>> sendFuture = kafkaTemplate.send(KafkaConstants.TOPIC, msg);
        //添加异步回调
        sendFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("上送Kafka消息失败：{}", ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("上送Kafka消息成功!发送主题：{}, 发送分区：{},发送的偏移量：{}", metadata.topic(), metadata.partition(),metadata.offset());

            }
        });
    }
}
