import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AndroidSensitiveTextGood extends Activity {
    void showPassword(String password) {
TextView pwView = findViewById(R.id.pw_text);
pwView.setVisibility(View.INVISIBLE);
pwView.setText("Your password is: " + password);

Button showButton = findViewById(R.id.show_pw_button);
showButton.setOnClickListener(new View.OnClickListener() {
    public void onClick(View v) {
      pwView.setVisibility(View.VISIBLE); // GOOD: password is only shown when the user clicks the button
    }
});
    }
}
