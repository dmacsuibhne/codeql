package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.util.Eval;
/** CWE-094 Groovy injection. Source: "script". Sinks: GroovyClassLoader.parseClass, Eval.me, GroovyShell.evaluate. */
public class CWE_094_GroovyInjectionBad extends HttpServlet {

    void injectionViaClassLoader(HttpServletRequest request) throws Exception {
        String script = request.getParameter("script");
        final GroovyClassLoader classLoader = new GroovyClassLoader();
        Class groovy = classLoader.parseClass(script); // BAD: Groovy code injection
        GroovyObject groovyObj = (GroovyObject) groovy.newInstance();
    }

    void injectionViaEval(HttpServletRequest request) {
        String script = request.getParameter("script");
        Eval.me(script); // BAD: Groovy code injection
    }

    Object injectionViaGroovyShell(HttpServletRequest request) {
        GroovyShell shell = new GroovyShell();
        String script = request.getParameter("script");
        return shell.evaluate(script); // BAD: Groovy code injection
    }

    void injectionViaGroovyShellGroovyCodeSource(HttpServletRequest request) {
        GroovyShell shell = new GroovyShell();
        String script = request.getParameter("script");
        GroovyCodeSource gcs = new GroovyCodeSource(script, "test", "Test");
        shell.evaluate(gcs); // BAD: Groovy code injection
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Object result = null;
        try { injectionViaClassLoader(request); } catch (Exception e) { }
        try { injectionViaEval(request); } catch (Exception e) { }
        try { result = injectionViaGroovyShell(request); } catch (Exception e) { }
        try { injectionViaGroovyShellGroovyCodeSource(request); } catch (Exception e) { }
        response.getWriter().print("result=" + result);
    }
}
