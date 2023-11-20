package org.edelweiss.logging.config;

import org.edelweiss.logging.interceptor.LogInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LogWebConfig implements WebMvcConfigurer {

    @Bean("edelweiss.log.interceptor")
    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.logInterceptor()).addPathPatterns("/**").order(10000);
    }
}
