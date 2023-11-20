package org.edelweiss.logging.context;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
public class LogContext {

    private static final ThreadLocal<Deque<Map<String, Object>>> LOG_ATTRIBUTE_HOLDER = new ThreadLocal<>();

    public static void initLogContext() {
        ArrayDeque<Map<String, Object>> deque = new ArrayDeque<>();
        deque.offerFirst(new HashMap<>());
        LOG_ATTRIBUTE_HOLDER.set(deque);
    }

    public static void removeLogContext() {
        LOG_ATTRIBUTE_HOLDER.remove();
    }

    public static void createCurrentStackFrame() {
        LOG_ATTRIBUTE_HOLDER.get().offerFirst(new ConcurrentHashMap<>());
    }

    @SuppressWarnings("ConstantConditions")
    public static Object getLogAttribute(String key) {
        return LOG_ATTRIBUTE_HOLDER.get().peekFirst().get(key);
    }

    @SuppressWarnings("ConstantConditions")
    public static Object getLogAttributeCommon(String key) {
        return LOG_ATTRIBUTE_HOLDER.get().peekLast().get(key);
    }

    @SuppressWarnings("ConstantConditions")
    public static void setLogAttribute(String key, Object val) {
        LOG_ATTRIBUTE_HOLDER.get().peekFirst().put(key, val);
    }

    @SuppressWarnings("ConstantConditions")
    public static void setLogAttributeCommon(String key, Object val) {
        LOG_ATTRIBUTE_HOLDER.get().peekLast().put(key, val);
    }

    @SuppressWarnings("ConstantConditions")
    public static void removeLogAttribute(String key) {
        LOG_ATTRIBUTE_HOLDER.get().peekFirst().remove(key);
    }

    public static Map<String, Object> getLogAttributes() {
        return LOG_ATTRIBUTE_HOLDER.get().peekFirst();
    }

    public static Map<String, Object> getLogAttributesCommon() {
        return LOG_ATTRIBUTE_HOLDER.get().peekLast();
    }

    public static void removeLogAttributes() {
        LOG_ATTRIBUTE_HOLDER.get().pollFirst();
    }

}
