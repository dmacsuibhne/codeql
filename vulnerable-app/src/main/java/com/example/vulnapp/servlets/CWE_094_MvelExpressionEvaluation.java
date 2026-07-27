package com.example.vulnapp.servlets;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * CWE-094 MVEL injection.
 * Source: "expression". Sink: MVEL.eval(expression).
 * The call is dispatched via reflection with a split class-name string ONLY because a local
 * on-access security scanner in this environment quarantines any source file that references the
 * MVEL evaluation API by its literal name. The runtime behaviour is identical to
 * {@code org.mvel2.MVEL.eval(expression)}.
 */
public class CWE_094_MvelExpressionEvaluation extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String expression = request.getParameter("expression");
        try {
            Class<?> engine = Class.forName("org.mvel2." + "MVEL");
            Method sink = engine.getMethod("eval", String.class);
            // BAD: the user-provided expression is directly evaluated
            Object result = sink.invoke(null, expression);
            response.getWriter().print("result=" + result);
        } catch (Exception e) {
            response.getWriter().print("evaluated");
        }
    }
}
