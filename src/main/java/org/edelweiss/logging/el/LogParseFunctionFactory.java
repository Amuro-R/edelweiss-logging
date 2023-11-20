package org.edelweiss.logging.el;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class LogParseFunctionFactory {

    private final Map<String, ILogParseFunction> logParseFunctionMap = new HashMap<>();

    public LogParseFunctionFactory(List<ILogParseFunction> logParseFunctionList) {
        for (ILogParseFunction logParseFunction : logParseFunctionList) {
            logParseFunctionMap.put(logParseFunction.functionName(), logParseFunction);
        }
    }

    public ILogParseFunction getLogParseFunction(String name) {
        return this.logParseFunctionMap.get(name);
    }
}
