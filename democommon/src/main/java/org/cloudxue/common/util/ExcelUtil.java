package org.cloudxue.common.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import org.cloudxue.common.bean.LogEntity;
import org.cloudxue.common.bean.LogEntityListener;

/**
 * @ClassName ExcelUtil
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/4/7 上午10:09
 * @Version 1.0
 **/
public class ExcelUtil {
    public static void main(String[] args) {

        ExcelReaderBuilder readerBuilder = EasyExcel.read("/Users/xuexiao/Downloads/log3.xlsx", LogEntity.class, new LogEntityListener());

        ExcelReaderSheetBuilder sheetBuilder = readerBuilder.sheet();

        sheetBuilder.doRead();
    }
}
