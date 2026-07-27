package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/** CWE-532 sensitive info in logs. Source: "password". Sink: logger.debug("User password is " + password). */
public class CWE_532_SensitiveInfoLog extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(CWE_532_SensitiveInfoLog.class);
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String password = request.getParameter("password");
        // BAD: user password is written to debug log
        logger.debug("User password is " + password);
        response.getWriter().print("logged");
    }
}
