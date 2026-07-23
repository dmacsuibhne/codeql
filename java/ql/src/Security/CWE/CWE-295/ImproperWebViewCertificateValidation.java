import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

class Bad extends WebViewClient {
    // BAD: All certificates are trusted.
    public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) { // $ hasResult
        handler.proceed();
    }
}

class Good extends WebViewClient {
    PublicKey myPubKey = null;

    // GOOD: Only certificates signed by a certain public key are trusted.
    public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) { // $ hasResult
        try {
            X509Certificate cert = error.getCertificate().getX509Certificate();
            cert.verify(this.myPubKey);
            handler.proceed();
        }
        catch (CertificateException|NoSuchAlgorithmException|InvalidKeyException|NoSuchProviderException|SignatureException e) {
            handler.cancel();
        }
    }
}
