// package com.shokaku.logging.util;
//
// import com.fasterxml.jackson.annotation.JsonAutoDetect;
// import com.fasterxml.jackson.annotation.PropertyAccessor;
// import com.fasterxml.jackson.core.type.TypeReference;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import lombok.SneakyThrows;
//
// import java.util.*;
// import java.util.concurrent.TimeUnit;
// import java.util.stream.Collectors;
//
// /**
//  * @author jingyun
//  * @date 2022-03-02
//  */
// public class RedisUtil {
//
//     private static final StringRedisTemplate STRING_REDIS_TEMPLATE;
//     private static final ValueOperations<String, String> OPS_FOR_VALUE;
//     private static final ListOperations<String, String> OPS_FOR_LIST;
//     private static final SetOperations<String, String> OPS_FOR_SET;
//     private static final HashOperations<String, Object, Object> OPS_FOR_HASH;
//
//     //    redis special ObjectMapper
//     private static final ObjectMapper OBJECT_MAPPER;
//
//     static {
//         STRING_REDIS_TEMPLATE = ApplicationContextUtil.getSingleBean(StringRedisTemplate.class);
//         OBJECT_MAPPER = new ObjectMapper();
//         OBJECT_MAPPER.setTimeZone(TimeZone.getDefault());
//         OBJECT_MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
//         OPS_FOR_VALUE = STRING_REDIS_TEMPLATE.opsForValue();
//         OPS_FOR_LIST = STRING_REDIS_TEMPLATE.opsForList();
//         OPS_FOR_SET = STRING_REDIS_TEMPLATE.opsForSet();
//         OPS_FOR_HASH = STRING_REDIS_TEMPLATE.opsForHash();
//     }
//
//     public static <T> T get(String key, Class<T> clazz) {
//         String value = OPS_FOR_VALUE.get(key);
//         return value == null ? null : RedisUtil.fromJsonString(value, clazz);
//     }
//
//     public static <T> T get(String key, TypeReference<T> typeReference) {
//         String value = OPS_FOR_VALUE.get(key);
//         return value == null ? null : RedisUtil.fromJsonString(value, typeReference);
//     }
//
//     public static <T> List<T> batchGet(Collection<String> keys, Class<T> clazz) {
//         List<String> values = OPS_FOR_VALUE.multiGet(keys);
//         if (values != null) {
//             return values.stream().filter(Objects::nonNull).map(item -> RedisUtil.fromJsonString(item, clazz)).collect(Collectors.toList());
//         }
//         return new ArrayList<>();
//     }
//
//     public static long getExpire(String key) {
//         Long expire = STRING_REDIS_TEMPLATE.getExpire(key);
//         return expire == null ? -2L : expire;
//     }
//
//     public static void set(String key, Object value) {
//         String json = RedisUtil.toJsonString(value);
//         OPS_FOR_VALUE.set(key, json);
//     }
//
//     public static void setIfAbsent(String key, Object value, long second) {
//         String json = RedisUtil.toJsonString(value);
//         OPS_FOR_VALUE.setIfAbsent(key, json, second, TimeUnit.SECONDS);
//     }
//
//     public static void set(String key, Object value, long second) {
//         String json = RedisUtil.toJsonString(value);
//         OPS_FOR_VALUE.set(key, json, second, TimeUnit.SECONDS);
//     }
//
//     public static boolean expire(String key, long second) {
//         return Boolean.TRUE.equals(STRING_REDIS_TEMPLATE.expire(key, second, TimeUnit.SECONDS));
//     }
//
//     public static boolean batchExpire(Collection<String> keys, long second) {
//         boolean expire = true;
//         for (String key : keys) {
//             expire = expire && Boolean.TRUE.equals(STRING_REDIS_TEMPLATE.expire(key, second, TimeUnit.SECONDS));
//         }
//         return expire;
//     }
//
//     public static boolean delete(String key) {
//         return Boolean.TRUE.equals(STRING_REDIS_TEMPLATE.delete(key));
//     }
//
//     public static long batchDelete(Collection<String> keys) {
//         Long delete = STRING_REDIS_TEMPLATE.delete(keys);
//         return delete == null ? -1 : delete;
//     }
//
//     public static boolean hasKey(String key) {
//         return Boolean.TRUE.equals(STRING_REDIS_TEMPLATE.hasKey(key));
//     }
//
//     public static <T> T script(RedisScript<T> script, List<String> keys, Object... args) {
//         return STRING_REDIS_TEMPLATE.execute(script, keys, args);
//     }
//
//     @SneakyThrows
//     public static String toJsonString(Object data) {
//         return OBJECT_MAPPER.writeValueAsString(data);
//     }
//
//     @SneakyThrows
//     public static <T> T fromJsonString(String json, Class<T> clazz) {
//         return OBJECT_MAPPER.readValue(json, clazz);
//     }
//
//     @SneakyThrows
//     public static <T> T fromJsonString(String json, TypeReference<T> typeReference) {
//         return OBJECT_MAPPER.readValue(json, typeReference);
//     }
// }
