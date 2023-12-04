package org.edelweiss.logging.pojo.eo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fzw
 * @date 2023/12/2
 **/
public enum LogLevelEnum {
    ERROR("ERROR", "错误"),
    WARN("WARN", "警告"),
    INFO("INFO", "正常"),
    DEBUG("DEBUG", "调试"),
    TRACE("TRACE", "详细"),
    ;
    public final String code;
    public final String codeDesc;

    LogLevelEnum(String code, String codeDesc) {
        this.code = code;
        this.codeDesc = codeDesc;
    }

    private final static Map<String, LogLevelEnum> CODE_MAP;

    static {
        CODE_MAP = new HashMap<>();
        for (LogLevelEnum value : LogLevelEnum.values()) {
            CODE_MAP.put(value.code, value);
        }
    }

    public static LogLevelEnum getValueByCode(String code) {
        return CODE_MAP.get(code);
    }
}
