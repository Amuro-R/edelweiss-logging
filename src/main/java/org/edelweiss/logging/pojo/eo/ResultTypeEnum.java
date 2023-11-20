package org.edelweiss.logging.pojo.eo;

import java.util.HashMap;
import java.util.Map;

public enum ResultTypeEnum {
    SUCCESS(0, "成功"),
    FAIL(1, "失败"),
    UNKNOWN(2, "未知"),
    ;
    public final Integer code;

    public final String codeDesc;

    ResultTypeEnum(Integer code, String codeDesc) {
        this.code = code;
        this.codeDesc = codeDesc;
    }

    private final static Map<Integer, ResultTypeEnum> CODE_MAP;

    static {
        CODE_MAP = new HashMap<>();
        for (ResultTypeEnum value : ResultTypeEnum.values()) {
            CODE_MAP.put(value.code, value);
        }
    }

    public static ResultTypeEnum getValueByCode(Integer code) {
        return CODE_MAP.get(code);
    }
}
