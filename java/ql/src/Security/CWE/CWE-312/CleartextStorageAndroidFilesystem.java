import android.content.Context;
import androidx.security.crypto.EncryptedFile;
import androidx.security.crypto.MasterKeys;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CleartextStorageAndroidFilesystem {

public void fileSystemStorageUnsafe(String name, String password) throws IOException {
        // BAD - sensitive data stored in cleartext
    FileWriter fw = new FileWriter("some_file.txt");
    fw.write(name + ":" + password);
    fw.close();
}

public void filesystemStorageEncryptedFileSafe(Context context, String name, String password) throws IOException, GeneralSecurityException {
        // GOOD - the whole file is encrypted with androidx.security.crypto.EncryptedFile
    File file = new File("some_file.txt");
    String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
    EncryptedFile encryptedFile = new EncryptedFile.Builder(
        file,
        context,
        masterKeyAlias,
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build();
        FileOutputStream encryptedOutputStream = encryptedFile.openFileOutput();
        encryptedOutputStream.write((name + ":" + password).getBytes(StandardCharsets.UTF_8));
}

public void fileSystemStorageSafe(String name, String password) throws IOException, NoSuchAlgorithmException {
        // GOOD - sensitive data is encrypted using a custom method
    FileWriter fw = new FileWriter("some_file.txt");
    fw.write(name + ":" + encrypt(password));
    fw.close();
}

private static String encrypt(String cleartext) throws NoSuchAlgorithmException {
    // Use an encryption or strong hashing algorithm in the real world.
    // The example below just returns a SHA-256 hash.
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] hash = digest.digest(cleartext.getBytes(StandardCharsets.UTF_8));
    String encoded = Base64.getEncoder().encodeToString(hash);
    return encoded;
}
}
