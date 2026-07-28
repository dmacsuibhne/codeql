package com.example.vulnapp.servlets;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.security.MessageDigest;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-312: cleartext storage of credentials in a cookie. Sink: response.addCookie(new Cookie("auth", data)). */
public class CWE_312_CleartextStorage extends HttpServlet {
    private static String bytesToString(byte[] bytes) {
        return new String(bytes);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String user = request.getParameter("user");
        PasswordAuthentication credentials =
                new PasswordAuthentication(user, "BP@ssw0rd".toCharArray());
        String data = credentials.getUserName() + ":" + new String(credentials.getPassword());
        // BAD: store data in a cookie in cleartext form
        response.addCookie(new Cookie("auth", data));

        try {
            PasswordAuthentication goodCredentials =
                    new PasswordAuthentication(user, "GP@ssw0rd".toCharArray());
            String salt = "ThisIsMySalt";
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.reset();
            String credentialsToHash =
                    goodCredentials.getUserName() + ":" + goodCredentials.getPassword();
            byte[] hashedCredsAsBytes =
                    messageDigest.digest((salt + credentialsToHash).getBytes("UTF-8"));
            String goodData = bytesToString(hashedCredsAsBytes);

            // GOOD: store data in a cookie in encrypted (hashed) form
            response.addCookie(new Cookie("auth", goodData));
        } catch (Exception hashUnavailable) {
        }

        response.getWriter().print("stored");
    }
}
