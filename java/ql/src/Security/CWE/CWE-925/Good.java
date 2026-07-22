import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Good extends BroadcastReceiver {
    static class MainActivity {
        void saveLocalData() {}
        void stopActivity() {}
    }

    private MainActivity mainActivity;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        // GOOD: The code checks if the intent is an ACTION_SHUTDOWN intent
        if (!intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
            return;
        }
        mainActivity.saveLocalData();
        mainActivity.stopActivity();
    }
}