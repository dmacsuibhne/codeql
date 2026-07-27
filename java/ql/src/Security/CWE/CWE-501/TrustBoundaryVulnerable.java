package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-501: trust boundary violation. Source: "username". Sink: session.setAttribute("username", username). */
public class CWE_501_TrustBoundaryVulnerable extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        // BAD: input written to the session without being sanitized
        request.getSession().setAttribute("username", username);
        response.getWriter().print("stored in session");
    }
}
