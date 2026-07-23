import java.net.PasswordAuthentication;
import java.security.MessageDigest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CleartextStorage {
    static HttpServletResponse response;

    private static String bytesToString(byte[] bytes) {
        return new String(bytes);
    }

    public static void main(String[] args) throws Exception {
        {
            String data;
            PasswordAuthentication credentials =
                    new PasswordAuthentication("user", "BP@ssw0rd".toCharArray());
            data = credentials.getUserName() + ":" + new String(credentials.getPassword());

            // BAD: store data in a cookie in cleartext form
            response.addCookie(new Cookie("auth", data));
        }

        {
            String data;
            PasswordAuthentication credentials =
                    new PasswordAuthentication("user", "GP@ssw0rd".toCharArray());
            String salt = "ThisIsMySalt";
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.reset();
            String credentialsToHash =
                    credentials.getUserName() + ":" + credentials.getPassword();
            byte[] hashedCredsAsBytes =
                    messageDigest.digest((salt + credentialsToHash).getBytes("UTF-8"));
            data = bytesToString(hashedCredsAsBytes);

            // GOOD: store data in a cookie in encrypted form
            response.addCookie(new Cookie("auth", data));
        }
    }
}
