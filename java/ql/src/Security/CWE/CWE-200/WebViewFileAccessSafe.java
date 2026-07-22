import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewFileAccessSafe {
    private WebView view;

    void configure() {
WebSettings settings = view.getSettings();

// GOOD: WebView is configured to disallow file access
settings.setAllowFileAccess(false);
settings.setAllowFileAccessFromURLs(false);
settings.setAllowUniversalAccessFromURLs(false);
    }
}
