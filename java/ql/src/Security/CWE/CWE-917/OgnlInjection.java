package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ognl.Ognl;
/** CWE-917 OGNL injection. Source: "expression". Sink: Ognl.getValue(expression, root). */
public class CWE_917_OgnlInjection extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String expression = request.getParameter("expression");
        Object root = new Object();
        try {
            // BAD: User provided expression is evaluated
            Object result = Ognl.getValue(expression, root);
            response.getWriter().print("result=" + result);
        } catch (Exception e) {
            response.getWriter().print("evaluated");
        }
    }
}
