package org.edelweiss.logging.aspect.part;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
@Data
public class StringPart {

    private final String part;

    private Object value;

    private final PartType partType;

    private final boolean before;

    private final List<StringPart> functionParam;

    public StringPart(String part, PartType partType, boolean before) {
        this.part = part;
        this.partType = partType;
        this.before = before;
        this.functionParam = new ArrayList<>();
    }

    public void addFunctionArg(StringPart arg) {
        this.functionParam.add(arg);
    }
}
