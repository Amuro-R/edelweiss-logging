package org.edelweiss.logging.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

/**
 * @author Amuro-R
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
                    ip = InetAddress.getLocalHost().getHostAddress();
                }
            }
            if (ip != null && ip.length() > 15) {
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
