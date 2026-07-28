package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
/** CWE-094 SpEL injection. Source: "expression". Sink: expression.getValue(). */
public class CWE_094_UnsafeSpelExpressionEvaluation extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String string = request.getParameter("expression");
        ExpressionParser parser = new SpelExpressionParser();
        // BAD: string is controlled by the user
        Expression expression = parser.parseExpression(string);
        Object result = expression.getValue();
        response.getWriter().print("result=" + result);
    }
}
