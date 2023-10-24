import lombok.extern.slf4j.Slf4j;
import org.cloud.xue.QuartzDemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

/**
 * @Description:
 * @Author: xuexiao
 * @Date: 2023年10月21日 23:11:55
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuartzDemoApplication.class)
@Slf4j
public class DefaultDataSourceTest {
    @Autowired
    DataSource dataSource;

    @Test
    public void contextLoad() {
        log.info("获取数据源信息：{}", dataSource.getClass());
    }
}
