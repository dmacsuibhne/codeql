import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SensitiveInfoLog {
    private static final Logger logger = LogManager.getLogger(SensitiveInfoLog.class);

    public static void main(String[] args) {
        {
            String password = "Pass@0rd";

            // BAD: user password is written to debug log
            logger.debug("User password is " + password);
        }

        {
            String password = "Pass@0rd";

            // GOOD: user password is never written to debug log
            logger.debug("User password changed");
        }
    }
}
