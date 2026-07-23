import java.security.SecureRandom;
import java.util.Base64;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class SecureRandomnessCookie {
    private String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    void setCookie(HttpServletResponse response) {
        SecureRandom r = new SecureRandom(); // GOOD: SecureRandom is cryptographically secure

        byte[] bytes = new byte[16];
        r.nextBytes(bytes);

        String cookieValue = encode(bytes);

        Cookie cookie = new Cookie("name", cookieValue);
        response.addCookie(cookie);
    }
}
