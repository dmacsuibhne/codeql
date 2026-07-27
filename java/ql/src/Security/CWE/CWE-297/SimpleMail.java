package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
/** CWE-297 Commons Email without setSSLCheckServerIdentity. Sink: email.send() with SSL but no identity check. */
public class CWE_297_SimpleMail extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Email email = new SimpleEmail();
            email.setHostName("hostName");
            email.setSmtpPort(25);
            email.setAuthenticator(new DefaultAuthenticator("username", "password"));
            email.setSSLOnConnect(true);
            // BAD: setSSLCheckServerIdentity not set
            email.setFrom("from@example.com");
            email.setSubject("subject");
            email.setMsg("body");
            email.addTo("to@example.com");
            email.send();
            response.getWriter().print("sent");
        } catch (Exception e) {
            response.getWriter().print("send attempted");
        }
    }
}
