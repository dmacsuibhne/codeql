import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.jexl3.introspection.JexlSandbox;

public class SaferJexlExpressionEvaluationWithSandbox {
    public void evaluate(Socket socket) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()))) {

            JexlSandbox onlyMath = new JexlSandbox(false);
            onlyMath.white("java.lang.Math");
            JexlEngine jexl = new JexlBuilder().sandbox(onlyMath).create(); // GOOD: using a sandbox

            String input = reader.readLine();
            JexlExpression expression = jexl.createExpression(input);
            JexlContext context = new MapContext();
            expression.evaluate(context);
        }
    }
}
