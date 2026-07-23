import static org.springframework.ldap.query.LdapQueryBuilder.query;

import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.web.bind.annotation.RequestParam;

public class LdapInjectionSpring {
    private LdapTemplate ldapTemplate;

    static class AttributeCheckAttributesMapper implements AttributesMapper<Object> {
        @Override
        public Object mapFromAttributes(Attributes attributes) {
            return null;
        }
    }

    public void ldapQueryGood(@RequestParam String organizationName, @RequestParam String username) {
        // GOOD: Organization name is encoded before being used in DN
        String safeDn = LdapNameBuilder.newInstance()
                .add("O", organizationName)
                .add("OU=People")
                .build().toString();

        // GOOD: User input is encoded before being used in search filter
        LdapQuery query = query()
                .base(safeDn)
                .where("username").is(username);

        ldapTemplate.search(query, new AttributeCheckAttributesMapper());
    }
}
