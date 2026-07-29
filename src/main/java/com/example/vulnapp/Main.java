package com.example.vulnapp;
import com.example.vulnapp.servlets.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import javax.servlet.http.HttpServlet;
/**
 * Boots an embedded Jetty server exposing every intentionally vulnerable endpoint (one per class
 * from examplesVulnerable.txt).
 *
 * <p><strong>WARNING:</strong> This application is intentionally vulnerable and is provided for
 * security research and testing only. Do not deploy it to any environment reachable by untrusted
 * users.</p>
 */
public final class Main {
    public static final int DEFAULT_PORT = 8080;
    private Main() {
    }
    public static void main(String[] args) throws Exception {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        Server server = createServer(port);
        server.start();
        System.out.println("Vulnerable app started on http://localhost:" + port);
        server.join();
    }
    public static Server createServer(int port) throws Exception {
        Backends.initDatabase();
        Backends.startLdapServer();
        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setResourceBase(Backends.staticContentDir());
        // CWE-020
        add(context, new CWE_020_ExternalAPISinkExample(), "/api-sink");
        add(context, new CWE_020_ExternalAPITaintStepExample(), "/api-taint-step");
        // CWE-022
        add(context, new CWE_022_TaintedPath(), "/file");
        add(context, new CWE_022_ZipSlipBad(), "/zipslip");
        // CWE-023
        add(context, new CWE_023_PartialPathTraversalBad(), "/partialpath");
        // CWE-074
        add(context, new CWE_074_JndiInjection(), "/jndi");
        add(context, new CWE_074_XsltInjection(), "/xslt");
        // CWE-078
        add(context, new CWE_078_ExecTainted(), "/exec");
        add(context, new CWE_078_ExecRelative(), "/exec-relative");
        add(context, new CWE_078_ExecTaintedEnvironmentName(), "/exec-env-name");
        add(context, new CWE_078_ExecTaintedEnvironmentValue(), "/exec-env-value");
        add(context, new CWE_078_ExecUnescaped(), "/exec-unescaped");
        // CWE-079
        add(context, new CWE_079_XSS(), "/xss");
        // CWE-089
        add(context, new CWE_089_SqlTainted(), "/sql");
        add(context, new CWE_089_SqlConcatenated(), "/sql-concat");
        add(context, new CWE_089_SqlTaintedPersistence(), "/sql-jpa");
        // CWE-090
        add(context, new CWE_090_LdapInjectionJndi(), "/ldap");
        // CWE-094
        add(context, new CWE_094_GroovyInjectionBad(), "/groovy");
        add(context, new CWE_094_InsecureBeanValidation(), "/beanvalidation");
        add(context, new CWE_094_MvelExpressionEvaluation(), "/mvel");
        add(context, new CWE_094_SaferSpelExpressionEvaluation(), "/spel-safer");
        add(context, new CWE_094_SSTIBad(), "/ssti");
        add(context, new CWE_094_UnsafeJexlExpressionEvaluation(), "/jexl");
        add(context, new CWE_094_UnsafeSpelExpressionEvaluation(), "/spel");
        // CWE-1004
        add(context, new CWE_1004_SensitiveCookieNotHttpOnly(), "/cookie-httponly");
        // CWE-113
        add(context, new CWE_113_NettyRequestSplitting(), "/netty-request-split");
        add(context, new CWE_113_NettyResponseSplitting(), "/netty-response-split");
        add(context, new CWE_113_ResponseSplitting(), "/split");
        // CWE-117
        add(context, new CWE_117_LogInjectionBad(), "/log");
        // CWE-1204
        add(context, new CWE_1204_BadStaticInitializationVector(), "/static-iv");
        // CWE-129
        add(context, new CWE_129_ImproperValidationOfArrayConstructionCodeSpecified(), "/array-construct-code");
        add(context, new CWE_129_ImproperValidationOfArrayConstruction(), "/array-construct");
        add(context, new CWE_129_ImproperValidationOfArrayIndexCodeSpecified(), "/array-index-code");
        add(context, new CWE_129_ImproperValidationOfArrayIndex(), "/array-index");
        // CWE-134
        add(context, new CWE_134_ExternallyControlledFormatString(), "/format-string");
        // CWE-190
        add(context, new CWE_190_ArithmeticTainted(), "/arith-tainted");
        add(context, new CWE_190_ArithmeticUncontrolled(), "/arith-uncontrolled");
        add(context, new CWE_190_ArithmeticWithExtremeValues(), "/arith-extreme");
        add(context, new CWE_190_ComparisonWithWiderType(), "/comparison-wider");
        // CWE-200
        add(context, new CWE_200_SpringBootActuators(), "/actuators");
        add(context, new CWE_200_TempDirUsageVulnerable(), "/tempdir");
        // CWE-209
        add(context, new CWE_209_SensitiveDataExposureThroughErrorMessage(), "/sensitive-error");
        add(context, new CWE_209_StackTraceExposure(), "/stacktrace");
        // CWE-273
        add(context, new CWE_273_UnsafeCertTrust(), "/unsafe-cert-trust");
        // CWE-295
        add(context, new CWE_295_InsecureTrustManager(), "/insecure-trustmanager");
        // CWE-297
        add(context, new CWE_297_JavaMail(), "/javamail");
        add(context, new CWE_297_SimpleMail(), "/simplemail");
        add(context, new CWE_297_UnsafeHostnameVerification(), "/hostname-verify");
        // CWE-312
        add(context, new CWE_312_CleartextStorage(), "/cleartext-storage");
        // CWE-319
        add(context, new CWE_319_HttpsUrls(), "/https-urls");
        add(context, new CWE_319_UseSSL(), "/use-ssl");
        add(context, new CWE_319_UseSSLSocketFactories(), "/ssl-socket-factories");
        // CWE-326
        add(context, new CWE_326_InsufficientKeySizeBad(), "/key-size");
        // CWE-327
        add(context, new CWE_327_BrokenCryptoAlgorithm(), "/broken-crypto");
        // CWE-330
        add(context, new CWE_330_InsecureRandomnessCookie(), "/insecure-random");
        // CWE-335
        add(context, new CWE_335_PredictableSeed(), "/predictable-seed");
        // CWE-338
        add(context, new CWE_338_JHipsterGeneratedPRNGVulnerable(), "/jhipster-prng");
        // CWE-347
        add(context, new CWE_347_MissingJWTSignatureCheck(), "/jwt");
        // CWE-352
        add(context, new CWE_352_CsrfUnprotectedRequestTypeBadSpring(), "/csrf-spring");
        add(context, new CWE_352_CsrfUnprotectedRequestTypeBadStapler(), "/csrf-stapler");
        add(context, new CWE_352_SpringCSRFProtection(), "/spring-csrf");
        // CWE-367
        add(context, new CWE_367_TOCTOURace(), "/toctou");
        // CWE-421
        add(context, new CWE_421_SocketAuthRace(), "/socket-auth-race");
        // CWE-501
        add(context, new CWE_501_TrustBoundaryVulnerable(), "/trust-boundary");
        // CWE-502
        add(context, new CWE_502_UnsafeDeserializationBad(), "/deserialize");
        // CWE-522
        add(context, new CWE_522_InsecureBasicAuth(), "/basic-auth");
        add(context, new CWE_522_LdapAuthUseLdap(), "/ldap-auth");
        // CWE-532
        add(context, new CWE_532_SensitiveInfoLog(), "/sensitive-log");
        // CWE-552
        add(context, new CWE_552_UrlForward(), "/forward");
        // CWE-601
        add(context, new CWE_601_UrlRedirect(), "/redirect");
        // CWE-611
        add(context, new CWE_611_XXEBad(), "/xxe");
        // CWE-614
        add(context, new CWE_614_InsecureCookie(), "/insecure-cookie");
        // CWE-643
        add(context, new CWE_643_XPathInjection(), "/xpath");
        // CWE-681
        add(context, new CWE_681_NumericCastTainted(), "/numeric-cast");
        // CWE-730
        add(context, new CWE_730_RegexInjection(), "/regex");
        // CWE-780
        add(context, new CWE_780_RsaWithoutOaep(), "/rsa-no-oaep");
        // CWE-798
        add(context, new CWE_798_HardcodedAWSCredentials(), "/hardcoded-aws");
        add(context, new CWE_798_HardcodedCredentialsApiCall(), "/hardcoded-creds");
        // CWE-807
        add(context, new CWE_807_ConditionalBypass(), "/conditional-bypass");
        add(context, new CWE_807_TaintedPermissionsCheck(), "/tainted-permissions");
        // CWE-833
        add(context, new CWE_833_LockOrderInconsistency(), "/lock-order");
        // CWE-835
        add(context, new CWE_835_InfiniteLoopBad(), "/infinite-loop");
        // CWE-917
        add(context, new CWE_917_OgnlInjection(), "/ognl");
        // CWE-918
        add(context, new CWE_918_RequestForgery(), "/ssrf");
        context.addServlet(new ServletHolder(new DefaultServlet()), "/");
        server.setHandler(context);
        return server;
    }
    private static void add(ServletContextHandler context, HttpServlet servlet, String path) {
        context.addServlet(new ServletHolder(servlet), path);
    }
}
