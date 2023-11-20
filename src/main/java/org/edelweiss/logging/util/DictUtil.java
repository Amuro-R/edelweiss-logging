// package com.shokaku.logging.util;
//
// import com.google.common.cache.CacheBuilder;
// import com.google.common.cache.CacheLoader;
// import org.springframework.util.ObjectUtils;
// import org.springframework.util.StringUtils;
//
// import java.time.Duration;
//
// /**
//  * @author jingyun
//  * @date 2022-08-29
//  */
// public class DictUtil {
//     private static final LoadingCache<String, DictPO> CACHE;
//     private static final CacheLoader<String, DictPO> CACHE_LOADER;
//     private static final CacheSaver CACHE_SAVER;
//
//
//     static {
//         // noinspection unchecked
//         CACHE_LOADER = ApplicationContextUtil.getSingleBean("dictCacheLoader", CacheLoader.class);
//         CACHE = CacheBuilder.newBuilder().initialCapacity(1024).concurrencyLevel(Runtime.getRuntime().availableProcessors() + 1).expireAfterAccess(Duration.ofHours(1)).build(CACHE_LOADER);
//         CACHE_SAVER = ApplicationContextUtil.getSingleBean("dictCacheSaver", CacheSaver.class);
//     }
//
//     public static DictPO getWithCache(String code, String errMsg) {
//         try {
//             return CACHE.get(code);
//         } catch (Exception e) {
//             throw new BusinessException(StringUtils.hasLength(errMsg) ? errMsg + " " + code : e.getMessage(), BusinessErrorCodeEnum.CACHE_LOAD_EXP.errorCode, e);
//         }
//     }
//
//     public static DictPO getWithCache(String code) {
//         try {
//             return CACHE.get(code);
//         } catch (Exception e) {
//             try {
//                 return CACHE.get("unknown");
//             } catch (Exception ex) {
//                 throw new RuntimeException(ex);
//             }
//         }
//     }
//
//     public static DictPO getWithOutCache(String code, String errMsg) {
//         try {
//             return CACHE_LOADER.load(code);
//         } catch (Exception e) {
//             throw new BusinessException(StringUtils.hasLength(errMsg) ? errMsg + " " + code : e.getMessage(), BusinessErrorCodeEnum.CACHE_LOAD_EXP.errorCode, e);
//         }
//     }
//
//     public static String dictCodeConvert(String code, DictCodePrefixEnum prefixEnum) {
//         if (ObjectUtils.isEmpty(code)) {
//             return "unknown";
//         }
//         if (!"unknown".equals(code) && !StringUtils.startsWithIgnoreCase(code, prefixEnum.codeDesc)) {
//             return prefixEnum.codeDesc + code;
//         }
//         return code;
//     }
//
//     public static DictPO getOrSaveWithCache(String code, DictPO dictPO) {
//         try {
//             return DictUtil.getWithCache(code, null);
//         } catch (Exception e) {
//             if (e instanceof LightBusinessException && BusinessErrorCodeEnum.CACHE_LOAD_EXP.errorCode.equals(((LightBusinessException) e).getErrorCode())) {
//                 CACHE_SAVER.save(dictPO);
//             }
//         }
//         return DictUtil.getWithOutCache(code, null);
//     }
//
//     public static DictPO getOrSaveWithOutCache(String code, DictPO dictPO) {
//         try {
//             return DictUtil.getWithOutCache(code, null);
//         } catch (Exception e) {
//             if (e instanceof LightBusinessException && BusinessErrorCodeEnum.CACHE_LOAD_EXP.errorCode.equals(((LightBusinessException) e).getErrorCode())) {
//                 CACHE_SAVER.save(dictPO);
//             }
//         }
//         return DictUtil.getWithOutCache(code, null);
//     }
//
//
//     public static class CacheSaver {
//         private final Consumer<DictPO> save;
//
//         public CacheSaver(Consumer<DictPO> save) {
//             this.save = save;
//         }
//
//         public void save(DictPO dictPO) {
//             save.accept(dictPO);
//         }
//     }
// }
