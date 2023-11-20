package org.edelweiss.logging.registry;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
public class SpringBasedRegistry<T> extends ClassBasedRegistry<T> implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public T get(Class<? extends T> key) {
        T data = this.getContainer().get(key);
        if (data == null) {
            synchronized (this.lock) {
                data = this.getContainer().get(key);
                if (data == null) {
                    data = applicationContext.getBean(key);
                }
            }
        }
        return data;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
