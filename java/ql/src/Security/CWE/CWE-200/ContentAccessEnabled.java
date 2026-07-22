import android.webkit.WebSettings;
import android.webkit.WebView;

public class ContentAccessEnabled {
    private WebView webview;

    void configure() {
WebSettings settings = webview.getSettings();

// BAD: WebView is configured to allow content access
settings.setAllowContentAccess(true);
    }
}
