import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Bad extends BroadcastReceiver {
    static class MainActivity {
        void saveLocalData() {}
        void stopActivity() {}
    }

    private MainActivity mainActivity;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        // BAD: The code does not check if the intent is an ACTION_SHUTDOWN intent
        mainActivity.saveLocalData();
        mainActivity.stopActivity();
    }
}