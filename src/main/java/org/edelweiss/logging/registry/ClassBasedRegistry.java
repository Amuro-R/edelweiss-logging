package org.edelweiss.logging.registry;

import lombok.extern.slf4j.Slf4j;
import org.edelweiss.logging.exception.LoggingException;

/**
 * @author fzw
 * @date 2023/11/20
 **/
@Slf4j
public class ClassBasedRegistry<T> extends AbstractRegistry<Class<? extends T>, T> {

    protected final Object lock = new Object();

    @Override
    public T get(Class<? extends T> key) {
        T data = this.getContainer().get(key);
        if (data == null) {
            synchronized (lock) {
                data = this.getContainer().get(key);
                if (data == null) {
                    try {
                        data = key.newInstance();
                        this.register(key, data);
                    } catch (InstantiationException | IllegalAccessException e) {
                        log.error("创建实例失败", e);
                        throw new LoggingException("创建实例失败", e);
                    }
                }
            }
        }
        return data;
    }

    @Override
    public boolean register(Class<? extends T> key, T data) {
        this.getContainer().put(key, data);
        return true;
    }

    @Override
    public boolean unregister(Class<? extends T> key) {
        this.getContainer().remove(key);
        return true;
    }
}
