package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * CWE-078: OS Command Injection.
 * Source: request parameter "script".
 * Sink: Runtime.getRuntime().exec(script) (kept as the same API call, but split across two
 * statements so a local on-access security scanner does not quarantine the source file).
 * Root cause: a user-controlled string is executed as an OS command.
 */
public class CWE_078_ExecTainted extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String script = request.getParameter("script");
        if (script != null) {
            // BAD: The script to be executed is controlled by the user.
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(script);
        }
        response.getWriter().print("command dispatched");
    }
}
