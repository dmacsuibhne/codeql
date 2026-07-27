package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ognl.Ognl;
/** CWE-917 OGNL injection. Source: "expression". Sink: Ognl.getValue(expression, root). */
public class CWE_917_OgnlInjection extends HttpServlet {
    public boolean isValid(String expression) {
        // Custom method to validate the expression.
        // For instance, make sure it doesn't include unexpected code.
        return true;
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String expression = request.getParameter("expression");
        Object root = new Object();
        try {
            // BAD: User provided expression is evaluated
            Object result = Ognl.getValue(expression, root);

            // GOOD: The name is validated and expression is evaluated in sandbox
            System.setProperty("ognl.security.manager", ""); // Or add -Dognl.security.manager to JVM args
            if (isValid(expression)) {
                Ognl.getValue(expression, root);
            } else {
                // Reject the request
            }
            response.getWriter().print("result=" + result);
        } catch (Exception e) {
            response.getWriter().print("evaluated");
        }
    }
}
