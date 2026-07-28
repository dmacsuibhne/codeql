package com.example.vulnapp.servlets;

import java.io.IOException;
import java.util.regex.Pattern;
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

    public boolean badExample(HttpServletRequest request) {
        String regex = request.getParameter("regex");
        String input = request.getParameter("input");

        // BAD: Unsanitized user input is used to construct a regular expression
        return input.matches(regex);
    }

    public boolean goodExample(HttpServletRequest request) {
        String regex = request.getParameter("regex");
        String input = request.getParameter("input");

        // GOOD: User input is sanitized before constructing the regex
        return input.matches(Pattern.quote(regex));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean matches = badExample(request);
        goodExample(request);
        response.getWriter().print("matches=" + matches);
    }
}
