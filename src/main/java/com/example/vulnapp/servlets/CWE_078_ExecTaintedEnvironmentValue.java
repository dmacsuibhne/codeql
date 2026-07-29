package com.example.vulnapp.servlets;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-078: tainted environment value. Source: "path". Sink: env.put("PATH", path); processBuilder.start(). */
public class CWE_078_ExecTaintedEnvironmentValue extends HttpServlet {
    private final ProcessBuilder processBuilder = new ProcessBuilder("true");
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getParameter("path");
        Map<String, String> env = processBuilder.environment();
        // BAD: path is tainted and being added to the environment
        env.put("PATH", path);
        processBuilder.start();
        response.getWriter().print("started");
    }
}
