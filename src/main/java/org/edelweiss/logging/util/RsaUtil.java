package org.edelweiss.logging.util;

import org.edelweiss.logging.exception.LoggingException;
import lombok.Getter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 2048位 RSA 加密
 *
 * @author jingyun
 * @date 2022-03-01
 */
public class RsaUtil {

    private final RsaContext rsaContext;

    public RsaUtil(RsaContext rsaContext) {
        this.rsaContext = rsaContext;
    }

    public String encode(String plainText) {
        try {
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, rsaContext.publicKey);
            return Base64Util.encodeString(encryptCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new LoggingException("rsa加密失败", e);
        }
    }


    public byte[] decode(String cipherText) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, rsaContext.privateKey);
            byte[] bytes = Base64Util.decode(cipherText);
            int length = bytes.length;
            int divide = length / 256;
            int mod = length % 256;
            if (mod > 0) {
                divide = divide + 1;
            }
            int start = 0;
            int len = 0;
            byte[] temp = new byte[0];
            for (int i = 0; i < divide; i++) {
                start = i * 256;
                len = Math.min(256, length - start);
                temp = decryptCipher.doFinal(bytes, start, len);
                outputStream.write(temp);
            }
            return outputStream.toByteArray();

        } catch (IllegalBlockSizeException | BadPaddingException | IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new LoggingException("rsa解密失败", e);
        }
    }

    public String decodeString(String cipherText) {
        byte[] bytes = this.decode(cipherText);
        return new String(bytes);
    }


    public static RsaUtil getRsaUtil(RsaContextEnum rsaContextEnum) {
        return RsaFactory.getRsaUtil(rsaContextEnum);
    }

    public static class RsaFactory {
        private static final ConcurrentMap<RsaContextEnum, RsaContext> CONTEXT_CACHE = new ConcurrentHashMap<>();
        private static final ConcurrentMap<RsaContextEnum, RsaUtil> UTIL_CACHE = new ConcurrentHashMap<>();

        public static RsaContext createRsaContext(String publicKey, String privateKey) {
            return new RsaContext(publicKey, privateKey);
        }

        public static RsaUtil createRsaContext(RsaContextEnum rsaContextEnum, String publicKey, String privateKey) {
            RsaContext rsaContext = RsaFactory.createRsaContext(publicKey, privateKey);
            RsaUtil rsaUtil = new RsaUtil(rsaContext);
            CONTEXT_CACHE.put(rsaContextEnum, rsaContext);
            UTIL_CACHE.put(rsaContextEnum, rsaUtil);
            return rsaUtil;
        }

        public static RsaContext getRsaContext(RsaContextEnum rsaContextEnum) {
            return CONTEXT_CACHE.get(rsaContextEnum);
        }


        public static RsaUtil getRsaUtil(RsaContextEnum rsaContextEnum) {
            return UTIL_CACHE.get(rsaContextEnum);
        }

        public static void removeRsa(RsaContextEnum rsaContextEnum) {
            CONTEXT_CACHE.remove(rsaContextEnum);
            UTIL_CACHE.remove(rsaContextEnum);
        }
    }


    @Getter
    public static class RsaContext {
        private final PublicKey publicKey;
        private final PrivateKey privateKey;

        public RsaContext(String publicKey, String privateKey) {
            try {
                X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64Util.decode(publicKey));
                PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64Util.decode(privateKey));
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                this.publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
                this.privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new LoggingException("RSA context 创建失败", e);
            }
        }
    }

    public static enum RsaContextEnum {
        Login_PWD(1, "登录密码加解密"),
        ;
        public final Integer code;
        public final String codeDesc;

        RsaContextEnum(Integer code, String codeDesc) {
            this.code = code;
            this.codeDesc = codeDesc;
        }
    }

}
