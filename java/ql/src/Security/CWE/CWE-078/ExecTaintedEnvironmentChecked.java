import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class ExecTaintedEnvironmentChecked {
    void configure(ProcessBuilder builder, HttpServletRequest request) {
        Map<String, String> env = builder.environment();
        String debug = request.getParameter("debug");

// GOOD: Checking the value and not tainting the variable added to the environment
        if (debug != null) {
            env.put("PYTHONDEBUG", "1");
        }
    }
}
