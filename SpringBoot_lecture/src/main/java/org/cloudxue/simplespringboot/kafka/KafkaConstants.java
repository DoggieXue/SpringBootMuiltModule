package org.cloudxue.simplespringboot.kafka;

/**
 * @ClassName KafKaConstants
 * @Description: Kafka常量配置
 * @Author: Doggie
 * @Date: 2023年08月07日 15:02:11
 * @Version 1.0
 **/
public class KafkaConstants {
    public static final String BROKER_LIST = "localhost:9092";
    public static final String CLIENT_ID = "client1";
    public static final String GROUP_ID_CONFIG = "consumerGroup1";
    private KafkaConstants() {

    }
}
