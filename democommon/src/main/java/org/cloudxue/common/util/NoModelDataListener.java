package org.cloudxue.common.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ClassName NoModelDataListener
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/4/7 上午10:36
 * @Version 1.0
 **/
@Slf4j
public class NoModelDataListener extends AnalysisEventListener<Map<Integer, String>> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    private List<Map<Integer, String>> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        //log.info("解析到一条数据:{}", JSON.toJSONString(data));
        String originalLog = data.get(3);
//        log.info("originalLog数据:{}", originalLog);
        List<String> contentList = Arrays.asList(originalLog.split("\\^"));
//        log.info("总共" + contentList.size() + "条数据");
//        log.info("contentList 数据:{}", contentList.toString());

        for (int i = 0; i < contentList.size(); i++){
//            log.info("！！！！！" + contentList.get(i));
//            if (contentList.get(i).indexOf("errorMsg") > -1) {
//                log.info("！！！！！" + contentList.get(i));
//                System.out.println(contentList.get(i));
//            }
            if (contentList.get(i).indexOf("errorUrl") > -1) {
//                log.info("！！！！！" + contentList.get(i));
                String s = contentList.get(i);
//                s.indexOf("=");
                System.out.println(s.substring(s.indexOf("=")+1));
            }
        }


//        cachedDataList.add(data);
//        if (cachedDataList.size() >= BATCH_COUNT) {
//            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
//        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        log.info("存储数据库成功！");
    }
}
