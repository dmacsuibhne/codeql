package com.example.vulnapp.servlets;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
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

            try {
                SSLContext goodContext = SSLContext.getInstance("TLS");
                File certificateFile = new File("path/to/self-signed-certificate");
                // Create a `KeyStore` with default type
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                // `keyStore` is initially empty
                keyStore.load(null, null);
                X509Certificate generatedCertificate;
                try (InputStream cert = new FileInputStream(certificateFile)) {
                    generatedCertificate = (X509Certificate) CertificateFactory.getInstance("X509")
                            .generateCertificate(cert);
                }
                // Add the self-signed certificate to the key store
                keyStore.setCertificateEntry(certificateFile.getName(), generatedCertificate);
                // Get default `TrustManagerFactory`
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                // Use it with our key store that trusts our self-signed certificate
                tmf.init(keyStore);
                TrustManager[] trustManagers = tmf.getTrustManagers();
                goodContext.init(null, trustManagers, null);
                // GOOD, we are not using a custom `TrustManager` but instead have
                // added the self-signed certificate we want to trust to the key
                // store. Note, the `trustManagers` will **only** trust this one
                // certificate.

                URL url = new URL("https://self-signed.badssl.com/");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setSSLSocketFactory(goodContext.getSocketFactory());
            } catch (Exception goodBranchNeedsCertFile) {
                // The GOOD branch requires a real self-signed certificate file that is not shipped here.
            }

            response.getWriter().print("context initialized");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
