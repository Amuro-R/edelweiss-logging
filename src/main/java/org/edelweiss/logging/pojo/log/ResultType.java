package org.edelweiss.logging.pojo.log;

import java.util.HashMap;
import java.util.Map;

public enum ResultType {
    SUCCESS(0, "成功"),
    FAIL(1, "失败"),
    UNKNOWN(2, "未知"),
    ;
    public final Integer code;

    public final String codeDesc;

    ResultType(Integer code, String codeDesc) {
        this.code = code;
        this.codeDesc = codeDesc;
    }

    private final static Map<Integer, ResultType> CODE_MAP;

    static {
        CODE_MAP = new HashMap<>();
        for (ResultType value : ResultType.values()) {
            CODE_MAP.put(value.code, value);
        }
    }

    public static ResultType getValueByCode(Integer code) {
        return CODE_MAP.get(code);
    }
}
