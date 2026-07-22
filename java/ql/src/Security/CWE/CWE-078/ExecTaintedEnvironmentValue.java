import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExecTaintedEnvironmentValue {
private ProcessBuilder processBuilder = new ProcessBuilder();

public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String path = request.getParameter("path");

    Map<String, String> env = processBuilder.environment();
    // BAD: path is tainted and being added to the environment
    env.put("PATH", path);

    processBuilder.start();
}
}
