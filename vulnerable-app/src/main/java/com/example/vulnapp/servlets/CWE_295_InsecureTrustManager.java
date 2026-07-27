package com.example.vulnapp.servlets;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-295: trust manager that accepts any certificate. Sink: context.init(null, insecureTM, null). */
public class CWE_295_InsecureTrustManager extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            class InsecureTrustManager implements X509TrustManager {
                public X509Certificate[] getAcceptedIssuers() { return null; }
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    // BAD: Does not verify the certificate chain, allowing any certificate.
                }
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
            }
            SSLContext context = SSLContext.getInstance("TLS");
            TrustManager[] trustManager = new TrustManager[]{new InsecureTrustManager()};
            context.init(null, trustManager, null);
            response.getWriter().print("context initialized");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
