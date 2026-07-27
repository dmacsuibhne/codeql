package com.example.vulnapp.servlets;
import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
/** CWE-094 SSTI (Velocity). Source: "code". Sink: Velocity.evaluate(context, w, "mystring", code). */
public class CWE_094_SSTIBad extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Velocity.init();
        String code = request.getParameter("code");
        VelocityContext context = new VelocityContext();
        context.put("name", "Velocity");
        context.put("project", "Jakarta");
        StringWriter w = new StringWriter();
        // BAD: code is controlled by the user
        Velocity.evaluate(context, w, "mystring", code);
        response.getWriter().print(w.toString());
    }
}
