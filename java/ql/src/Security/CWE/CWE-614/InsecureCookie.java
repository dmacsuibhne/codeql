package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-614: cookie without the Secure flag. Sink: response.addCookie(cookie) with no setSecure(true). */
public class CWE_614_InsecureCookie extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cookie cookie = new Cookie("secret", "fakesecret");
        // BAD: 'secure' flag not set
        response.addCookie(cookie);

        Cookie goodCookie = new Cookie("secret", "fakesecret");
        // GOOD: set 'secure' flag
        goodCookie.setSecure(true);
        response.addCookie(goodCookie);

        response.getWriter().print("cookie set");
    }
}
