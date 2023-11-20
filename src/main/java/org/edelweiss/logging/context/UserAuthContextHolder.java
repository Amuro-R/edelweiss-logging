package org.edelweiss.logging.context;

/**
 * @author jingyun
 * @date 2022-08-29
 */
public class UserAuthContextHolder {

    // private static final ThreadLocal<UserAuthContext> USER_AUTH_CONTEXT;
    //
    // static {
    //     USER_AUTH_CONTEXT = new ThreadLocal<>();
    // }
    //
    // public static void setUserAuthContext(UserAuthContext userAuthContext) {
    //     USER_AUTH_CONTEXT.set(userAuthContext);
    // }
    //
    // public static void removeUserAuthContext() {
    //     USER_AUTH_CONTEXT.remove();
    // }
    //
    // public static UserAuthContext getUserAuthContext() {
    //     UserAuthContext authContext = USER_AUTH_CONTEXT.get();
    //     if (authContext == null) {
    //         throw new LoggingException(BusinessErrorCodeEnum.USER_LOGIN_AUTH_ERROR);
    //     }
    //     return authContext;
    // }

    // public static Long getEmployeeId() {
    // return UserAuthContextHolder.getUserAuthContext().getEmployeeId();
    // }

    // public static Long getUserId() {
    // return UserAuthContextHolder.getUserAuthContext().getUserId();
    // }

    // public static boolean isAdmin() {
    // return UserAuthContextHolder.getUserAuthContext().isAdmin();
    // }

    public static boolean isLogin() {
        // return USER_AUTH_CONTEXT.get() != null;
        return false;
    }

    public static String getUserName() {
        return null;
    }

    public static String getPhone() {
        return null;
    }
}
