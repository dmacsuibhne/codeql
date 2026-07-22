import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SSTIGood {

        @GetMapping(value = "good")
        public void good(HttpServletRequest request) {
		Velocity.init();
		VelocityContext context = new VelocityContext();

		context.put("name", "Velocity");
		context.put("project", "Jakarta");

		String s = "We are using $project $name to render this.";
		StringWriter w = new StringWriter();
		Velocity.evaluate(context, w, "mystring", s); // GOOD: s is a constant string
		System.out.println(" string : " + w);
	}
}
