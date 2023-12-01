package org.edelweiss.logging.context;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
public interface UserAuthService {

    /**
     * @return 是否登录
     */
    boolean isLogin();

    /**
     * @return 租户信息
     */
    String getTenant();

    /**
     * @return 用户信息
     */
    String getUser();


}
