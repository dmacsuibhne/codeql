import java.io.IOException;

public class ExecTainted {
    public static void main(String[] args) throws IOException {
        String script = System.getenv("SCRIPTNAME");
        if (script != null) {
            // BAD: The script to be executed is controlled by the user.
            Runtime.getRuntime().exec(script);
        }
    }
}