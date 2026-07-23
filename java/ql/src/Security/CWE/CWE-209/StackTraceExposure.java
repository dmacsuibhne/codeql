import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StackTraceExposure extends HttpServlet {

    private void doSomeWork() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            doSomeWork();
        } catch (NullPointerException ex) {
            // BAD: printing a stack trace back to the response
            ex.printStackTrace(response.getWriter());
            return;
        }

        try {
            doSomeWork();
        } catch (NullPointerException ex) {
            // GOOD: log the stack trace, and send back a non-revealing response
            log("Exception occurred", ex);
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Exception occurred");
            return;
        }
    }
}
