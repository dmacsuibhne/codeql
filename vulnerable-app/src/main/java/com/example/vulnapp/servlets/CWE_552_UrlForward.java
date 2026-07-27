package com.example.vulnapp.servlets;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CWE-552: Unvalidated URL Forward.
 * Source: request parameter "target". Sink: getRequestDispatcher(target).forward(...).
 * Root cause: user input used as a forward target without validation.
 */
public class CWE_552_UrlForward extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletConfig cfg = getServletConfig();
        ServletContext sc = cfg.getServletContext();

        // BAD: a request parameter is incorporated without validation into a URL forward
        sc.getRequestDispatcher(request.getParameter("target")).forward(request, response);
    }
}

