package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-1004: sensitive cookie without HttpOnly. Source: "jwt_token". Sink: response.addCookie(cookie). */
public class CWE_1004_SensitiveCookieNotHttpOnly extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String jwt_token = request.getParameter("jwt_token");
        Cookie jwtCookie = new Cookie("jwt_token", jwt_token);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(3600 * 24 * 7);
        // BAD - HttpOnly flag is not set
        response.addCookie(jwtCookie);
        response.getWriter().print("cookie set");
    }
}
