package org.cloud.xue.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Description:
 * @Author: xuexiao
 * @Date: 2023年10月20日 09:27:06
 **/
@Data
@NoArgsConstructor
public class QuartzConfigDTO {
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 任务所属组
     */
    private String groupName;
    /**
     * 任务执行类
     */
    private String jobClass;
    /**
     * 任务调度时间表达式
     */
    private String cronExpression;
    /**
     * 附加参数
     */
    private Map<String, Object> param;
}
