package org.cloudxue.common.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @ClassName ClientFactory
 * @Description Curator客户端实例创建工厂
 * @Author xuexiao
 * @Date 2022/11/14 11:27 上午
 * @Version 1.0
 **/
public class ClientFactory {
    /**
     *  创建简单的zk连接地址
     * @param connectString ZK连接地址
     * @return
     */
    public static CuratorFramework createSimpleClient(String connectString) {
        //重试策略
        //第一个参数：重试等待时间的基础单位，ms
        //第二个参数：最大重试次数
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);

        return CuratorFrameworkFactory.newClient(connectString, retryPolicy);
    }

    /**
     * 通过CuratorFrameworkFactory的静态builder构造方法创建CuratorFramework客户端实例
     * @param connectString zk连接地址
     * @param retryPolicy   zk连接超时重试策略
     * @param connectionTimeoutMs 连接超时时间
     * @param sessionTimeoutMs  会话超时时间
     * @return
     */
    public static CuratorFramework createWithOptions(String connectString, RetryPolicy retryPolicy, int connectionTimeoutMs, int sessionTimeoutMs) {
        return CuratorFrameworkFactory.builder().connectString(connectString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .build();
    }
}
