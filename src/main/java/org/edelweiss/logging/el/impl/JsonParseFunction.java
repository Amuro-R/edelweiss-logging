package org.edelweiss.logging.el.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.edelweiss.logging.el.ILogParseFunction;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
@Slf4j
public class JsonParseFunction implements ILogParseFunction {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String functionName() {
        return "JsonParseFunction";
    }

    @Override
    public Object parse(Object[] args) {
        if (args != null && args.length > 0) {
            if (args[0] != null) {
                Object obj = args[0];
                try {
                    return OBJECT_MAPPER.writeValueAsString(obj);
                } catch (JsonProcessingException e) {
                    log.error("参数json序列化失败", e);
                    return null;
                }
            }
        }
        return null;
    }
}
