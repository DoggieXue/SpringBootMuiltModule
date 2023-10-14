package org.cloud.xue.zk.application.publish_subscribe;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.cloud.xue.common.zk.ZooKeeperClient;
import org.junit.Test;

/**
 * @ClassName ZKWatcherDemo
 * @Description Curator实现的分布式监听
 * @Author xuexiao
 * @Date 2022/11/17 4:01 下午
 * @Version 1.0
 **/
@Slf4j
public class ZKWatcherDemo {
    private String workPath = "/test/listener/remoteNode";
    private String subWorkPath = "/test/listener/remoteNode/id-";

    /**
     * Watcher监听
     * 监听节点的内容变更了两次，实际Watcher监听器只监听到了一个事件
     */
    @Test
    public void testWatcher() {
        CuratorFramework zkClient = ZooKeeperClient.instance.getZkClient();

        boolean isExist = ZooKeeperClient.instance.isNodeExist(workPath);
        if (!isExist) {
            ZooKeeperClient.instance.createNode(workPath, "");
        }

        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("监听到的变化 watchedEvent = " + event);
            }
        };

        try {
            byte[] content = zkClient.getData().usingWatcher(watcher).forPath(workPath);
            log.info("监听节点内容： " + new String(content));

            zkClient.setData().forPath(workPath, "第一次变更节点内容".getBytes("UTF-8"));

            zkClient.setData().forPath(workPath, "第二次变更节点内容".getBytes("UTF-8"));
            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNodeCache() {
        CuratorFramework zkClient = ZooKeeperClient.instance.getZkClient();

        boolean isExist = ZooKeeperClient.instance.isNodeExist(workPath);
        if (!isExist) {
            ZooKeeperClient.instance.createNode(workPath, null);
        }
        try {
            //创建NodeCache
            NodeCache nodeCache = new NodeCache(zkClient, workPath, false);
            //创建NodeCacheListener监听器
            NodeCacheListener listener = new NodeCacheListener() {
                @Override
                public void nodeChanged() throws Exception {
                    ChildData childData = nodeCache.getCurrentData();
                    log.info("ZNode节点状态改变，path = {}", childData.getPath());
                    log.info("ZNode节点状态改变，data = {}", new String(childData.getData(), "UTF-8"));
                    log.info("ZNode节点状态该表，stat = {}", childData.getStat());
                }
            };

            //启动节点的事件监听
            nodeCache.getListenable().addListener(listener);
            nodeCache.start();

            zkClient.setData().forPath(workPath, "第一次变更节点内容".getBytes("UTF-8"));
            Thread.sleep(1000);

            zkClient.setData().forPath(workPath, "第二次变更节点内容".getBytes("UTF-8"));
            Thread.sleep(1000);

            zkClient.setData().forPath(workPath, "第三次变更节点内容".getBytes("UTF-8"));
            Thread.sleep(1000);
        } catch (Exception e) {
            log.error("创建NodeCache监听失败， path={}", workPath);
        }
    }

    @Test
    public void testPathChildrenCache() {
        CuratorFramework zkClient = ZooKeeperClient.instance.getZkClient();

        boolean isExist = ZooKeeperClient.instance.isNodeExist(workPath);
        if (!isExist) {
            ZooKeeperClient.instance.createNode(workPath, null);
        }

        PathChildrenCache cache = new PathChildrenCache(zkClient, workPath, true);
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                ChildData data = event.getData();
                switch (event.getType()) {
                    case CHILD_ADDED:
                        log.info("子节点增加，path={}, data={}", data.getPath(), new String(data.getData(), "UTF-8"));
                        break;
                    case CHILD_UPDATED:
                        log.info("子节点更新，path={}, data={}", data.getPath(), new String(data.getData(), "UTF-8"));
                        break;
                    case CHILD_REMOVED:
                        log.info("子节点删除，path={}, data={}", data.getPath(), new String(data.getData(), "UTF-8"));
                        break;
                    default:
                        break;
                }
            }
        };

        try {
            cache.getListenable().addListener(listener);
            cache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);//启动时同步初始化Cache

            Thread.sleep(1000);

            //创建三个子节点
            for (int i = 0; i < 3; i++){
                ZooKeeperClient.instance.createNode(subWorkPath + i, null);
            }
            Thread.sleep(1000);

            //删除三个子节点
            for (int j = 0; j < 3; j++) {
                ZooKeeperClient.instance.deleteNode(subWorkPath + j);
            }
        } catch (Exception e) {
            log.error("创建PathChildCache监听失败， path={}", workPath);
        }
    }

    /**
     * TreeCache不仅可以监听子节点，也能监听节点本身
     */
    @Test
    public void testTreeCache() {
        boolean isExist = ZooKeeperClient.instance.isNodeExist(workPath);
        if (!isExist) {
            ZooKeeperClient.instance.createNode(workPath, null);
        }

        CuratorFramework client = ZooKeeperClient.instance.getZkClient();

        try {
            TreeCache treeCache = new TreeCache(client, workPath);
            TreeCacheListener listener = new TreeCacheListener() {
                @Override
                public void childEvent(CuratorFramework client, TreeCacheEvent treeCacheEvent) throws Exception {
                    ChildData data = treeCacheEvent.getData();
                    if (data == null) {
                        log.info("数据为空！");
                        return;
                    }
                    switch (treeCacheEvent.getType()) {
                        case NODE_ADDED:
                            log.info("[TreeCache]节点增加，path={}, data={}", data.getPath(), new String(data.getData(), "UTF-8"));
                            break;
                        case NODE_UPDATED:
                            log.info("[TreeCache]节点更新，path={}, data={}", data.getPath(), new String(data.getData(), "UTF-8"));
                            break;
                        case NODE_REMOVED:
                            log.info("[TreeCache]节点删除，path={}, data={}", data.getPath(), new String(data.getData(), "UTF-8"));
                            break;
                        default:
                            break;
                    }
                }
            };
            //设置监听器
            treeCache.getListenable().addListener(listener);
            //启动监听
            treeCache.start();
            Thread.sleep(1000);

            //创建3个子节点
            for (int i = 0; i < 3; i++) {
                ZooKeeperClient.instance.createNode(subWorkPath + i, null);
            }
            Thread.sleep(1000);

            //删除3个子节点
            for (int i = 0; i < 3; i++) {
                ZooKeeperClient.instance.deleteNode(subWorkPath + i);
            }
            Thread.sleep(1000);

            //删除当前节点
            ZooKeeperClient.instance.deleteNode(workPath);
            Thread.sleep(Integer.MAX_VALUE);

        } catch (Exception e) {
            log.error("创建TreeCache监听失败， path={}", workPath);
        }
    }
}
