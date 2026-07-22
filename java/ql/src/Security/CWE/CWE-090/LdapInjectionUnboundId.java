import javax.servlet.http.HttpServletRequest;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.DN;
import com.unboundid.ldap.sdk.RDN;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.SearchScope;

public class LdapInjectionUnboundId {
public void ldapQueryGood(HttpServletRequest request, LDAPConnection c) throws LDAPException {
  String organizationName = request.getParameter("organization_name");
  String username = request.getParameter("username");

  // GOOD: Organization name is encoded before being used in DN
  DN safeDn = new DN(new RDN("OU", "People"), new RDN("O", organizationName));

  // GOOD: User input is encoded before being used in search filter
  Filter safeFilter = Filter.createEqualityFilter("username", username);
  
  c.search(safeDn.toString(), SearchScope.ONE, safeFilter);
}
}
