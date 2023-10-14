package org.cloud.xue.common.config;


import io.netty.util.internal.StringUtil;
import io.netty.util.internal.SystemPropertyUtil;
import org.cloud.xue.common.anno.ConfigFileAnnotation;
import org.cloud.xue.common.util.ConfigProperties;

/**
 * @ClassName SystemConfig
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/10/20 5:09 下午
 * @Version 1.0
 **/
@ConfigFileAnnotation(file = "/system.properties")
public class SystemConfig extends ConfigProperties{
    //依照属性,从配置文件中，装载配置项
    static ConfigProperties singleton = new SystemConfig(ConfigProperties.getFileNameByAnnotation(SystemConfig.class));

    private SystemConfig(String fileName) {
        super(fileName);
        super.loadFromFile();
    }

    //服务器ip
    public static final String SOCKET_SERVER_IP = singleton.getValue("socket.server.ip");
    //服务器名称，为了方便抓包
    public static final String SOCKET_SERVER_NAME = singleton.getValue("socket.server.name");
    //服务器 port
    public static final int SOCKET_SERVER_PORT = singleton.getIntValue("socket.server.port");
    //发送文件路径
    public static final String SOCKET_SEND_FILE = singleton.getValue("socket.send.file");
    //第三方的类路径
    public static final String CLASS_SERVER_PATH = singleton.getValue("class.server.path");
    //宠物狗的类型
    public static final String PET_DOG_CLASS = singleton.getValue("pet.dog.class");

    public static final int SEND_SIZE = singleton.getIntValue("send.buffer.size");

    public static final int INPUT_SIZE = singleton.getIntValue("server.buffer.size");

    public static final String FILE_SRC_PATH = singleton.getValue("file.src.path");

    public static final String debug = singleton.getValue("debug");

    /**
     * 宠物工厂类的名称
     */
    public static final String PET_FACTORY_CLASS = singleton.getValue("pet.factory.class");

    /**
     * 宠物模块的类路径
     */
    public static final String PET_LIB_PATH = singleton.getValue("pet.lib.path");

    /**
     * 文件服务器的文件路径
     */
    public static final String FILE_SERVER_BASE_DIR = singleton.getValue("file.server.base.dir");

    /**
     * json的类型: gson/fastjson/Jackson
     *
     * json.strategy=fastjson
     */
    public static final String JSON_STRATEGY = singleton.getValue("json.strategy");

    /**
     * 获取文件服务器的文件路径
     *
     * @return
     */
    public static String getFileServerDir() {
        if (StringUtil.isNullOrEmpty(FILE_SERVER_BASE_DIR)) {
            return System.getProperty("user.dir");
        }
        return FILE_SERVER_BASE_DIR;
    }

    /**
     * 保存证书的文件路径
     */
    public static final String KEYSTORE_DIR = singleton.getValue("keystore.dir");

    public static String getKeystoreDir() {
        if (StringUtil.isNullOrEmpty(KEYSTORE_DIR)) {
            return SystemPropertyUtil.get("user.dir");
        }
        return FILE_SERVER_BASE_DIR;
    }
}
