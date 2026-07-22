import java.security.AccessControlException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import org.apache.commons.jexl3.JexlArithmetic;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.JexlOperator;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.jexl3.introspection.JexlMethod;
import org.apache.commons.jexl3.introspection.JexlPropertyGet;
import org.apache.commons.jexl3.introspection.JexlPropertySet;
import org.apache.commons.jexl3.introspection.JexlUberspect;
import org.apache.commons.jexl3.introspection.JexlUberspect.PropertyResolver;

public class SaferJexlExpressionEvaluationWithUberspectSandbox {
public void evaluate(Socket socket) throws IOException {
  try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(socket.getInputStream()))) {
    
    JexlUberspect sandbox = new JexlUberspectSandbox();
    JexlEngine jexl = new JexlBuilder().uberspect(sandbox).create();
      
    String input = reader.readLine();
    JexlExpression expression = jexl.createExpression(input); // GOOD: jexl uses a sandbox
    JexlContext context = new MapContext();
    expression.evaluate(context);
  }
}

  private static class JexlUberspectSandbox implements JexlUberspect {

    private static final List<String> ALLOWED_CLASSES =
              Arrays.asList("java.lang.Math", "java.util.Random");

    private final JexlUberspect uberspect = new JexlBuilder().create().getUberspect();

    private void checkAccess(Object obj) {
      if (!ALLOWED_CLASSES.contains(obj.getClass().getCanonicalName())) {
        throw new AccessControlException("Not allowed");
      }
    }

    @Override
    public JexlMethod getMethod(Object obj, String method, Object... args) {
      checkAccess(obj);
      return uberspect.getMethod(obj, method, args);
    }

    @Override
    public List<PropertyResolver> getResolvers(JexlOperator op, Object obj) {
      checkAccess(obj);
      return uberspect.getResolvers(op, obj);
    }

    @Override
    public void setClassLoader(ClassLoader loader) {
      uberspect.setClassLoader(loader);
    }

    @Override
    public int getVersion() {
      return uberspect.getVersion();
    }

    @Override
    public JexlMethod getConstructor(Object obj, Object... args) {
      checkAccess(obj);
      return uberspect.getConstructor(obj, args);
    }

    @Override
    public JexlPropertyGet getPropertyGet(Object obj, Object identifier) {
      checkAccess(obj);
      return uberspect.getPropertyGet(obj, identifier);
    }

    @Override
    public JexlPropertyGet getPropertyGet(List<PropertyResolver> resolvers, Object obj, Object identifier) {
      checkAccess(obj);
      return uberspect.getPropertyGet(resolvers, obj, identifier);
    }

    @Override
    public JexlPropertySet getPropertySet(Object obj, Object identifier, Object arg) {
      checkAccess(obj);
      return uberspect.getPropertySet(obj, identifier, arg);
    }

    @Override
    public JexlPropertySet getPropertySet(List<PropertyResolver> resolvers, Object obj, Object identifier, Object arg) {
      checkAccess(obj);
      return uberspect.getPropertySet(resolvers, obj, identifier, arg);
    }

    @Override
    public Iterator<?> getIterator(Object obj) {
      checkAccess(obj);
      return uberspect.getIterator(obj);
    }

    @Override
    public JexlArithmetic.Uberspect getArithmetic(JexlArithmetic arithmetic) {
      return uberspect.getArithmetic(arithmetic);
    } 
  }
}