package org.edelweiss.logging.el;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.edelweiss.logging.registry.AbstractRegistry;

import java.util.List;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
@Slf4j
@NoArgsConstructor
public class LogParseFunctionRegistry extends AbstractRegistry<String, ILogParseFunction> {

    public LogParseFunctionRegistry(List<ILogParseFunction> logParseFunctionList) {
        for (ILogParseFunction logParseFunction : logParseFunctionList) {
            this.register(logParseFunction.functionName(), logParseFunction);
        }
    }

    @Override
    public ILogParseFunction get(String key) {
        return this.getContainer().get(key);
    }

    @Override
    public boolean register(String key, ILogParseFunction data) {
        this.getContainer().put(key, data);
        return true;
    }

    @Override
    public boolean unregister(String key) {
        this.getContainer().remove(key);
        return true;
    }
}
