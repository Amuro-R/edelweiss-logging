// package com.shokaku.logging.util;
//
//
// import java.util.concurrent.TimeUnit;
//
// /**
//  * @author jingyun
//  * @date 2022-03-02
//  */
// public class RedissonUtil {
//
//     private static final RedissonClient REDISSON_CLIENT;
//
//     static {
//         REDISSON_CLIENT = ApplicationContextUtil.getSingleBean(RedissonClient.class);
//     }
//
//     private static RLock getLock(String key) {
//         return REDISSON_CLIENT.getLock("mbi_r_lock:" + key);
//     }
//
//     public static RLock rLock(String key, long wait, long lease) {
//         RLock lock = RedissonUtil.getLock(key);
//         if (lock == null) {
//             throw new RuntimeException("获取锁失败, key: " + key);
//         }
//         boolean hasLock = false;
//         try {
//             hasLock = lock.tryLock(wait, lease, TimeUnit.SECONDS);
//         } catch (InterruptedException e) {
//             throw new RuntimeException("加锁中断异常, key: " + key, e);
//         }
//         if (hasLock) {
//             return lock;
//         } else {
//             throw new RuntimeException("加锁失败, key: " + key);
//         }
//     }
//
//     public static void unLock(RLock rLock) {
//         String keyName = null;
//         try {
//             if (rLock != null && rLock.isLocked()) {
//                 keyName = rLock.getName();
//                 rLock.unlock();
//             }
//         } catch (Exception e) {
//             throw new RuntimeException("解锁失败, key: " + keyName, e);
//         }
//     }
//
//
//     // public static void batchUnLock(Collection<RLock> rLocks, boolean reverse) {
//     //     List<Exception> exceptions = new ArrayList<>();
//     //     if (rLocks == null) {
//     //         rLocks = new ArrayList<>();
//     //     }
//     //     if (reverse) {
//     //         List<RLock> rLockList = null;
//     //         if (!(rLocks instanceof List)) {
//     //             rLockList = new ArrayList<>(rLocks);
//     //         } else {
//     //             rLockList = (List<RLock>) rLocks;
//     //         }
//     //         Collections.reverse(rLockList);
//     //         rLocks = rLockList;
//     //     }
//     //     for (RLock rLock : rLocks) {
//     //         try {
//     //             RedissonUtil.unLock(rLock);
//     //         } catch (Exception e) {
//     //             exceptions.add(e);
//     //         }
//     //     }
//     //     if (!exceptions.isEmpty()) {
//     //         throw new BusinessException(BusinessErrorCodeEnum.BIZ_EXP, exceptions);
//     //     }
//     // }
//
//
//     // private static RReadWriteLock getReadWriteLock(String key) {
//     //     return REDISSON_CLIENT.getReadWriteLock(RedissonConstant.READ_WRITE_LOCK_LOCK_PREFIX + key);
//     // }
//
//     // public static RLock readLock(String key, long wait, long lease) {
//     //     return RedissonUtil.readWriteLock(false, key, wait, lease);
//     // }
//
//     // public static RLock writeLock(String key, long wait, long lease) {
//     //     return RedissonUtil.readWriteLock(true, key, wait, lease);
//     // }
//
//     // private static RLock readWriteLock(boolean isWrite, String key, long wait, long lease) {
//     //     RReadWriteLock readWriteLock = RedissonUtil.getReadWriteLock(key);
//     //     if (readWriteLock == null) {
//     //         throw new RuntimeException("获取" + (isWrite ? "写" : "读") + "锁失败, key: " + key);
//     //     }
//     //     RLock rLock = null;
//     //     if (isWrite) {
//     //         rLock = readWriteLock.writeLock();
//     //     } else {
//     //         rLock = readWriteLock.readLock();
//     //     }
//     //     boolean hasLock = false;
//     //     try {
//     //         hasLock = rLock.tryLock(wait, lease, TimeUnit.SECONDS);
//     //     } catch (InterruptedException e) {
//     //         throw new RuntimeException("加" + (isWrite ? "写" : "读") + "锁中断异常, key: " + key, e);
//     //     }
//     //     if (hasLock) {
//     //         return rLock;
//     //     } else {
//     //         throw new RuntimeException("加" + (isWrite ? "写" : "读") + "锁失败, key: " + key);
//     //     }
//     //
//     // }
//
//     // public static RLock lockByLockType(RedissonLockTypeEnum lockType, String key, long wait, long lease) {
//     //     switch (lockType) {
//     //         case R_LOCK: {
//     //             return RedissonUtil.rLock(key, wait, lease);
//     //         }
//     //         case READ_LOCK: {
//     //             return RedissonUtil.readLock(key, wait, lease);
//     //         }
//     //         case WRITE_LOCK: {
//     //             return RedissonUtil.writeLock(key, wait, lease);
//     //         }
//     //         default: {
//     //             throw new RuntimeException("锁类型不符");
//     //         }
//     //     }
//     // }
// }
