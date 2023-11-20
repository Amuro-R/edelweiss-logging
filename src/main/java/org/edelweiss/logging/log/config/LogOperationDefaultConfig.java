package org.edelweiss.logging.log.config;

import org.edelweiss.logging.log.aspect.LogOperationAspect;
import org.edelweiss.logging.log.aspect.executor.LogOperationExecutor;
import org.edelweiss.logging.log.aspect.executor.NopLogOperationExecutor;
import org.edelweiss.logging.log.aspect.processor.NopResultPostProcessor;
import org.edelweiss.logging.log.aspect.processor.ResultPostProcessor;
import org.edelweiss.logging.pojo.log.LogOperationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

public class LogOperationDefaultConfig {

    @Bean
    @ConfigurationProperties(prefix = "mbi.log")
    public LogOperationProperties logOperationProperties() {
        return new LogOperationProperties();
    }


    @Bean
    public LogOperationAspect logOperationAspect() {
        return new LogOperationAspect();
    }

    @Bean
    @ConditionalOnMissingBean({LogOperationExecutor.class})
    public LogOperationExecutor defaultLogOperationExecutor() {
        return new NopLogOperationExecutor();
    }

    @Bean
    @ConditionalOnMissingBean({ResultPostProcessor.class})
    public ResultPostProcessor defaultResultPostProcessor() {
        return new NopResultPostProcessor();
    }

}
