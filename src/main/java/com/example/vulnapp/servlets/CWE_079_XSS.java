package com.example.vulnapp.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CWE-079: Reflected Cross-Site Scripting.
 * Source: request parameter "page". Sink: response.getWriter().print(...).
 * Root cause: user input written to the response without output encoding.
 */
public class CWE_079_XSS extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // BAD: a request parameter is written directly to the Servlet response stream
        response.getWriter().print(
                "The page \"" + request.getParameter("page") + "\" was not found.");
    }
}

