package com.example.vulnapp;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.Entry;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Sets up the in-memory backends used by some of the vulnerable endpoints:
 * an H2 database (SQL injection) and an UnboundID in-memory LDAP server (LDAP injection).
 *
 * <p>None of the code here changes the vulnerabilities; it only provides realistic, running
 * infrastructure so the sinks are genuinely reachable over the network.</p>
 */
public final class Backends {

    public static final String JDBC_URL = "jdbc:h2:mem:vulnapp;DB_CLOSE_DELAY=-1";
    public static final int LDAP_PORT = 10389;

    private static volatile boolean dbReady = false;
    private static volatile InMemoryDirectoryServer ldapServer;
    private static File staticDir;

    private Backends() {
    }

    public static synchronized void initDatabase() throws Exception {
        if (dbReady) {
            return;
        }
        try (Connection c = DriverManager.getConnection(JDBC_URL, "sa", "");
             Statement st = c.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS PRODUCT ("
                    + "ITEM VARCHAR(255), PRICE INT, ITEM_CATEGORY VARCHAR(255))");
            st.execute("DELETE FROM PRODUCT");
            st.execute("INSERT INTO PRODUCT VALUES ('Widget', 10, 'tools')");
            st.execute("INSERT INTO PRODUCT VALUES ('Gadget', 25, 'tools')");
            st.execute("INSERT INTO PRODUCT VALUES ('Secret', 999, 'classified')");
        }
        dbReady = true;
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(JDBC_URL, "sa", "");
    }

    public static synchronized void startLdapServer() throws Exception {
        if (ldapServer != null) {
            return;
        }
        InMemoryDirectoryServerConfig config =
                new InMemoryDirectoryServerConfig("o=example");
        config.setSchema(null); // relaxed schema for the demo
        config.setListenerConfigs(
                InMemoryListenerConfig.createLDAPConfig("default", LDAP_PORT));
        InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);
        ds.add(new Entry("dn: o=example", "objectClass: top", "objectClass: organization",
                "o: example"));
        ds.add(new Entry("dn: OU=People,o=example", "objectClass: top",
                "objectClass: organizationalUnit", "ou: People"));
        ds.add(new Entry("dn: cn=alice,OU=People,o=example", "objectClass: top",
                "objectClass: person", "cn: alice", "sn: Alice", "username: alice"));
        ds.startListening();
        ldapServer = ds;
    }

    public static synchronized String staticContentDir() throws Exception {
        if (staticDir == null) {
            File dir = Files.createTempDirectory("vulnapp-static").toFile();
            dir.deleteOnExit();
            Files.write(new File(dir, "welcome.html").toPath(),
                    "<html><body>Welcome page (safe forward target)</body></html>".getBytes());
            staticDir = dir;
        }
        return staticDir.getAbsolutePath();
    }
}

