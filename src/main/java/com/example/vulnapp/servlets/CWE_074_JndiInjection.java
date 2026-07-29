package com.example.vulnapp.servlets;
import java.io.IOException;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-074 JNDI injection. Source: "name". Sink: ctx.lookup(name). */
public class CWE_074_JndiInjection extends HttpServlet {

    boolean isValid(String name) {
        return true;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        try {
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
            env.put(Context.PROVIDER_URL, "rmi://trusted-server:1099");
            InitialContext ctx = new InitialContext(env);

            // BAD: User input used in lookup
            ctx.lookup(name);

            // GOOD: The name is validated before being used in lookup
            if (isValid(name)) {
                ctx.lookup(name);
            } else {
                // Reject the request
            }
        } catch (Exception e) {
            // reaching the sink is what matters; remote lookup is expected to fail in tests
        }
        response.getWriter().print("looked up");
    }
}
