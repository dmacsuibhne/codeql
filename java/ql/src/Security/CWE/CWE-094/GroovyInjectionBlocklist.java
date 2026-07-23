import javax.servlet.http.HttpServletRequest;
import groovy.lang.GroovyClassLoader;

public class GroovyInjectionBlocklist {
}

class SandboxGroovyClassLoader extends ClassLoader {
    public SandboxGroovyClassLoader() {
        super();
    }

    public SandboxGroovyClassLoader(ClassLoader parent) {
        super(parent);
    }

    /* override `loadClass` here to prevent loading sensitive classes, such as `java.lang.Runtime`, `java.lang.ProcessBuilder`, `java.lang.System`, etc.  */
    /* Note we must also block `groovy.transform.ASTTest`, `groovy.lang.GrabConfig` and `org.buildobjects.process.ProcBuilder` to prevent compile-time RCE. */

    static void runWithSandboxGroovyClassLoader(HttpServletRequest untrusted) throws Exception {
        // GOOD: route all class-loading via sand-boxing classloader.
        GroovyClassLoader classLoader = new GroovyClassLoader(new SandboxGroovyClassLoader());

        Class<?> scriptClass = classLoader.parseClass(untrusted.getQueryString());
        Object scriptInstance = scriptClass.newInstance();
        Object result = scriptClass.getDeclaredMethod("bar", new Class[]{}).invoke(scriptInstance, new Object[]{});
    }
}