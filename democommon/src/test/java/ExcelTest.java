import com.alibaba.excel.EasyExcel;
import org.cloudxue.common.util.NoModelDataListener;
import org.junit.Test;

/**
 * @ClassName ExcelTest
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/4/7 上午10:39
 * @Version 1.0
 **/
public class ExcelTest {
    @Test
    public void noModelRead() {
        String fileName = "/Users/xuexiao/Downloads/log0402.xlsx";
        // 这里 只要，然后读取第一个sheet 同步读取会自动finish
        EasyExcel.read(fileName, new NoModelDataListener()).sheet().doRead();
    }
}
