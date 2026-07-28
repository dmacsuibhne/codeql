package com.example.vulnapp;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.example.vulnapp.servlets.CWE_502_UnsafeDeserializationBad;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Stream;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
/**
 * End-to-end tests: one exercise per vulnerable class (84 total). Each test drives a real HTTP
 * request through the running Jetty server so the vulnerable code path is genuinely executed. The
 * inputs are benign markers, NOT working exploit payloads.
 */
public class EndToEndTest {
    private static Server server;
    private static int port;
    private static HttpClient client;
    private static String base;
    @BeforeAll
    static void startServer() throws Exception {
        try (ServerSocket s = new ServerSocket(0)) {
            port = s.getLocalPort();
        }
        server = Main.createServer(port);
        server.start();
        client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NEVER).build();
        base = "http://localhost:" + port;
    }
    @AfterAll
    static void stopServer() throws Exception {
        if (server != null) {
            server.stop();
        }
    }
    private HttpResponse<String> get(String path) throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create(base + path)).GET().build(),
                HttpResponse.BodyHandlers.ofString());
    }
    // ---- Parameterized coverage of the GET endpoints that should return HTTP 200 ----
    static Stream<Arguments> getEndpoints() {
        return Stream.of(
                Arguments.of("/api-taint-step?user_id=1"),
                Arguments.of("/zipslip?entryName=../evil.txt"),
                Arguments.of("/partialpath?path=../x"),
                Arguments.of("/jndi?name=rmi://localhost/x"),
                Arguments.of("/exec?script=true"),
                Arguments.of("/exec-relative"),
                Arguments.of("/exec-env-name?attribute=X&value=Y"),
                Arguments.of("/exec-env-value?path=/tmp"),
                Arguments.of("/exec-unescaped?latlonCoords=1"),
                Arguments.of("/xss?page=MARKER"),
                Arguments.of("/sql?category=tools"),
                Arguments.of("/sql-concat?category=tools"),
                Arguments.of("/sql-jpa?category=tools"),
                Arguments.of("/ldap?organization_name=example&username=alice"),
                Arguments.of("/groovy?script=2*3"),
                Arguments.of("/beanvalidation?value=marker"),
                Arguments.of("/mvel?expression=2*3"),
                Arguments.of("/spel-safer?expression=2*3"),
                Arguments.of("/ssti?code=hello"),
                Arguments.of("/jexl?input=2*3"),
                Arguments.of("/spel?expression=2*3"),
                Arguments.of("/cookie-httponly?jwt_token=abc"),
                Arguments.of("/netty-request-split?uri=/x"),
                Arguments.of("/netty-response-split"),
                Arguments.of("/split?name=marker"),
                Arguments.of("/log?username=Guest"),
                Arguments.of("/static-iv"),
                Arguments.of("/array-construct?numberOfItems=1"),
                Arguments.of("/array-index-code?productSearchTerm=Choco"),
                Arguments.of("/array-index?productID=0"),
                Arguments.of("/format-string?cardSecurityCode=123"),
                Arguments.of("/arith-tainted?data=5"),
                Arguments.of("/arith-uncontrolled"),
                Arguments.of("/arith-extreme"),
                Arguments.of("/comparison-wider"),
                Arguments.of("/actuators"),
                Arguments.of("/tempdir"),
                Arguments.of("/stacktrace"),
                Arguments.of("/unsafe-cert-trust"),
                Arguments.of("/insecure-trustmanager"),
                Arguments.of("/javamail"),
                Arguments.of("/simplemail"),
                Arguments.of("/hostname-verify"),
                Arguments.of("/cleartext-storage?user=bob"),
                Arguments.of("/https-urls"),
                Arguments.of("/use-ssl"),
                Arguments.of("/ssl-socket-factories"),
                Arguments.of("/key-size"),
                Arguments.of("/broken-crypto?input=secret"),
                Arguments.of("/insecure-random"),
                Arguments.of("/predictable-seed"),
                Arguments.of("/jhipster-prng"),
                Arguments.of("/jwt?token=abc.def.ghi"),
                Arguments.of("/csrf-spring"),
                Arguments.of("/csrf-stapler"),
                Arguments.of("/spring-csrf"),
                Arguments.of("/toctou"),
                Arguments.of("/socket-auth-race?username=bob"),
                Arguments.of("/trust-boundary?username=bob"),
                Arguments.of("/basic-auth?username=u&password=p"),
                Arguments.of("/ldap-auth?username=u&password=p"),
                Arguments.of("/sensitive-log?password=secret"),
                Arguments.of("/forward?target=/welcome.html"),
                Arguments.of("/insecure-cookie"),
                Arguments.of("/xpath?user=aaa&pass=pass1"),
                Arguments.of("/numeric-cast?data=5"),
                Arguments.of("/regex?regex=a.*&input=abc"),
                Arguments.of("/rsa-no-oaep"),
                Arguments.of("/hardcoded-aws"),
                Arguments.of("/hardcoded-creds"),
                Arguments.of("/conditional-bypass?admin=false"),
                Arguments.of("/tainted-permissions?action=read"),
                Arguments.of("/lock-order"),
                Arguments.of("/infinite-loop"),
                Arguments.of("/ognl?expression=1")
        );
    }
    @ParameterizedTest(name = "{0}")
    @MethodSource("getEndpoints")
    void getEndpointReachesSink(String path) throws Exception {
        HttpResponse<String> r = get(path);
        assertEquals(200, r.statusCode(), "expected 200 for " + path + " but body was: " + r.body());
    }
    // ---- Endpoints with non-200 or special handling ----
    @Test
    void xssReflectsInput() throws Exception {
        assertTrue(get("/xss?page=MARKER_XSS").body().contains("MARKER_XSS"));
    }

    @Test
    void arrayConstructCodeReachesSink() throws Exception {
        // The array size comes from new Random().nextInt(10); a value of 0 makes the vulnerable
        // items[0] access throw (HTTP 500). Either outcome proves the vulnerable code path ran.
        int status = get("/array-construct-code").statusCode();
        assertTrue(status == 200 || status == 500, "unexpected status " + status);
    }
    @Test
    void externalApiSinkReturns404() throws Exception {        assertEquals(404, get("/api-sink?page=x").statusCode());
    }
    @Test
    void sensitiveErrorReturns500() throws Exception {
        assertEquals(500, get("/sensitive-error?detail=marker").statusCode());
    }
    @Test
    void openRedirectUsesLocationHeader() throws Exception {
        HttpResponse<String> r = get("/redirect?target=http://example.org/marker");
        assertEquals(302, r.statusCode());
        assertEquals("http://example.org/marker", r.headers().firstValue("Location").orElse(""));
    }
    @Test
    void responseSplittingSetsCookie() throws Exception {
        HttpResponse<String> r = get("/split?name=marker");
        assertNotNull(r.headers().firstValue("Set-Cookie").orElse(null));
    }
    @Test
    void pathTraversalReadsSuppliedFile() throws Exception {
        File tmp = Files.createTempFile("vulnapp-e2e", ".txt").toFile();
        tmp.deleteOnExit();
        Files.write(tmp.toPath(), "FILE_CONTENT_MARKER".getBytes(StandardCharsets.UTF_8));
        HttpResponse<String> r = get("/file?filename=" + tmp.getAbsolutePath());
        assertTrue(r.body().contains("FILE_CONTENT_MARKER"));
    }
    @Test
    void xsltInjectionTransforms() throws Exception {
        String xslt = "<xsl:stylesheet version=\"1.0\" "
                + "xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">"
                + "<xsl:template match=\"/\">x</xsl:template></xsl:stylesheet>";
        HttpResponse<String> r = get("/xslt?xslt=" + URLEncoder.encode(xslt, "UTF-8"));
        assertEquals(200, r.statusCode(), r.body());
    }
    @Test
    void ssrfSendsOutboundRequest() throws Exception {
        HttpResponse<String> r = get("/ssrf?uri=" + URLEncoder.encode(base + "/insecure-cookie", "UTF-8"));
        assertEquals(200, r.statusCode());
        assertTrue(r.body().contains("sent"));
    }
    @Test
    void xxeParsesRequestBody() throws Exception {
        String xml = "<?xml version=\"1.0\"?><root>marker</root>";
        HttpResponse<String> r = client.send(
                HttpRequest.newBuilder(URI.create(base + "/xxe"))
                        .POST(HttpRequest.BodyPublishers.ofString(xml))
                        .header("Content-Type", "application/xml").build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, r.statusCode());
        assertTrue(r.body().contains("parsed"));
    }
    @Test
    void unsafeDeserializationReadsObject() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(new CWE_502_UnsafeDeserializationBad.MyObject(42));
        }
        HttpResponse<String> r = client.send(
                HttpRequest.newBuilder(URI.create(base + "/deserialize"))
                        .POST(HttpRequest.BodyPublishers.ofByteArray(bos.toByteArray()))
                        .header("Content-Type", "application/octet-stream").build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, r.statusCode());
        assertTrue(r.body().contains("deserialized"));
    }
}
