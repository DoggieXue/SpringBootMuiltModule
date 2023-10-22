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
public class DefaultDataSourceTest {
    @Autowired
    DataSource dataSource;

    @Test
    public void contextLoad() {
        System.out.println("******************" + dataSource.getClass());
    }
}
