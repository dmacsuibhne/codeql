import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UrlRedirectGoodDomain extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String urlString = request.getParameter("page");
            URI url = new URI(urlString);

            if (!url.isAbsolute()) {
                response.sendRedirect(url.toString()); // GOOD: The redirect is to a relative URL
            }

            if ("example.org".equals(url.getHost())) {
                response.sendRedirect(url.toString()); // GOOD: The redirect is to a known host
            }
        } catch (URISyntaxException e) {
            // handle exception
        }
    }
}