import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebSettingsEnableJavascript {
    private WebView webview;

    void configure() {
WebSettings settings = webview.getSettings();
settings.setJavaScriptEnabled(true); // BAD: webview has JavaScript enabled
    }
}
