package org.cloud.xue.log;

import java.util.logging.Logger;

/**
 * @Description:
 * @Author: xuexiao
 * @Date: 2023年10月15日 22:00:21
 **/
public class JdkLogger {
    private static final Logger logger = Logger.getLogger(JdkLogger.class.getName());

    public static void main(String[] args) {
        logger.info("jdk logging info: this is one message");
    }

}
