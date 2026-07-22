import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class BrokenCryptoAlgorithm {
    void run(SecretKeySpec secretKeySpec, String input) throws Exception {
// BAD: DES is a weak algorithm
Cipher cipher = Cipher.getInstance("DES");
cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

byte[] encrypted = cipher.doFinal(input.getBytes("UTF-8"));

// ...

// GOOD: AES is a strong algorithm
Cipher aes = Cipher.getInstance("AES");

// ...
    }
}
