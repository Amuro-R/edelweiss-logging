package org.edelweiss.logging.log.el.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.edelweiss.logging.log.el.ILogParseFunction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonParseFunction implements ILogParseFunction {

    private static ObjectMapper objectMapper = new ObjectMapper();

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
                    return objectMapper.writeValueAsString(obj);
                } catch (JsonProcessingException e) {
                    log.error("参数json序列化失败", e);
                    return null;
                }
            }
        }
        return null;
    }
}
