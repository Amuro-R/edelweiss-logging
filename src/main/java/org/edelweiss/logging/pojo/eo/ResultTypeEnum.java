package org.edelweiss.logging.pojo.eo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
public enum ResultTypeEnum {
    SUCCESS("success", "成功"),
    FAIL("fail", "失败"),
    UNKNOWN("unknown", "未知"),
    ;
    public final String code;
    public final String codeDesc;

    ResultTypeEnum(String code, String codeDesc) {
        this.code = code;
        this.codeDesc = codeDesc;
    }

    private final static Map<String, ResultTypeEnum> CODE_MAP;

    static {
        CODE_MAP = new HashMap<>();
        for (ResultTypeEnum value : ResultTypeEnum.values()) {
            CODE_MAP.put(value.code, value);
        }
    }

    public static ResultTypeEnum getValueByCode(String code) {
        return CODE_MAP.get(code);
    }
}
