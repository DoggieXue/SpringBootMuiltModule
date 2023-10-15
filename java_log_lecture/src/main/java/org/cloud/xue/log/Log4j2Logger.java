package org.cloud.xue.log;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j2Logger {
    private static final Logger logger = LogManager.getLogger(Log4j2Logger.class);

        public static void main(String[] args) {
        if (logger.isTraceEnabled()) {
            logger.debug("This is log4j trace message..");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("This is log4j debug message");
        }

        if (logger.isInfoEnabled()) {
            logger.debug("This is log4j info message");
        }
    }
}
