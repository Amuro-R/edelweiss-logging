package org.edelweiss.logging.exception;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
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
