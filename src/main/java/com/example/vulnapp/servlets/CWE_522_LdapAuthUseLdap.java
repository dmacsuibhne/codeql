package com.example.vulnapp.servlets;
import com.example.vulnapp.Backends;
import java.io.IOException;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.directory.InitialDirContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-522 LDAP (cleartext) authentication. Sink: new InitialDirContext(environment) over ldap://. */
public class CWE_522_LdapAuthUseLdap extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String ldapUserName = request.getParameter("username");
        String password = request.getParameter("password");
        // BAD: LDAP authentication is used (cleartext, no TLS)
        String ldapUrl = "ldap://localhost:" + Backends.LDAP_PORT;
        Hashtable<String, String> environment = new Hashtable<String, String>();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environment.put(Context.PROVIDER_URL, ldapUrl);
        environment.put(Context.SECURITY_AUTHENTICATION, "simple");
        environment.put(Context.SECURITY_PRINCIPAL, ldapUserName);
        environment.put(Context.SECURITY_CREDENTIALS, password);
        try {
            InitialDirContext dirContext = new InitialDirContext(environment);
            response.getWriter().print("bound");
        } catch (Exception e) {
            response.getWriter().print("bind attempted");
        }
    }
}
