import android.os.CancellationSignal;
import androidx.biometric.BiometricPrompt;
import java.util.concurrent.Executor;

public class AndroidInsecureLocalAuthenticationBad {
    private BiometricPrompt biometricPrompt;
    private CancellationSignal cancellationSignal;
    private Executor executor;

    private void grantAccess() {
    }

    void authenticate() {
biometricPrompt.authenticate(
    cancellationSignal,
    executor,
    new BiometricPrompt.AuthenticationCallback() {
        @Override
        // BAD: This authentication callback does not make use of a `CryptoObject` from the `result`.
        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
            grantAccess();
        }
    });
    }
}
