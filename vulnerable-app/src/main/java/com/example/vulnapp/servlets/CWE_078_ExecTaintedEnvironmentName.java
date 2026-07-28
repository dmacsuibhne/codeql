package com.example.vulnapp.servlets;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-078: tainted environment name. Source: "attribute","value". Sink: env.put(attr,value); processBuilder.start(). */
public class CWE_078_ExecTaintedEnvironmentName extends HttpServlet {
    private final ProcessBuilder processBuilder = new ProcessBuilder("true");
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String attr = request.getParameter("attribute");
        String value = request.getParameter("value");
        Map<String, String> env = processBuilder.environment();
        // BAD: attr and value are tainted and being added to the environment
        env.put(attr, value);
        processBuilder.start();
        response.getWriter().print("started");
    }
}
