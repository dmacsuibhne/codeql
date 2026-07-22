import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebSettingsDisableJavascript {
    private WebView webview;

    void configure() {
WebSettings settings = webview.getSettings();
settings.setJavaScriptEnabled(false); // GOOD: webview has JavaScript disabled
    }
}
