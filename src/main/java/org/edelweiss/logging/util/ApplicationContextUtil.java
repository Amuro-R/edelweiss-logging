package org.edelweiss.logging.util;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static <T> T getSingleBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getSingleBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;
    }
}
