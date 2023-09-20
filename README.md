# SpringBootMuiltModule
基于Maven搭建SpringBoot多模块项目

## Spring Boot 集成Kafka
### 依赖版本确认
当使用3.5.1的Kafka时，kafka-client最好也使用3.5.1版本，根据官网要求，SpringBoot应该选择2.7.x，spring-kafka选择2.9.x

### 集成Demo
- 引入spring-kafka依赖
- application.yml配置kafka相关参数，包括：kafka地址、生产者信息、消费者信息
- 生产者同步发送信息： 注入KafkaTemplate即可
- 生产者异步发送消息： 在方法上添加KafkaListener(topics = {"topicName"})


