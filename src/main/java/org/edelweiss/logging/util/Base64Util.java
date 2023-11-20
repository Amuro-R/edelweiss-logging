package org.edelweiss.logging.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author jingyun
 * @date 2022-03-01
 */
public class Base64Util {

    private static final Base64.Decoder DECODER;
    private static final Base64.Encoder ENCODER;

    static {
        ENCODER = Base64.getEncoder();
        DECODER = Base64.getDecoder();
    }

    public static byte[] encode(byte[] origin) {
        return ENCODER.encode(origin);
    }

    public static String encodeString(byte[] origin) {
        return new String(ENCODER.encode(origin), StandardCharsets.UTF_8);
    }

    public static byte[] decode(String origin) {
        return DECODER.decode(origin);
    }

    public static byte[] decode(byte[] origin) {
        return DECODER.decode(origin);
    }

}
