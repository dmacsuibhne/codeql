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

import org.owasp.esapi.Encoder;
import org.owasp.esapi.reference.DefaultEncoder;

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

            try {
                // ESAPI encoder
                Encoder encoder = DefaultEncoder.getInstance();

                // GOOD: Organization name is encoded before being used in DN
                String safeOrganizationName = encoder.encodeForDN(organizationName);
                String safeDn = "OU=People,O=" + safeOrganizationName;

                // GOOD: User input is encoded before being used in search filter
                String safeUsername = encoder.encodeForLDAP(username);
                String safeFilter = "username=" + safeUsername;

                ctx.search(safeDn, safeFilter, new SearchControls());
            } catch (Throwable esapiUnavailable) {
                // The GOOD branch is retained for comparison with the original snippet; it needs an
                // ESAPI.properties configuration that is not shipped here, so its failure is ignored.
            }
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


