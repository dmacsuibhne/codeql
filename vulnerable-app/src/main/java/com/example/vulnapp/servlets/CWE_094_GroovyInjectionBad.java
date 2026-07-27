package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import groovy.lang.GroovyShell;
/** CWE-094 Groovy injection. Source: "script". Sink: shell.evaluate(script). */
public class CWE_094_GroovyInjectionBad extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        GroovyShell shell = new GroovyShell();
        String script = request.getParameter("script");
        Object result = shell.evaluate(script); // BAD: Groovy code injection
        response.getWriter().print("result=" + result);
    }
}
