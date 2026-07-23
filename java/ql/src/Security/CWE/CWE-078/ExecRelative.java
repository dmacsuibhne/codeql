import java.io.IOException;

public class ExecRelative {
    static class Paths {
        static final String MAKE_PREFIX = "/usr";
    }

    public static void main(String[] args) throws IOException {
        // BAD: relative path
        Runtime.getRuntime().exec("make");
        
        // GOOD: absolute path
        Runtime.getRuntime().exec("/usr/bin/make");

        // GOOD: build an absolute path from known values
        Runtime.getRuntime().exec(Paths.MAKE_PREFIX + "/bin/make");
    }
}