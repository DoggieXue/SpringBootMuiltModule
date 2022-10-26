package org.cloudxue.common.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName OkHttpUtil
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/10/25 4:39 下午
 * @Version 1.0
 **/
@Slf4j
public class OkHttpUtil {
    /**
     * 最大连接时间
     */
    private final static int CONNECTION_TIMEOUT = 5;

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    /**
     * OkHTTP线程池 最大空闲线程数
     */
    public static final int MAX_IDLE_CONNECTIONS = 100;
    /**
     * OkHTTP线程池 空闲线程的存活时间
     */
    public static final long KEEP_ALIVE_DURATION = 30L;

    /**
     * 全局变量
     */
    public final static OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION, TimeUnit.MINUTES))
            .build();

    /**
     * 发送GET请求
     * @param url
     * @param headers
     * @return
     */
    public static String simpleGet(String url, Map<String, String> headers) {
        try {
            Request.Builder builder = new Request.Builder();
            setHttpHeader(builder, headers);
            Request request = builder.url(url).build();
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            if (response.isSuccessful() && Objects.nonNull(response.body())) {
                String result = response.body().toString();
                log.info("执行GET请求，url: {}成功，返回数据：{}", url, result);
                return result;
            }
        } catch (IOException e) {
            log.error("执行get请求，url: {} 失败!", url, e);
        }
        return null;
    }

    /**
     * 发送POST请求
     * @param url
     * @param headers
     * @param json
     * @return
     */
    public static String simplePost(String url, Map<String, String> headers, String json) {
        try {
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
            Request.Builder builder = new Request.Builder();
            setHttpHeader(builder, headers);
            Request request = builder.url(url).post(body).build();
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            if (response.isSuccessful() && Objects.nonNull(response.body())) {
                String result = response.body().string();
                log.info("执行post请求,url: {}, header: {} ,参数: {} 成功，返回结果: {}", url, headers, json, result);
                return result;
            }
        } catch (Exception e) {
            log.error("执行post请求，url: {},参数: {} 失败!", url, json, e);
        }
        return null;
    }

    /**
     * 提交POST表单
     * @param url
     * @param params
     * @return
     */
    public static String postForm(String url, Map<String, String> params) {
        try {

            FormBody.Builder builder = new FormBody.Builder();
            if (!CollectionUtils.isEmpty(params)) {
                params.forEach(builder::add);
            }
            FormBody body = builder.build();
            Request request = new Request.Builder().url(url).post(body).build();
            Response response = OK_HTTP_CLIENT.newCall(request).execute();
            //调用成功
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 设置HTTP请求报文头
     * @param builder
     * @param headers
     */
    private static void setHttpHeader(Request.Builder builder, Map<String, String> headers) {
        if (Objects.nonNull(headers) && headers.size() > 0) {
            headers.forEach((k, v) -> {
                if (Objects.nonNull(k) && Objects.nonNull(v)){
                    builder.addHeader(k, v);
                }
            });
        }
    }
}
