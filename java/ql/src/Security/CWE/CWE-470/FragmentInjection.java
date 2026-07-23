import android.app.Fragment;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

class MyFragment extends Fragment {}

public class FragmentInjection extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstance) {
        try {
            super.onCreate(savedInstance);
            // BAD: Fragment instantiated from user input without validation
            {
                String fName = getIntent().getStringExtra("fragmentName");
                getFragmentManager().beginTransaction().replace(com.android.internal.R.id.prefs,
                        Fragment.instantiate(this, fName, null)).commit();
            }
            // GOOD: Fragment instantiated statically
            {
                getFragmentManager().beginTransaction()
                        .replace(com.android.internal.R.id.prefs, new MyFragment()).commit();
            }
        } catch (Exception e) {
        }
    }

}
