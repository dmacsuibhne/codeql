import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class AndroidMissingCertificatePinning1 {
    void run() throws IOException {
// BAD - By default, this network call does not use certificate pinning
URLConnection conn = new URL("https://example.com").openConnection();
    }
}
