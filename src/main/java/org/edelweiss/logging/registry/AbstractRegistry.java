package org.edelweiss.logging.registry;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fzw
 * @date 2023/11/20
 **/
@Data
public abstract class AbstractRegistry<K, V> implements Registry<K, V> {

    private final Map<K, V> container = new HashMap<>();

    public abstract V get(K key);


}
