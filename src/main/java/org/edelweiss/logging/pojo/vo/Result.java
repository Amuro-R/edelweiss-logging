package org.edelweiss.logging.pojo.vo;

import java.io.Serializable;

/**
 * @author jingyun
 * @date 2022-05-23
 */
public interface Result extends Serializable {
    boolean success();

    // private static final String SUCCESS_CODE = "ECO_SUS_000";
    //
    // private static final String FAIL_CODE = "ECO_EXP_000";
    //
    // private static final String SUCCESS_MESSAGE = "操作成功";
    //
    // private static final String FAIL_MESSAGE = "操作失败";
    //
    // private String code;
    //
    // private String message;
    //
    // private boolean success;
    //
    // private T data;

    // public Result(String code, String message, boolean success, T data) {
    //     this.code = code;
    //     this.message = message;
    //     this.success = success;
    //     this.data = data;
    // }

    // public static <T> Result<T> success() {
    //     return new Result<>(SUCCESS_CODE, SUCCESS_MESSAGE, true, null);
    // }
    //
    // public static <T> Result<T> success(T data) {
    //     return new Result<>(SUCCESS_CODE, SUCCESS_MESSAGE, true, data);
    // }
    //
    // public static <T> Result<T> fail(String message) {
    //     return new Result<>(FAIL_CODE, message, false, null);
    // }
    //
    // public static <T> Result<T> fail(String code, String message) {
    //     return new Result<>(code, message, false, null);
    // }
    //
    // public static <T> Result<T> fail(String code, String message, T data) {
    //     return new Result<>(code, message, false, data);
    // }
    //
    // public static <T> Result<T> fail() {
    //     return new Result<>(FAIL_CODE, FAIL_MESSAGE, false, null);
    // }
}
