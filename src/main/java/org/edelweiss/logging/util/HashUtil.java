package org.edelweiss.logging.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
public class HashUtil {
    private static final BCryptPasswordEncoder ENCODER;

    static {
        try {
            ENCODER = new BCryptPasswordEncoder(10, SecureRandom.getInstance("SHA1PRNG"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encode(String plainText) {
        return ENCODER.encode(plainText);
    }

    public static boolean matches(String plainText, String encodedText) {
        return ENCODER.matches(plainText, encodedText);
    }


}
