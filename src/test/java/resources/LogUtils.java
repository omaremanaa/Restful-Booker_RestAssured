package resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtils {
    private static Logger logger = LogManager.getLogger();

    public static void info(String infoMessage) {
        logger.info(infoMessage);
    }

    public static void error(String errorMessage) {
        logger.error(errorMessage);
    }

    public static void warn(String warnMessage) {
        logger.warn(warnMessage);
    }

    public static void fatal(String fatalMessage) {
        logger.fatal(fatalMessage);
    }

    public static void debug(String debugMessage) {
        logger.debug(debugMessage);
    }


}
