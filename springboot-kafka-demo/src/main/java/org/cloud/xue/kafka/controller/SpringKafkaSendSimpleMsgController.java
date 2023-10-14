package org.cloud.xue.kafka.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.cloud.xue.kafka.constants.KafkaConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

/**
 * @Description: 使用spirng-kafka发送复杂消息对象
 *               key: org.apache.kafka.common.serialization.StringDeserializer
 *               value: org.springframework.kafka.support.serializer.JsonDeserializer
 * @Author: xuexiao
 * @Date: 2023年10月01日 11:35:09
 **/
@Slf4j
@RestController
@RequestMapping("/api/kafka")
public class SpringKafkaSendSimpleMsgController {

    private final KafkaTemplate<Integer, Object> kafkaTemplate;
    @Autowired
    public SpringKafkaSendSimpleMsgController(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 同步发送
     * 发送简单字符串消息，@PathVariable注解传参
     * @param normalMsg
     */
    @RequestMapping(value = "/sync", method = RequestMethod.POST)
    @ResponseBody
    public void sendMsg(@RequestParam("msg") String normalMsg) {
        ListenableFuture<SendResult<Integer, Object>> future = kafkaTemplate.send(KafkaConstant.TOPIC, normalMsg);
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

    /**
     * 设置回调函数，异步等待Kafka Broker端的返回
     * 可以监控消息是否发送成功，失败时做出补偿
     * @param msg
     */
    @RequestMapping(value = "/async", method = RequestMethod.POST)
    @ResponseBody
    public void sendMsgAsync(@RequestParam("msg") String msg) {
        //同步发送消息
        ListenableFuture<SendResult<Integer, Object>> sendFuture = kafkaTemplate.send(KafkaConstant.TOPIC, msg);
        //添加异步回调
        sendFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("上送Kafka消息失败：{}", ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<Integer, Object> result) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("上送Kafka消息成功!发送主题：{}, 发送分区：{},发送的偏移量：{}",
                        metadata.topic(), metadata.partition(),metadata.offset());
            }
        });
    }
}
