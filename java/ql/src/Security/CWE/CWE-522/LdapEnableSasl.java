import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class LdapEnableSasl {
    void connect(String ldapUserName, String password) throws NamingException {
// GOOD: LDAP is used but SASL authentication is enabled
        String ldapUrl = "ldap://ad.your-server.com:389";
        Hashtable<String, String> environment = new Hashtable<String, String>();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environment.put(Context.PROVIDER_URL, ldapUrl);
        environment.put(Context.REFERRAL, "follow");
        environment.put(Context.SECURITY_AUTHENTICATION, "DIGEST-MD5 GSSAPI");
        environment.put(Context.SECURITY_PRINCIPAL, ldapUserName);
        environment.put(Context.SECURITY_CREDENTIALS, password);
        DirContext dirContext = new InitialDirContext(environment);
    }
}
