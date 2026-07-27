package com.example.vulnapp.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CWE-113: HTTP Response Splitting.
 * Source: request parameter "name". Sink: response.addCookie(new Cookie("name", ...)).
 * Root cause: unvalidated user input placed into a response header.
 */
public class CWE_113_ResponseSplitting extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // BAD: setting a cookie with an unvalidated parameter
        Cookie cookie = new Cookie("name", request.getParameter("name"));
        response.addCookie(cookie);
        response.getWriter().print("cookie set");
    }
}

