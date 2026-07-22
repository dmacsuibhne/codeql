import javax.servlet.http.HttpServletRequest;
import ognl.Ognl;
import ognl.OgnlException;

public class OgnlInjection {

public void evaluate(HttpServletRequest request, Object root) throws OgnlException {
  String expression = request.getParameter("expression");

  // BAD: User provided expression is evaluated
  Ognl.getValue(expression, root);
  
  // GOOD: The name is validated and expression is evaluated in sandbox
  System.setProperty("ognl.security.manager", ""); // Or add -Dognl.security.manager to JVM args
  if (isValid(expression)) {
    Ognl.getValue(expression, root);
  } else {
    // Reject the request
  }
}

public boolean isValid(String expression) {
  // Custom method to validate the expression.
  // For instance, make sure it doesn't include unexpected code.
  return true;
}
}
