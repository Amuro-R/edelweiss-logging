package org.edelweiss.logging.pojo.eo;

/**
 * @author jingyun
 * @date 2022-07-22
 */
public enum LogExecutorType {

    NOP(0, "无操作", 0, ""),
    CONSOLE(1, "控制台输出", 0, ""),
    ES(2, "ElasticSearch", 0, ""),
    DB(3, "数据库", 0, ""),
    FILE(4, "文件", 0, ""),
    ;
    public final Integer code;

    public final String codeDesc;

    public final Integer type;

    public final String typeDesc;

    LogExecutorType(Integer code, String codeDesc, Integer type, String typeDesc) {
        this.code = code;
        this.codeDesc = codeDesc;
        this.type = type;
        this.typeDesc = typeDesc;
    }
}
