import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.webkit.WebView;

public class WebviewDebuggingEnabled extends Activity {
    void configure() {
// BAD - debugging is always enabled
WebView.setWebContentsDebuggingEnabled(true);

// GOOD - debugging is only enabled when this is a debug build, as indicated by the debuggable flag being set.
if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
    WebView.setWebContentsDebuggingEnabled(true);
}
    }
}
