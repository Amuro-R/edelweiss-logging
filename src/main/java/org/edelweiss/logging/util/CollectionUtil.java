package org.edelweiss.logging.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
public class CollectionUtil {


    public static <K, V> Map<K, V> of(K k1, V v1) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        return map;
    }
}
