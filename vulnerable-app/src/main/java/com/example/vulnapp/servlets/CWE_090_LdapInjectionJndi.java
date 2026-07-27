package com.example.vulnapp.servlets;

import com.example.vulnapp.Backends;

import java.io.IOException;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CWE-090: LDAP Injection.
 * Source: request parameters "organization_name" and "username".
 * Sink: ctx.search(dn, filter, new SearchControls()).
 * Root cause: user input used in the DN and search filter without encoding.
 */
public class CWE_090_LdapInjectionJndi extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String organizationName = request.getParameter("organization_name");
        String username = request.getParameter("username");

        // BAD: User input used in DN (Distinguished Name) without encoding
        String dn = "OU=People,O=" + organizationName;

        // BAD: User input used in search filter without encoding
        String filter = "username=" + username;

        try {
            DirContext ctx = createContext();
            ctx.search(dn, filter, new SearchControls());
            response.getWriter().print("searched");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private DirContext createContext() throws Exception {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost:" + Backends.LDAP_PORT);
        env.put(Context.SECURITY_AUTHENTICATION, "none");
        return new InitialDirContext(env);
    }
}


