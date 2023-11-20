package org.edelweiss.logging.util;


import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

import java.util.UUID;

/**
 * uuid version 1
 *
 * @author jingyun
 * @date 2022-03-02
 */
public class UUIDUtil {

    private static final TimeBasedGenerator timeBasedGenerator;

    static {
        timeBasedGenerator = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
    }

    public static String genStrongUUID() {
        UUID uuid = timeBasedGenerator.generate();
        return uuid.toString();
    }

    public static String genStrongSimpleUUID() {
        return UUIDUtil.genStrongUUID().replace("-", "");
    }
}
