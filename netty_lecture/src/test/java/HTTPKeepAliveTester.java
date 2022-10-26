import org.cloudxue.common.util.HttpClientHelper;
import org.cloudxue.common.util.JdkHttpURLConnectionHelper;
import org.cloudxue.common.util.OkHttpUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
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

//    private String url = "http://10.238.144.120:8891/ifp-creditcard-service/ifp-api-docs.do";
    private String url = "http://127.0.0.1:18899/";
    private ExecutorService pool = Executors.newFixedThreadPool(10);

    /**
     * 使用JDK的 java.net.HttpURLConnection发起HTTP请求
     */
    @Test
    public void simpleGet() throws InterruptedException{
        int index = 1000000;
        while (--index > 0) {
            String target = url + index;
            pool.submit(() -> {
                String out = JdkHttpURLConnectionHelper.jdkGet(target);
                System.out.println("out = " + out);
            });
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     *
     * @throws InterruptedException
     */
    @Test
    public void pooledGet() throws InterruptedException {
        int index = 10000;
        while (--index > 0) {
            String target = url + index;
            pool.submit(() -> {
               String out = HttpClientHelper.get(target);
                System.out.println("======out====" + out);
            });
        }
        Thread.sleep(Integer.MAX_VALUE);
    }


    @Test
    public void okHttpGet() throws InterruptedException{
//        int index = 100;
//        while (--index > 0) {
//            String target = url + index;
//            Map<String, String> headers = new HashMap<>();
//            headers.put("Content-Type", "application/x-www-form-urlencoded");
//            headers.put("User-Agent","PostmanRuntime/7.29.2");
//            pool.submit(() -> {
//                String out = OkHttpUtil.simpleGet(target,headers);
//                System.out.println("out" + out);
//            });
//        }
//        Thread.sleep(Integer.MAX_VALUE);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("User-Agent","PostmanRuntime/7.29.2");
        String out = OkHttpUtil.simpleGet(url,headers);
        System.out.println("out" + out);
    }
}
