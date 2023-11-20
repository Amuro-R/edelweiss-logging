package org.edelweiss.logging.util;


import org.edelweiss.logging.exception.LoggingException;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
@Slf4j
@Getter
public class AesUtil {
    private final AesContext aesContext;

    private AesUtil(AesContext aesContext) {
        this.aesContext = aesContext;
    }

    public String encrypt(String plainText) {
        try {
            Cipher encryptCipher = Cipher.getInstance(aesContext.getCipherSpec());
            encryptCipher.init(Cipher.ENCRYPT_MODE, aesContext.getAesKeySpec(), aesContext.getIvParameterSpec());
            return Base64Util.encodeString(encryptCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException |
                 InvalidKeyException e) {
            throw new LoggingException("aes加密失败", e);
        }
    }

    public String decrypt(String cipherText) {
        try {
            Cipher decryptCipher = Cipher.getInstance(aesContext.getCipherSpec());
            decryptCipher.init(Cipher.DECRYPT_MODE, aesContext.getAesKeySpec(), aesContext.getIvParameterSpec());
            return new String(decryptCipher.doFinal(Base64Util.decode(cipherText)), StandardCharsets.UTF_8);
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException e) {
            throw new LoggingException("aes解密失败", e);
        }
    }

    public static AesUtil getAesUtil(AesContextEnum aesContextEnum) {
        return AesFactory.UTIL_CACHE.get(aesContextEnum);
    }

    public static class AesFactory {
        private static final ConcurrentMap<AesContextEnum, AesContext> CONTEXT_CACHE = new ConcurrentHashMap<>();
        private static final ConcurrentMap<AesContextEnum, AesUtil> UTIL_CACHE = new ConcurrentHashMap<>();

        // 只用于生成key
        @SneakyThrows
        public static void generateKeyInfo(String algorithm, String seed, int keyLen, String cipherSpec) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance(algorithm);
            secureRandom.setSeed(seed.getBytes(StandardCharsets.UTF_8));
            keyGenerator.init(keyLen, secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();
            String secret = Base64Util.encodeString(secretKey.getEncoded());
            Cipher encryptCipher = Cipher.getInstance(cipherSpec);
            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            String iv = Base64Util.encodeString(encryptCipher.getIV());
            log.info("secret: {}", secret);
            log.info("iv: {}", iv);
        }

        public static AesContext createAesContext(String secret, String iv, String cipherSpec) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(Base64Util.decode(secret), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(Base64Util.decode(iv));
            return new AesContext(secretKeySpec, ivParameterSpec, cipherSpec);
        }

        public static AesUtil createAesContext(AesContextEnum aesContextEnum, String secret, String iv, String cipherSpec) {
            AesContext aesContext = AesFactory.createAesContext(secret, iv, cipherSpec);
            AesUtil aesUtil = new AesUtil(aesContext);
            CONTEXT_CACHE.put(aesContextEnum, aesContext);
            UTIL_CACHE.put(aesContextEnum, aesUtil);
            return aesUtil;
        }

        public static void removeAes(AesContextEnum aesContextEnum) {
            CONTEXT_CACHE.remove(aesContextEnum);
            UTIL_CACHE.remove(aesContextEnum);
        }

        public static AesContext getAesContext(AesContextEnum aesContextEnum) {
            return CONTEXT_CACHE.get(aesContextEnum);
        }

        public static AesUtil getAesUtil(AesContextEnum aesContextEnum) {
            return UTIL_CACHE.get(aesContextEnum);
        }


    }

    @Getter
    public static class AesContext {
        private final SecretKeySpec aesKeySpec;
        private final IvParameterSpec ivParameterSpec;
        private final String encodedSecretKey;
        private final String encodedIv;
        private final String cipherSpec;

        public AesContext(SecretKeySpec aesKeySpec, IvParameterSpec ivParameterSpec, String cipherSpec) {
            this.aesKeySpec = aesKeySpec;
            this.ivParameterSpec = ivParameterSpec;
            this.cipherSpec = cipherSpec;
            this.encodedSecretKey = Base64Util.encodeString(this.aesKeySpec.getEncoded());
            this.encodedIv = Base64Util.encodeString(this.ivParameterSpec.getIV());
        }
    }

    public static enum AesContextEnum {
        QTZ_TOKEN(1, "全诊通同步token加密"),
        LOGIN_TOKEN(2, "登录token加密"),
        ;
        public final Integer code;
        public final String codeDesc;

        AesContextEnum(Integer code, String codeDesc) {
            this.code = code;
            this.codeDesc = codeDesc;
        }
    }
}
