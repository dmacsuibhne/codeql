package com.example.vulnapp.servlets;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-330: insecure randomness for a cookie. Sink: new Random().nextBytes(...) used as a token. */
public class CWE_330_InsecureRandomnessCookie extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Random r = new Random(); // BAD: Random is not cryptographically secure
        byte[] bytes = new byte[16];
        r.nextBytes(bytes);
        String cookieValue = Base64.getEncoder().encodeToString(bytes);
        Cookie cookie = new Cookie("name", cookieValue);
        response.addCookie(cookie);
        response.getWriter().print("token issued");
    }
}
