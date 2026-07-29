package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-020: user input written into an error response. Source: "page". Sink: response.sendError(...). */
public class CWE_020_ExternalAPISinkExample extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // BAD: a request parameter is written directly to an error response page
        response.sendError(HttpServletResponse.SC_NOT_FOUND,
                "The page \"" + request.getParameter("page") + "\" was not found.");
    }
}
