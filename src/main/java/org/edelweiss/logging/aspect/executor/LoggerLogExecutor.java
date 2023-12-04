package org.edelweiss.logging.aspect.executor;

import lombok.extern.slf4j.Slf4j;
import org.edelweiss.logging.pojo.eo.LogLevelEnum;
import org.edelweiss.logging.pojo.po.LogPO;

/**
 * @author fzw
 * @date 2023/12/2
 **/
@Slf4j
public class LoggerLogExecutor implements LogExecutor {
    @Override
    public Object execute(LogPO logPO) {
        String logLevelStr = logPO.getLogLevel();
        LogLevelEnum logLevel = LogLevelEnum.getValueByCode(logLevelStr);
        if (logLevel == null) {
            logLevel = LogLevelEnum.INFO;
        }
        switch (logLevel) {
            case ERROR:
                log.error("{}", logPO);
                break;
            case WARN:
                log.warn("{}", logPO);
                break;
            case DEBUG:
                log.debug("{}", logPO);
                break;
            case TRACE:
                log.trace("{}", logPO);
                break;
            default:
                log.info("{}", logPO);
        }
        return null;
    }
}
