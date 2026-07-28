package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
/** CWE-094 JEXL injection. Source: "input". Sink: jexl.createExpression(input).evaluate(context). */
public class CWE_094_UnsafeJexlExpressionEvaluation extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String input = request.getParameter("input");
        JexlEngine jexl = new JexlBuilder().create();
        // BAD: input is controlled by the user
        JexlExpression expression = jexl.createExpression(input);
        JexlContext context = new MapContext();
        Object result = expression.evaluate(context);
        response.getWriter().print("result=" + result);
    }
}
