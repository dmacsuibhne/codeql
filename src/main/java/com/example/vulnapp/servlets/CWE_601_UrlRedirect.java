package com.example.vulnapp.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CWE-601: Open Redirect.
 * Source: request parameter "target". Sink: response.sendRedirect(...).
 * Root cause: user input incorporated into a redirect without validation.
 */
public class CWE_601_UrlRedirect extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // BAD: a request parameter is incorporated without validation into a URL redirect
        response.sendRedirect(request.getParameter("target"));
    }
}

