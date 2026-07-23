import android.app.Activity;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewAssetLoader.AssetsPathHandler;
import androidx.webkit.WebViewClientCompat;

public class AssetLoaderExample extends Activity {
    private WebView webView;
    private WebViewAssetLoader assetLoader;

    void configure() {
WebViewAssetLoader loader = new WebViewAssetLoader.Builder()
    // Replace the domain with a domain you control, or use the default
    // appassets.androidplatform.com
    .setDomain("appassets.example.com")
    .addPathHandler("/resources", new AssetsPathHandler(this))
    .build();

webView.setWebViewClient(new WebViewClientCompat() {
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return assetLoader.shouldInterceptRequest(request.getUrl());
    }
});

webView.loadUrl("https://appassets.example.com/resources/www/index.html");
    }
}
