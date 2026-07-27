package com.example.vulnapp.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CWE-730: Regular Expression Injection (ReDoS).
 * Source: request parameters "regex" and "input". Sink: input.matches(regex).
 * Root cause: user input used to construct a regular expression.
 */
public class CWE_730_RegexInjection extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String regex = request.getParameter("regex");
        String input = request.getParameter("input");

        // BAD: Unsanitized user input is used to construct a regular expression
        boolean matches = input.matches(regex);
        response.getWriter().print("matches=" + matches);
    }
}

