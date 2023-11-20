package org.edelweiss.logging.config;

import org.edelweiss.logging.aspect.LogAspect;
import org.edelweiss.logging.aspect.executor.LogExecutor;
import org.edelweiss.logging.aspect.executor.NopLogExecutor;
import org.edelweiss.logging.aspect.processor.ControllerResultPostProcessor;
import org.edelweiss.logging.aspect.processor.NopResultPostProcessor;
import org.edelweiss.logging.aspect.processor.ResultPostProcessor;
import org.edelweiss.logging.el.ILogParseFunction;
import org.edelweiss.logging.el.LogParseFunctionRegistry;
import org.edelweiss.logging.el.impl.DateParseFunction;
import org.edelweiss.logging.el.impl.JsonParseFunction;
import org.edelweiss.logging.el.impl.MultipartParseFunction;
import org.edelweiss.logging.properties.LogProperties;
import org.edelweiss.logging.registry.LogExecutorRegistry;
import org.edelweiss.logging.registry.LogResultPostProcessorRegistry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
@Configuration
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

    @Bean("edelweiss.log.executor.nop")
    // @ConditionalOnMissingBean({LogExecutor.class})
    public LogExecutor nopLogExecutor() {
        return new NopLogExecutor();
    }

    @Bean("edelweiss.log.result.post.processor.nop")
    // @ConditionalOnMissingBean({ResultPostProcessor.class})
    public ResultPostProcessor nopResultPostProcessor() {
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


    @Bean("edelweiss.log.result.post.processor.controller")
    public ResultPostProcessor controllerResultPostProcessor() {
        return new ControllerResultPostProcessor();
    }

    @Bean("edelweiss.log.function.json")
    public JsonParseFunction jsonParseFunction() {
        return new JsonParseFunction();
    }

    @Bean("edelweiss.log.function.date")
    public DateParseFunction dateParseFunction() {
        return new DateParseFunction();
    }

    @Bean("edelweiss.log.function.multipart")
    public MultipartParseFunction multipartParseFunction() {
        return new MultipartParseFunction();
    }
}
