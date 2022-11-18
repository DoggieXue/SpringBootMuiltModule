package org.cloudxue.zk.basic.operate;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.cloudxue.common.zk.ClientFactory;
import org.junit.Test;

import java.util.List;

/**
 * @ClassName ZKCRUD
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/11/16 5:42 下午
 * @Version 1.0
 **/
@Slf4j
public class ZookeeperCRUD {
    public static final String ZK_ADDRESS = "127.0.0.1:2181";

    @Test
    public void checkNode() {
        CuratorFramework client = ClientFactory.createSimpleClient(ZK_ADDRESS);

        try {
            client.start();

            String zkPath = "/test/CRUD/node-1";
            long startTime = System.currentTimeMillis();
            Stat stat = client.checkExists().forPath(zkPath);
            if (null == stat) {
                log.info("{}节点不存在", zkPath);
            } else {
                log.info("节点存在， stat is {}",stat.toString());
            }
            log.info("forPath耗时ms: " + (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }


    @Test
    public void createNode() {
        CuratorFramework client = ClientFactory.createSimpleClient(ZK_ADDRESS);

        try {
            client.start();

            String data = "hello";
            byte[] payload = data.getBytes("UTF-8");
            String zkPath = "/test/CRUD/node-1";
            client.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(zkPath, payload);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }

    }

    @Test
    public void readNode() {
        CuratorFramework client = ClientFactory.createSimpleClient(ZK_ADDRESS);

        try {
            client.start();
            String zkPath = "/test/CRUD/node-1";
            Stat stat = client.checkExists().forPath(zkPath);
            if (null != stat) {
                byte[] payload = client.getData().forPath(zkPath);
                String data = new String(payload, "UTF-8");
                log.info("========read data from zk: {}", data);

                String parentPath = "/test";
                List<String> children = client.getChildren().forPath(parentPath);
                for (String child : children) {
                    log.info("******child: {}", child);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    @Test
    public void updateNode() {
        CuratorFramework client = ClientFactory.createSimpleClient(ZK_ADDRESS);
        try {
            client.start();
            String data = "hello world";
            byte[] payload = data.getBytes("UTF-8");
            String zkPath = "/test/CRUD/node-1";
            client.setData().forPath(zkPath, payload);

        } catch (Exception e) {

        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    /**
     * 异步更新
     */
    @Test
    public void updateNodeAsync() {
        CuratorFramework client = ClientFactory.createSimpleClient(ZK_ADDRESS);
        try {
            AsyncCallback.StringCallback callback = new AsyncCallback.StringCallback() {
                @Override
                public void processResult(int rc, String path, Object ctx, String name) {
                    System.out.println("------------rc = " + rc + "|  " +
                            "path = " + path + " | " +
                            "ctx = " + ctx + " | " +
                            "name = " + name);
                }
            };

            client.start();
            byte[] payload = "hello, everyone!".getBytes("UTF-8");
            String zkPath = "/test/CRUD/node-1";
            client.setData()
                    .inBackground(callback)
                    .forPath(zkPath, payload);

            Thread.sleep(10000);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    @Test
    public void deleteNode() {
        CuratorFramework client = ClientFactory.createSimpleClient(ZK_ADDRESS);

        try {
            client.start();

            String zkPath = "/test/CRUD/node-1";
            client.delete().forPath(zkPath);

            String parentPath = "/test/CRUD";
            List<String> children = client.getChildren().forPath(parentPath);
            for (String child : children) {
                log.info("++++child: {}", child);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }
}
