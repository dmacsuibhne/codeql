import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExecTaintedEnvironmentName {
    private ProcessBuilder processBuilder = new ProcessBuilder();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String attr = request.getParameter("attribute");
        String value = request.getParameter("value");

        Map<String, String> env = processBuilder.environment();
        // BAD: attr and value are tainted and being added to the environment
        env.put(attr, value);

        processBuilder.start();
    }
}
