import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;

public class AndroidSensitiveNotifications extends Activity {
    private static final String CHANNEL_ID = "channel_id";

// BAD: `password` is exposed in a notification.
void confirmPassword(String password) {
    NotificationManager manager = NotificationManager.from(this);
    manager.send(
        new Notification.Builder(this, CHANNEL_ID)
        .setContentText("Your password is: " + password)
        .build());
}
}
