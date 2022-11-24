package org.cloudxue.common.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * @ClassName ZooKeeperClient
 * @Description 自定义zk操作类  单例形式对外提供服务，操作ZooKeeper
 * @Author xuexiao
 * @Date 2022/11/16 11:00 上午
 * @Version 1.0
 **/
public class ZooKeeperClient {

    private static final String ZK_ADDRESS = "127.0.0.1:2181";

    private CuratorFramework zkClient;

    public static ZooKeeperClient instance = null;

    private ZooKeeperClient(){

    }

    static {
        instance = new ZooKeeperClient();
        instance.init();
    }

    public void init() {
        if (null != zkClient) {
            return;
        }
        //创建ZooKeeper操作客户端实例
        zkClient = ClientFactory.createSimpleClient(ZK_ADDRESS);

        //启动客户端实例，连接ZooKeeper服务器
        zkClient.start();
    }

    /**
     * 断开ZK连接
     */
    public void destroy() {
        CloseableUtils.closeQuietly(zkClient);
    }

    /**
     * 获取Curator客户端实例
     * @return
     */
    public CuratorFramework getZkClient() {
        if (null == zkClient) {
            this.init();
        }
        return zkClient;
    }

    /**
     * 创建ZNode永久节点
     * @param zkPath
     * @param data
     */
    public void createNode(String zkPath, Object data) {
        try {
            byte[] payload = "to set content".getBytes("UTF-8");
            if (data != null) {
                String content = (String)data;
                payload = content.getBytes("UTF-8");
            }
            zkClient.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(zkPath, payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查节点是否存在
     * @param zkPath
     * @return
     */
    public boolean isNodeExist(String zkPath) {
        try {
            Stat stat = zkClient.checkExists().forPath(zkPath);
            if (null != stat) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除ZNode节点
     * @param zkPath
     */
    public void deleteNode(String zkPath) {
        try {
            if (!isNodeExist(zkPath)) {
                return;
            }
            zkClient.delete().forPath(zkPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建 不带负载的 临时 顺序 节点
     * @param zkPath
     * @return 节点路径
     */
    public String createEphemeralSeqNode(String zkPath) {
        try {
            return zkClient.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(zkPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建不带负载的节点
     * @param zkPath
     * @param mode 节点类型
     * @return 节点路径
     */
    public String createNodeWithMode(String zkPath, CreateMode mode) {
        try {
            return zkClient.create()
                    .creatingParentsIfNeeded()
                    .withMode(mode)
                    .forPath(zkPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建带负载的节点
     * @param zkPath
     * @param payload
     * @param mode
     * @return
     */
    public String createNodeWithMode(String zkPath, byte[] payload, CreateMode mode) {
        try {
            return zkClient.create()
                    .creatingParentsIfNeeded()
                    .withMode(mode)
                    .forPath(zkPath, payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
