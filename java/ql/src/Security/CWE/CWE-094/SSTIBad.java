import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SSTIBad {

    @GetMapping(value = "bad")
    public void bad(HttpServletRequest request) {
        Velocity.init();

        String code = request.getParameter("code");

        VelocityContext context = new VelocityContext();

        context.put("name", "Velocity");
        context.put("project", "Jakarta");

        StringWriter w = new StringWriter();
        // evaluate( Context context, Writer out, String logTag, String instring )
        // BAD: code is controlled by the user
        Velocity.evaluate(context, w, "mystring", code);
    }
}
