package com.example.vulnapp.servlets;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-297: hostname verifier always returns true. Sink: setDefaultHostnameVerifier(verifier). */
public class CWE_297_UnsafeHostnameVerification extends HttpServlet {
    static void check(String[] hosts, X509Certificate cert) throws SSLException {
        throw new RuntimeException("Not implemented");
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HostnameVerifier verifier = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true; // BAD: accept even if the hostname doesn't match
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(verifier);

        HostnameVerifier goodVerifier = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                try { // GOOD: verify the certificate
                    Certificate[] certs = session.getPeerCertificates();
                    X509Certificate x509 = (X509Certificate) certs[0];
                    check(new String[]{hostname}, x509); //todo not a genuine GOOD unless check() is implemented for real
                    return true;
                } catch (SSLException e) {
                    return false;
                }
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(goodVerifier);

        response.getWriter().print("verifier set");
    }
}
