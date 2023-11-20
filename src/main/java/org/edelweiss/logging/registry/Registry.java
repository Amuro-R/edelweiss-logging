package org.edelweiss.logging.registry;

/**
 * @author fzw
 * @date 2023/11/20
 **/
public interface Registry<K, V> {

    boolean register(K key, V data);

    boolean unregister(K key);


}
