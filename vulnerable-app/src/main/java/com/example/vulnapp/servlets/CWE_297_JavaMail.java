package com.example.vulnapp.servlets;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.PropertiesUtil;
/** CWE-297 JavaMail without server certificate check. Sink: Session.getInstance without ssl.checkserveridentity. */
public class CWE_297_JavaMail extends HttpServlet {
    private static Authenticator buildAuthenticator(String username, String password) {
        return new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final Properties properties = PropertiesUtil.getSystemProperties();
        properties.put("mail.transport.protocol", "protocol");
        properties.put("mail.smtp.host", "hostname");
        properties.put("mail.smtp.socketFactory.class", "classname");
        final Authenticator authenticator = buildAuthenticator("username", "password");
        if (null != authenticator) {
            properties.put("mail.smtp.auth", "true");
        }
        // BAD: no mail.smtp.ssl.checkserveridentity set
        final Session session = Session.getInstance(properties, authenticator);
        response.getWriter().print("session created " + (session != null));
    }
}
