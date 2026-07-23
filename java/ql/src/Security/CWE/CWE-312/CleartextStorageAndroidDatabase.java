import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CleartextStorageAndroidDatabase {

public void sqliteStorageUnsafe(Context ctx, String name, String password) {
        // BAD - sensitive information saved in cleartext.
        SQLiteDatabase db = ctx.openOrCreateDatabase("test", Context.MODE_PRIVATE, null);
        db.execSQL("INSERT INTO users VALUES (?, ?)", new String[] {name, password});
}

public void sqliteStorageSafe(Context ctx, String name, String password) throws NoSuchAlgorithmException {
        // GOOD - sensitive information encrypted with a custom method.
        SQLiteDatabase db = ctx.openOrCreateDatabase("test", Context.MODE_PRIVATE, null);
        db.execSQL("INSERT INTO users VALUES (?, ?)", new String[] {name, encrypt(password)});
}

public void sqlCipherStorageSafe(String name, String password, String databasePassword) {
        // GOOD - sensitive information saved using SQLCipher.
        net.sqlcipher.database.SQLiteDatabase db =
                net.sqlcipher.database.SQLiteDatabase.openOrCreateDatabase("test", databasePassword, null);
        db.execSQL("INSERT INTO users VALUES (?, ?)", new String[] {name, password});
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
