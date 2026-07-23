import android.webkit.WebSettings;
import android.webkit.WebView;

public class ContentAccessDisabled {
    private WebView webview;

    void configure() {
WebSettings settings = webview.getSettings();

// GOOD: WebView is configured to disallow content access
settings.setAllowContentAccess(false);
    }
}
