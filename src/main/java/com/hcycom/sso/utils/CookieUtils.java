package com.hcycom.sso.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: LiuGenshi
 * @Date:Created in 2018-05-25 13:34
 */
public class CookieUtils {
    /**
     *
     * @param response
     * @param cookieName cookie名称
     * @param cookieValue cookie值
     * @param expire 存活时间
     */
    public static void writeCookie(HttpServletResponse response, String cookieName,String cookieValue, int expire) {
        Cookie c = new Cookie(cookieName, cookieValue);
        c.setPath("/");
        c.setMaxAge(expire);
        response.addCookie(c);
    }
}
