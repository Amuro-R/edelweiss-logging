package org.edelweiss.logging.config;

import org.edelweiss.logging.aspect.LogAspect;
import org.edelweiss.logging.aspect.executor.LogExecutor;
import org.edelweiss.logging.aspect.executor.NopLogExecutor;
import org.edelweiss.logging.aspect.processor.NopResultPostProcessor;
import org.edelweiss.logging.aspect.processor.ResultPostProcessor;
import org.edelweiss.logging.el.ILogParseFunction;
import org.edelweiss.logging.el.LogParseFunctionRegistry;
import org.edelweiss.logging.properties.LogProperties;
import org.edelweiss.logging.registry.LogExecutorRegistry;
import org.edelweiss.logging.registry.LogResultPostProcessorRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class LogDefaultConfig {

    @Bean("edelweiss.log.properties")
    @ConfigurationProperties(prefix = "edelweiss.log.prop")
    public LogProperties logProperties() {
        return new LogProperties();
    }

    @Bean("edelweiss.log.aspect")
    public LogAspect logAspect() {
        return new LogAspect();
    }

    @Bean("edelweiss.log.executor.default")
    @ConditionalOnMissingBean({LogExecutor.class})
    public LogExecutor defaultLogExecutor() {
        return new NopLogExecutor();
    }

    @Bean("edelweiss.log.result.post.processor.default")
    @ConditionalOnMissingBean({ResultPostProcessor.class})
    public ResultPostProcessor defaultResultPostProcessor() {
        return new NopResultPostProcessor();
    }

    @Bean("edelweiss.log.parse.function.registry")
    public LogParseFunctionRegistry logParseFunctionRegistry(List<ILogParseFunction> logParseFunctionList) {
        return new LogParseFunctionRegistry(logParseFunctionList);
    }

    @Bean("edelweiss.log.executor.registry")
    public LogExecutorRegistry logExecutorRegistry(List<LogExecutor> executors) {
        return new LogExecutorRegistry(executors);
    }

    @Bean("edelweiss.log.result.post.processor.registry")
    public LogResultPostProcessorRegistry logResultPostProcessorRegistry(List<ResultPostProcessor> processors) {
        return new LogResultPostProcessorRegistry(processors);
    }

}
