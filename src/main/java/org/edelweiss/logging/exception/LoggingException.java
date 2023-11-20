package org.edelweiss.logging.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author fzw
 * @date 2023/11/20
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LoggingException extends RuntimeException {

    public LoggingException() {
    }

    public LoggingException(String message) {
        super(message);
    }

    public LoggingException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoggingException(Throwable cause) {
        super(cause);
    }

    public LoggingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
