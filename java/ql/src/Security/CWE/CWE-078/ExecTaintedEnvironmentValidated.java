import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class ExecTaintedEnvironmentValidated {
    private ProcessBuilder processBuilder = new ProcessBuilder();
    private List<String> permittedJavaOptions;

    boolean validOption(String opt, String value) {
        return true;
    }

    void configure(HttpServletRequest request) {
        String opt = request.getParameter("opt");
        String value = request.getParameter("value");

        Map<String, String> env = processBuilder.environment();

// GOOD: opt and value are checked before being added to the environment
        if (permittedJavaOptions.contains(opt) && validOption(opt, value)) {
            env.put(opt, value);
        }
    }
}
