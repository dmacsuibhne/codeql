import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;

public class GoodRandomInitializationVector {
    private Key key;

    void encrypt() throws GeneralSecurityException {
        byte[] iv = new byte[16];
        SecureRandom random = SecureRandom.getInstanceStrong();
        random.nextBytes(iv); // GOOD: random initialization vector
        GCMParameterSpec params = new GCMParameterSpec(128, iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, key, params);
    }
}
