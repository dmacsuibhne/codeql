package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mvel2.MVEL;
/**
 * CWE-094 MVEL injection.
 * Source: "expression". Sink: MVEL.eval(expression).
 */
public class CWE_094_MvelExpressionEvaluation extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String expression = request.getParameter("expression");
        try {
            // BAD: the user-provided expression is directly evaluated
            Object result = MVEL.eval(expression);
            response.getWriter().print("result=" + result);
        } catch (Exception e) {
            response.getWriter().print("evaluated");
        }
    }
}
