package org.cloud.xue.common.zk.application.name_service;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.cloud.xue.common.zk.ZooKeeperClient;

/**
 * @ClassName DistributedIDMaker
 * @Description 基于zk的集群节点的命名服务
 * @Author xuexiao
 * @Date 2022/11/16 5:52 下午
 * @Version 1.0
 **/
@Slf4j
public class DistributedIDMaker {
    private transient ZooKeeperClient zkClient = null;

    private transient CuratorFramework curatorClient = null;

    //工作节点路径
    private String pathPrefix = "/test/IDMater/worker-";
    private String pathRegistered = null;

    private Long nodeId = null;

    public static DistributedIDMaker instance = new DistributedIDMaker();

    private DistributedIDMaker() {
        this.zkClient = ZooKeeperClient.instance;
        this.init();
    }

    public void init () {
        try {
            //创建临时顺序节点
            pathRegistered = zkClient.createNodeWithMode(pathPrefix, pathPrefix.getBytes("UTF-8"), CreateMode.EPHEMERAL_SEQUENTIAL);
            log.info("分布式节点生成器初始化完毕，工作节点路径： {}", pathRegistered);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取临时顺序节点的ID
     * @return
     */
    public long getNodeId() {
        if (null != nodeId) {
            return nodeId;
        }

        if (null == pathRegistered) {
            throw new RuntimeException("临时节点注册失败！");
        }
        String sid = null;
        int index = pathRegistered.lastIndexOf(pathPrefix);
        if (index >= 0) {
            index += pathPrefix.length();
            sid = index <= pathRegistered.length() ? pathRegistered.substring(index) : null;
        }
        if (null == sid) {
            throw new RuntimeException("节点ID生成失败！");
        }
        nodeId = Long.parseLong(sid);

        return nodeId;
    }
}
