package org.cloud.xue.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.cloud.xue.kafka.constants.KafkaConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 当SpringBoot自动配置不能满足需要时，可以使用该类，自定义kafka的一些配置项
 * @Author: xuexiao
 * @Date: 2023年10月01日 12:59:02
 **/
@Configuration
@EnableKafka
public class KafkaConfig {
    @Value("{server.kafkaParam.bootstrap-servers}")
    private String bootstrapServers;

    @Value("{spring.consumer.group-id}")
    private String groupId;

    /**
     * 生产者配置
     * @return
     */
    private Map<String, Object> producerProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "-1");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 5);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 500);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.BROKER_LIST);
        return props;
    }

    /**
     * 生产者工厂
     * 不使用spring-kafka 的KafkaAutoConfiguration默认方式创建的DefaultKafkaProducerFactory，重新定义
     * @return
     */
    @Bean("producerFactory")
    public DefaultKafkaProducerFactory producerFactory() {
        return new DefaultKafkaProducerFactory(producerProperties());
    }

    /**
     * 生产者模板
     * 不使用spring-kafka 的KafkaAutoConfiguration默认方式创建的KafkaTemplate，重新定义
     * @param producerFactory
     * @return
     */
    @Bean
    public KafkaTemplate<Integer, Object> kafkaTemplate(DefaultKafkaProducerFactory producerFactory) {
        return new KafkaTemplate(producerFactory);
    }

    @Bean(name = "batchConsumeFactory")
    public KafkaListenerContainerFactory<?> batchConsumeFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerProperties()));
        factory.setBatchListener(true);//开启批量处理
        factory.setMissingTopicsFatal(false);
        factory.setConcurrency(3);
        return factory;
    }

    /**
     * 消费者配置
     * @return
     */
    private Map<String, Object> consumerProperties() {
        Map<String, Object> props = new HashMap<>(16);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstant.BROKER_LIST);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstant.GROUP_ID_CONFIG);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put("spring.json.trusted.packages", "org.cloud.xue.kafka.entity");
        return props;
    }
}