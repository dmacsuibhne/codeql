import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.Validator;

public class TrustBoundaryFixed {
    private Validator validator;

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");

        if (validator.isValidInput("HTTP parameter", username, "username", 20, false)) {
            // GOOD: The input is sanitized before being written to the session.
            request.getSession().setAttribute("username", username);
        }
    }
}
