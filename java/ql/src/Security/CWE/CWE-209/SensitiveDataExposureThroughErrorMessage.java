import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SensitiveDataExposureThroughErrorMessage extends HttpServlet {

    private void doSomeWork() {
    }

    private void log(String message, String detail) {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            doSomeWork();
        } catch (NullPointerException ex) {
            // BAD: printing a exception message back to the response
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    ex.getMessage());
            return;
        }

        try {
            doSomeWork();
        } catch (NullPointerException ex) {
            // GOOD: log the exception message, and send back a non-revealing response
            log("Exception occurred", ex.getMessage());
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Exception occurred");
            return;
        }
    }
}
