package org.edelweiss.logging.aspect.part;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
public enum PartType {

    PLAIN_STRING(0, "空字符串块", "", "", "", "", ""),
    SPEL_STRING(1, "SpEl字符串块", "#", "{#", "#}", "[#", "#]"),
    FUNCTION_STRING(2, "方法字符串块", "@", "{@", "@}", "[@", "@]"),
    ;
    public final Integer code;
    public final String codeDesc;
    public final String identifier;
    public final String beforePreIdentifier;
    public final String beforeSubIdentifier;
    public final String afterPreIdentifier;
    public final String afterSubIdentifier;

    PartType(Integer code, String codeDesc, String identifier, String beforePreIdentifier, String beforeSubIdentifier,
             String afterPreIdentifier, String afterSubIdentifier) {
        this.code = code;
        this.codeDesc = codeDesc;
        this.identifier = identifier;
        this.beforePreIdentifier = beforePreIdentifier;
        this.beforeSubIdentifier = beforeSubIdentifier;
        this.afterPreIdentifier = afterPreIdentifier;
        this.afterSubIdentifier = afterSubIdentifier;
    }
}
