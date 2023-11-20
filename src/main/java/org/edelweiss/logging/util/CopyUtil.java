package org.edelweiss.logging.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import com.esotericsoftware.kryo.util.Pool;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author jingyun
 * @date 2022-03-17
 */
public class CopyUtil {

    private static final Pool<Kryo> KRYO_POOL;

    static {
        KRYO_POOL = new Pool<Kryo>(true, false, 8) {
            protected Kryo create() {
                Kryo kryo = new Kryo();
                kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
                kryo.setReferences(true);
                kryo.register(HashMap.class);
                return kryo;
            }
        };
    }

    public static <T extends Serializable> T deepCopy(T t) {
        Kryo kryo = KRYO_POOL.obtain();
        return kryo.copy(t);
    }

    public static <T extends Serializable> T shallowCopy(T t) {
        Kryo kryo = KRYO_POOL.obtain();
        return kryo.copyShallow(t);
    }
}
