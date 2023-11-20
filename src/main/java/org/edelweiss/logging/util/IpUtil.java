package org.edelweiss.logging.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

/**
 * @author fzw
 * @date 2023/11/6
 **/
@Slf4j
public class IpUtil {
    public static String getOriginIpFromHttpRequest(HttpServletRequest request) {
        try {
            String ip = request.getHeader("x-forwarded-for");
            if (ObjectUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ObjectUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ObjectUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if ("127.0.0.1".equals(ip)) {
                    // 根据网卡取本机配置的IP
                    ip = InetAddress.getLocalHost().getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ip != null && ip.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ip.indexOf(",") > 0) {
                    ip = ip.substring(0, ip.indexOf(","));
                }
            }
            return ObjectUtils.isEmpty(ip) ? "unknown" : ip;
        } catch (Exception e) {
            log.error("获取IP异常", e);
        }
        return "unknown";
    }
}
