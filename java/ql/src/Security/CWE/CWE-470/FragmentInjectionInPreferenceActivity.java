import android.app.Fragment;
import android.preference.PreferenceActivity;

class SafeFragment1 extends Fragment {}
class SafeFragment2 extends Fragment {}
class SafeFragment3 extends Fragment {}

class UnsafeActivity extends PreferenceActivity {

    @Override
    protected boolean isValidFragment(String fragmentName) {
        // BAD: any Fragment name can be provided.
        return true;
    }
}


class SafeActivity extends PreferenceActivity {
    @Override
    protected boolean isValidFragment(String fragmentName) {
        // Good: only trusted Fragment names are allowed.
        return SafeFragment1.class.getName().equals(fragmentName)
                || SafeFragment2.class.getName().equals(fragmentName)
                || SafeFragment3.class.getName().equals(fragmentName);
    }

}

