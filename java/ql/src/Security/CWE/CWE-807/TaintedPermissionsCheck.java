import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class TaintedPermissionsCheck {

        private static void doIt() {
        }

public static void main(String[] args) {
	String whatDoTheyWantToDo = args[0];
	Subject subject = SecurityUtils.getSubject();

	// BAD: permissions decision made using tainted data
	if(subject.isPermitted("domain:sublevel:" + whatDoTheyWantToDo))
		doIt();

	// GOOD: use fixed checks
        if(subject.isPermitted("domain:sublevel:whatTheMethodDoes"))
                doIt();
}
}
