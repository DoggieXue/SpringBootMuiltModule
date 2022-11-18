package org.cloudxue.common.bean;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName LogEntityListener
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/5/31 10:54 上午
 * @Version 1.0
 **/
@Slf4j
public class LogEntityListener extends AnalysisEventListener<LogEntity> {
    private static int amount = 0;
    private static Map<String, String> appInfo ;
    static {
        appInfo = new HashMap<>();
        appInfo.put("1111111","我的账户");
        appInfo.put("2222222","转账汇款");
        appInfo.put("3333333","理财模块");

    }
    /**
     * 每读取一行内容，都会调用该对象的invoke方法，在invoke方法中可以操作使用读取到的数据
     * @param logEntity 每次读取到的数据封装成的对象
     * @param analysisContext
     */
    @Override
    public void invoke(LogEntity logEntity, AnalysisContext analysisContext) {
        amount++;
        String originalLog = logEntity.getOriginalLog();

        String[] logInfo = originalLog.split("\\^");
        System.out.println();
        for (int i = 0; i < logInfo.length; i++) {

            String logInfoDetail = logInfo[i];

            if (logInfoDetail.startsWith("errorMsg")){
                System.out.println(logInfoDetail);
            }

            if (logInfoDetail.startsWith("errorUrl")){
                System.out.println(logInfoDetail);
            }

            if (logInfoDetail.startsWith("viewId")){
                System.out.println(logInfoDetail);
            }

            if (logInfoDetail.startsWith("viewId")){
                String appId = logInfoDetail.split("=")[1].substring(8,16);
                System.out.println(appId + "-" + appInfo.get(appId));
            }

        }

    }

    /**
     * 全部读完之后，调用该方法
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println();
    }
}
