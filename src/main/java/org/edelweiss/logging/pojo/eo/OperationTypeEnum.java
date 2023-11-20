package org.edelweiss.logging.pojo.eo;

import java.util.HashMap;
import java.util.Map;

public enum OperationTypeEnum {

    NONE("none", "空", -1, ""),
    EMPLOYEE("employee", "人员管理", 1, ""),
    OPERATION("operation", "手术管理", 1, ""),
    ACCESS_CONTROL("access_control", "门禁管理", 1, ""),
    MONITOR("monitor", "监控管理", 1, ""),
    ALARM("alarm", "预警中心", 1, ""),
    SYSTEM("system", "系统管理", 1, ""),
    // MULTIPART("multipart", "导入导出", 1, ""),
    OPERATION_ROOM("operation_room", "手术室管理", 1, ""),
    UNKNOWN("unknown", "未知", 0, ""),
    ;

    public final String code;

    public final String codeDesc;

    public final Integer type;

    public final String typeDesc;

    OperationTypeEnum(String code, String codeDesc, Integer type, String typeDesc) {
        this.code = code;
        this.codeDesc = codeDesc;
        this.type = type;
        this.typeDesc = typeDesc;
    }

    private static final Map<String, OperationTypeEnum> CODE_MAP;

    static {
        CODE_MAP = new HashMap<>();
        for (OperationTypeEnum value : OperationTypeEnum.values()) {
            CODE_MAP.put(value.code, value);
        }
    }

    public static OperationTypeEnum getValueByCode(String code) {
        return CODE_MAP.get(code);
    }
}
