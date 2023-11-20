// package com.shokaku.logging.util;
//
// import lombok.extern.slf4j.Slf4j;
// import org.apache.commons.codec.digest.DigestUtils;
// import org.springframework.util.ObjectUtils;
//
// import java.io.UnsupportedEncodingException;
// import java.lang.reflect.Field;
// import java.net.URLEncoder;
// import java.util.TreeMap;
// import java.util.stream.Collectors;
//
// /**
//  * @author jingyun
//  * @date 2022-09-21
//  */
// @Slf4j
// public class QyUtil {
//
//     private static final Field[] DECLARED_FIELDS;
//
//     static {
//         DECLARED_FIELDS = BlockChainDTO.class.getDeclaredFields();
//         for (Field field : DECLARED_FIELDS) {
//             field.setAccessible(true);
//         }
//     }
//
//     public static String getSign(BlockChainDTO dto, String appSecret) {
//         log.info("获取签名 {} {}", dto, appSecret);
//         // 按照字段名 自然排序
//         TreeMap<String, String> params = new TreeMap<>();
//         try {
//             for (Field field : DECLARED_FIELDS) {
//                 String fieldName = field.getName();
//                 Object obj = field.get(dto);
//                 log.info("签名字段 name:{} value:{}", fieldName, obj);
//                 if (fieldName.equals("sign")) {
//                     continue;
//                 }
//                 if (ObjectUtils.isEmpty(obj)) {
//                     throw new RuntimeException("签名字段为空: " + fieldName);
//                 }
//                 String fieldValue = obj.toString();
//                 try {
//                     params.put(fieldName, encodedFix(URLEncoder.encode(fieldValue, "UTF-8")));
//                 } catch (UnsupportedEncodingException e) {
//                     throw new RuntimeException(e);
//                 }
//             }
//         } catch (IllegalAccessException e) {
//             throw new RuntimeException(e);
//         }
//         log.info("参数map:{}", params);
//         String query = params.entrySet().stream().map(item -> item.getKey() + "=" + item.getValue()).collect(Collectors.joining("&"));
//         log.info("查询字符串:{}", query);
//         return DigestUtils.md5Hex(query + appSecret);
//     }
//
//     /**
//      * 固定编码格式
//      *
//      * @param encoded 原字符串
//      * @return 编码字符串
//      */
//     private static String encodedFix(String encoded) {
//         // required
//         encoded = encoded.replace("+", "%20");
//         encoded = encoded.replace("*", "%2A");
//         encoded = encoded.replace("%7E", "~");
//         // optional
//         encoded = encoded.replace("!", "%21");
//         encoded = encoded.replace("(", "%28");
//         encoded = encoded.replace(")", "%29");
//         encoded = encoded.replace("'", "%27");
//         return encoded;
//     }
// }
