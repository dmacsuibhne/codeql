import android.app.Activity;
import android.widget.TextView;

public class AndroidSensitiveTextBad extends Activity {
    void showPassword(String password) {
TextView pwView = findViewById(R.id.pw_text);
pwView.setText("Your password is: " + password); // BAD: password is shown immediately
    }
}
