// package com.shokaku.logging.util;
//
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// public class TransactionUtil {
//
//     public static final DataSourceTransactionManager TRANSACTION_MANAGER;
//
//     static {
//         TRANSACTION_MANAGER = ApplicationContextUtil.getSingleBean(DataSourceTransactionManager.class);
//     }
//
//     public static void doOperationInTX(Runnable runnable, int propagation) {
//         DefaultTransactionDefinition definition = new DefaultTransactionDefinition(propagation);
//         TransactionStatus transaction = TRANSACTION_MANAGER.getTransaction(definition);
//         log.info("事务创建成功");
//         try {
//             log.info("开始执行数据库操作");
//             runnable.run();
//             log.info("结束执行数据库操作");
//             TRANSACTION_MANAGER.commit(transaction);
//             log.info("执行成功 事务提交");
//         } catch (Throwable e) {
//             log.error("数据库执行异常 事务回滚", e);
//             TRANSACTION_MANAGER.rollback(transaction);
//             throw new BusinessException("数据库执行异常", e);
//         }
//     }
// }
