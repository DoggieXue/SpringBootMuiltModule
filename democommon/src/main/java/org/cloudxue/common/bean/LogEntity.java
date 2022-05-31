package org.cloudxue.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName LogEntity
 * @Description 日志实体类，按照实际Excel定义
 * @Author xuexiao
 * @Date 2022/5/31 10:48 上午
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogEntity {
    private String cDateTime;

    private String clientLogTime;

    private String dateTime;

    private String originalLog;

    private String sDateTime;

    private String simpleLog;

    private String traceId;

    private String userId;

    private String utdid;
}
