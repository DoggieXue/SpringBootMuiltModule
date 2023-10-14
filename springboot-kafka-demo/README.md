# get方式传参报错
java.lang.IllegalArgumentException: Invalid character found in the request target [/kafkaDemo/api/kafka/sync/0xe50x930x880xe50x930x880xe50x930x88 ]. The valid characters are defined in RFC 7230 and RFC 3986
	at org.apache.coyote.http11.Http11InputBuffer.parseRequestLine(Http11InputBuffer.java:502) ~[tomcat-embed-core-9.0.68.jar:9.0.68]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:271) ~[tomcat-embed-core-9.0.68.jar:9.0.68]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65) [tomcat-embed-core-9.0.68.jar:9.0.68]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:893) [tomcat-embed-core-9.0.68.jar:9.0.68]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1789) [tomcat-embed-core-9.0.68.jar:9.0.68]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49) [tomcat-embed-core-9.0.68.jar:9.0.68]
	at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191) [tomcat-embed-core-9.0.68.jar:9.0.68]
	at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659) [tomcat-embed-core-9.0.68.jar:9.0.68]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) [tomcat-embed-core-9.0.68.jar:9.0.68]
	at java.lang.Thread.run(Thread.java:748) [na:1.8.0_301]
	
`curl http://localhost:8090/kafkaDemo/api/kafka/sync/哈哈哈`

原因：
这是因为Tomcat在 7.0.73, 8.0.39, 8.5.7 版本后，添加了对于http头的验证。

具体来说，就是添加了些规则去限制HTTP头的规范性。

org.apache.tomcat.util.http.parser.HttpParser#IS_NOT_REQUEST_TARGET[]中定义了一堆not request target

if(IS_CONTROL[i] || i > 127 || i == 32 || i == 34 || i == 35 || i == 60 || i == 62 || i == 92 || i == 94 || i == 96 || i == 123 || i == 124 || i == 125) {
                IS_NOT_REQUEST_TARGET[i] = true;
            }
转换过来就是以下字符(对应10进制ASCII):

键盘上的控制键:(<32或者=127)

非英文字符(>127)

空格(32)

双引号(34)

#(35)

<(60)

>(62)

\(92)

^(94)

`(96)

{(123)

}(124)

|(125)

解决方法1：

配置tomcat的catalina.properties

添加或者修改：

tomcat.util.http.parser.HttpParser.requestTargetAllow=\|{}

上述配置允许URL中包含\|{}字符，如果包含中文就不能使用这种配置。

解决方法2：

更换tomcat版本，使用较低的版本。

解决方法3：

对请求链接进行编码转义，可以使用javascript中的encodeURI和decodeURI函数。Chrome、Firefox等浏览器会对URL自动进行转义，而IE却不行。这也是只有在用IE访问的情况下会报错的原因。

解决方法4：

选择另外的参数传递方法，比如post方式。

# SpringBoot自动配置Kafka
配置application.yml即可，实际上所有的配置实现都是在`org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration`中完成。
可以看出是依赖于@Configuration完成bean配置，这种配置方式基本能够实现大部分情况。

但是`org.springframework.boot.autoconfigure.kafka.KafkaProperties`中并没有涵盖所有的`org.apache.kafka.clients.producer.ProducerConfig`中的配置，这就导致某些特殊配置不能依赖SpringBoot自动创建，需要我们手动创建Producer和Consumer

# 手动配置Kafka
参考`org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration`创建配置类：KafkaConfig.java  
创建了对应类型的bean之后，org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration中的对应Bean定义将不起作用。  
通常用于配置多线程生产者、多线程消费者来提高Kafka的吞吐量

