package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
/** CWE-094 SpEL parse of user input (restricted context). Source: "expression". Sink: parser.parseExpression(string). */
public class CWE_094_SaferSpelExpressionEvaluation extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String string = request.getParameter("expression");
        ExpressionParser parser = new SpelExpressionParser();
        // AVOID: string is controlled by the user
        Expression expression = parser.parseExpression(string);
        SimpleEvaluationContext context = SimpleEvaluationContext.forReadWriteDataBinding().build();
        // OK: Untrusted expressions are evaluated in a restricted context
        Object result = expression.getValue(context);
        response.getWriter().print("result=" + result);
    }
}
