import java.util.Base64;
import java.util.Random;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class InsecureRandomnessCookie {
    private String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    void setCookie(HttpServletResponse response) {
        Random r = new Random(); // BAD: Random is not cryptographically secure

        byte[] bytes = new byte[16];
        r.nextBytes(bytes);

        String cookieValue = encode(bytes);

        Cookie cookie = new Cookie("name", cookieValue);
        response.addCookie(cookie);
    }
}
