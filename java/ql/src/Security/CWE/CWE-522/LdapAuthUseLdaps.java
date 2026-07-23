import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class LdapAuthUseLdaps {
    void connect(String ldapUserName, String password) throws NamingException {
// GOOD: LDAP connection using LDAPS
String ldapUrl = "ldaps://ad.your-server.com:636";
Hashtable<String, String> environment = new Hashtable<String, String>();
environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
environment.put(Context.PROVIDER_URL, ldapUrl);
environment.put(Context.REFERRAL, "follow");
environment.put(Context.SECURITY_AUTHENTICATION, "simple");
environment.put(Context.SECURITY_PRINCIPAL, ldapUserName);
environment.put(Context.SECURITY_CREDENTIALS, password);
DirContext dirContext = new InitialDirContext(environment);
    }
}
