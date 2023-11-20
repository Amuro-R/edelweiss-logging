package org.edelweiss.logging.util;

import org.edelweiss.logging.exception.LoggingException;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author jingyun
 * @date 2022-06-16
 */
public class RecursionLoopChecker<T> {

    private final Set<T> set;

    private RecursionLoopChecker() {
        this.set = new HashSet<>();
    }

    public static <V> RecursionLoopChecker<V> getInstance(Class<V> clazz) {
        return new RecursionLoopChecker<V>();
    }

    public boolean updateLoop(T t) {
        return set.add(t);
    }

    public boolean hasLoop(T t) {
        return this.hasLoop(t, "递归异常");
    }

    public boolean hasLoop(T t, String message) {
        if (set.contains(t)) {
            throw new LoggingException(message);
        }
        return false;
    }

    public boolean hasLoop(Collection<T> c) {
        return this.hasLoop(c, "递归异常");
    }

    public boolean hasLoop(Collection<T> c, String message) {
        if (CollectionUtils.containsAny(set, c)) {
            throw new LoggingException(message);
        }
        return false;
    }

    public boolean checkAndUpdate(T t) {
        this.hasLoop(t);
        return this.updateLoop(t);
    }

    public boolean checkAndUpdate(T t, String message) {
        this.hasLoop(t, message);
        return this.updateLoop(t);
    }

    public void clear() {
        set.clear();
    }

}
