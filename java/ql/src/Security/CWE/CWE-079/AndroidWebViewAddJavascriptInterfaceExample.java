import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class AndroidWebViewAddJavascriptInterfaceExample {
    private WebView webview;

    static class ExposedObject extends SQLiteOpenHelper {
        private SQLiteDatabase db;

        ExposedObject(Context context) {
            super(context, "students.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

        @JavascriptInterface
        public String studentEmail(String studentName) {
            // SQL injection
            String query = "SELECT email FROM students WHERE studentname = '" + studentName + "'";

            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            String email = cursor.getString(0);

            return email;
        }
    }

    void run(Context context) {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new ExposedObject(context), "exposedObject");
        webview.loadData("", "text/html", null);

        String name = "Robert'; DROP TABLE students; --";
        // BAD: Untrusted input loaded into WebView
        webview.loadUrl("javascript:alert(exposedObject.studentEmail(\"" + name + "\"))");
    }
}
