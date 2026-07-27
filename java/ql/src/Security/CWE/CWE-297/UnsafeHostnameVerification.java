package com.example.vulnapp.servlets;
import java.io.IOException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-297: hostname verifier always returns true. Sink: setDefaultHostnameVerifier(verifier). */
public class CWE_297_UnsafeHostnameVerification extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HostnameVerifier verifier = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true; // BAD: accept even if the hostname doesn't match
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(verifier);
        response.getWriter().print("verifier set");
    }
}
