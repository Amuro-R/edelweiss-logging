package org.edelweiss.logging.config;

import org.edelweiss.logging.interceptor.LogInterceptor;
import org.edelweiss.logging.properties.LogProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Optional;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
@Configuration
public class LogWebConfig implements WebMvcConfigurer {

    @Autowired
    private LogProperties logProperties;

    @Bean("edelweiss.log.interceptor")
    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] includePatterns = Optional.ofNullable(logProperties.getInterceptor()).map(LogProperties.LogInterceptorProp::getIncludePatterns).orElse(null);
        String[] excludePatterns = Optional.ofNullable(logProperties.getInterceptor()).map(LogProperties.LogInterceptorProp::getExcludePatterns).orElse(null);
        Integer order = Optional.ofNullable(logProperties.getInterceptor()).map(LogProperties.LogInterceptorProp::getOrder).orElse(10000);
        InterceptorRegistration registration = null;
        if (!ObjectUtils.isEmpty(includePatterns) && !ObjectUtils.isEmpty(excludePatterns)) {
            registration = registry.addInterceptor(this.logInterceptor()).order(order);
        }
        if (!ObjectUtils.isEmpty(includePatterns) && registration != null) {
            registration.addPathPatterns(includePatterns);
        }
        if (!ObjectUtils.isEmpty(excludePatterns) && registration != null) {
            registration.excludePathPatterns(excludePatterns);
        }
    }
}
