package org.edelweiss.logging.config;

import org.edelweiss.logging.aspect.processor.ControllerResultPostProcessor;
import org.edelweiss.logging.aspect.processor.ResultPostProcessor;
import org.edelweiss.logging.el.impl.DateParseFunction;
import org.edelweiss.logging.el.impl.JsonParseFunction;
import org.edelweiss.logging.el.impl.MultipartParseFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class LogConfig {

    public static final ThreadPoolExecutor LOG_THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors() * 2, 64, 30L, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(1000), new ThreadPoolExecutor.DiscardPolicy());

    //    @Bean
//    public LogOperationExecutor esLogOperationExecutor(LogOperationProperties logOperationProperties) {
//        return new ElasticSearchLogOperationExecutor(logOperationProperties.getEs());
//    }

    @Bean
    public ResultPostProcessor controllerResultPostProcessor() {
        return new ControllerResultPostProcessor();
    }

    @Bean
    public JsonParseFunction jsonParseFunction() {
        return new JsonParseFunction();
    }

    @Bean
    public DateParseFunction dateParseFunction() {
        return new DateParseFunction();
    }

    @Bean
    public MultipartParseFunction multipartParseFunction() {
        return new MultipartParseFunction();
    }
}
