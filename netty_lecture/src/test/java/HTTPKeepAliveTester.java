import org.cloudxue.common.util.HttpClientHelper;
import org.cloudxue.common.util.JdkHttpURLConnectionHelper;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName HTTPKeepAliveTester
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/10/20 2:18 下午
 * @Version 1.0
 **/
public class HTTPKeepAliveTester {

    private String url = "http://10.238.144.120:8891/ifp-creditcard-service/ifp-api-docs.do";
    private ExecutorService pool = Executors.newFixedThreadPool(10);

    /**
     * 使用JDK的 java.net.HttpURLConnection发起HTTP请求
     */
    @Test
    public void simpleGet() throws InterruptedException{
        int index = 1000000;
        while (--index > 0) {
            pool.submit(() -> {
                String out = JdkHttpURLConnectionHelper.jdkGet(url);
                System.out.println("out = " + out);
            });
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void pooledGet() throws InterruptedException {
        int index = 1000000;
        while (--index > 0) {
            pool.submit(() -> {
               String out = HttpClientHelper.get(url);
                System.out.println("out" + out);
            });
        }
        Thread.sleep(Integer.MAX_VALUE);
    }


}
