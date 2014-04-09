package halfpipe.logging;

import ch.qos.logback.classic.Level;
import halfpipe.properties.LoggingProperties;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * User: spencergibb
 * Date: 4/9/14
 * Time: 1:05 PM
 */
public class LogbackConfigurer {
    @Inject
    LoggingProperties properties;

    @Log Logger logger;

    @PostConstruct
    public void init() {
        logger.info("Setting rootLogger to {}", properties.getLevel());
        setLogLevel(null, properties.getLevel());

        for (String entry: properties.getLoggers()) {
            String[] entryValues = entry.split(":");
            if (entryValues.length != 2) {
                throw new IllegalArgumentException(entry +" is an illegal loggers entry.  Must be of form 'log.name=LEVEL");
            }
            Level level = Level.valueOf(entryValues[1]);
            String loggerName = entryValues[0];
            logger.info("Setting {} to {}", loggerName, level);
            setLogLevel(loggerName, level);
        }
    }

    public void setLogLevel(String loggerName, Level level) {
        ILoggerFactory factory = StaticLoggerBinder.getSingleton().getLoggerFactory();
        Logger slf4jLogger = factory .getLogger(StringUtils.isEmpty(loggerName) ? Logger.ROOT_LOGGER_NAME : loggerName);
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) slf4jLogger;
        logger.setLevel(level);
    }
}
