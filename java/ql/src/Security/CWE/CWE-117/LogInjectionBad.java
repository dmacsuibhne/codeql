package com.example.vulnapp.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CWE-117: Log Injection.
 * Source: request parameter "username". Sink: log.warn("User:'{}'", username).
 * Root cause: unsanitized user input (e.g. containing newlines) written to the log.
 */
public class CWE_117_LogInjectionBad extends HttpServlet {

    private final Logger log = LoggerFactory.getLogger(CWE_117_LogInjectionBad.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        // BAD: untrusted input logged without sanitizing line breaks
        log.warn("User:'{}'", username);
        response.getWriter().print(username);
    }
}

