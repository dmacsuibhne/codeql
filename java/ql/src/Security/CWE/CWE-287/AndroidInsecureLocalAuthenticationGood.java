import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import androidx.biometric.BiometricPrompt;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.concurrent.Executor;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class AndroidInsecureLocalAuthenticationGood {
    private BiometricPrompt biometricPrompt;
    private CancellationSignal cancellationSignal;
    private Executor executor;

    private void grantAccessWithData(byte[] data) {
    }

private void generateSecretKey() throws GeneralSecurityException {
    KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(
        "MySecretKey",
        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
        .setUserAuthenticationRequired(true)
        .setInvalidatedByBiometricEnrollment(true)
        .build();
    KeyGenerator keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
    keyGenerator.init(keyGenParameterSpec);
    keyGenerator.generateKey();
}


private SecretKey getSecretKey() throws GeneralSecurityException, java.io.IOException {
    KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
    keyStore.load(null);
    return ((SecretKey)keyStore.getKey("MySecretKey", null));
}

private Cipher getCipher() throws GeneralSecurityException {
    return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
            + KeyProperties.BLOCK_MODE_CBC + "/"
            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
}

public void prompt(byte[] encryptedData) throws GeneralSecurityException, java.io.IOException {
    Cipher cipher = getCipher();
    SecretKey secretKey = getSecretKey();
    cipher.init(Cipher.DECRYPT_MODE, secretKey);

    biometricPrompt.authenticate(
        new BiometricPrompt.CryptoObject(cipher),
        cancellationSignal,
        executor,
        new BiometricPrompt.AuthenticationCallback() {
            @Override
            // GOOD: This authentication callback uses the result to decrypt some data.
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                Cipher cipher = result.getCryptoObject().getCipher();
                byte[] decryptedData = cipher.doFinal(encryptedData);
                grantAccessWithData(decryptedData);
            }
        }
    );
}
}
