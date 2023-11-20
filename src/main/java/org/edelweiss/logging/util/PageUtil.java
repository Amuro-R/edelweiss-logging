// package com.shokaku.logging.util;
//
//
// import java.util.List;
//
// /**
//  * @author jingyun
//  * @date 2022-03-16
//  */
// public class PageUtil {
//
//     public static <T> Page<T> getPageObject(Integer pageNum, Integer pageSize, Class<T> clazz) {
//         Page<T> page = null;
//         if (pageNum == null || pageSize == null) {
//             page = null;
//         } else if (pageNum < 0 || pageSize <= 0) {
//             page = null;
//         } else {
//             page = new Page<>(pageNum, pageSize);
//         }
//         return page;
//     }
//
//     public static <T, V> PageVO<T> getPageResult(Page<V> page, List<T> list) {
//         PageVO<T> result = new PageVO<>();
//         result.setData(list);
//         if (page != null) {
//             result.setCurrent(page.getCurrent());
//             result.setSize(page.getSize());
//             result.setTotal(page.getTotal());
//         }
//         return result;
//     }
// }
