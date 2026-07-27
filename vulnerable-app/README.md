# Vulnerable App — runnable CWE demonstrator (all 84 classes)
> ⚠️ **DISCLAIMER — INTENTIONALLY VULNERABLE CODE.**
> This project deliberately contains security vulnerabilities. It exists **only** for security
> research, static-analysis tooling validation, and educational testing. **Do NOT deploy it**,
> expose it to any untrusted network, or run it against data/systems you care about.
This is a single, runnable Java web application (embedded **Jetty**, `javax.servlet`) built from the
84 vulnerable snippets listed in `examplesVulnerable.txt`. **Every class is exposed as its own
HTTP-invokable endpoint** and has at least one automated end-to-end test that drives a real HTTP
request through the running server so the vulnerable code path is genuinely executed. The untrusted
**source** (an HTTP request parameter/body) and the **exact sink** from each snippet are preserved —
nothing is sanitized, encoded, or otherwise mitigated.
## Requirements
- JDK 11+ (uses `java.net.http.HttpClient`)
- Maven 3.6+
## Build / Run / Test
```bash
cd vulnerable-app
mvn clean test                       # runs all end-to-end tests (86 tests covering 84 classes)
mvn -DskipTests package              # builds the runnable fat jar
java -jar target/vulnerable-app.jar 8080
```
Run a single test:
```bash
mvn test -Dtest=EndToEndTest
```
Example exploit demonstration (reflected XSS):
```bash
curl 'http://localhost:8080/xss?page=<script>alert(1)</script>'
# => The page "<script>alert(1)</script>" was not found.
```
## Dependencies & licenses (all open source)
Versions are aligned via the **Spring Boot 2.7.x dependencies BOM** (Apache-2.0), which keeps Spring,
Spring Security, Netty, Groovy, Hibernate/Validator, H2, Jetty, Log4j2, HttpClient and JUnit mutually
compatible (Spring Boot 2.7 still targets `javax.*`, matching the snippets).
| Dependency | Used by (CWE) | License |
|---|---|---|
| Eclipse Jetty (server/servlet) | embedded container | Apache-2.0 / EPL-2.0 |
| H2 Database | 089 SQLi, JPA | MPL-2.0 / EPL-1.0 |
| SLF4J + slf4j-simple | 117 log injection | MIT |
| Log4j2 (log4j-core/api) | 297 JavaMail, 532 | Apache-2.0 |
| UnboundID LDAP SDK | 090, 522 (in-memory LDAP) | Apache-2.0 |
| Apache Groovy | 094 Groovy | Apache-2.0 |
| MVEL2 | 094 MVEL | Apache-2.0 |
| Apache Commons JEXL3 | 094 JEXL | Apache-2.0 |
| Spring Expression (SpEL) | 094 SpEL | Apache-2.0 |
| Apache Velocity | 094 SSTI | Apache-2.0 |
| OGNL | 917 | Apache-2.0 |
| Netty (codec-http) | 113 Netty split | Apache-2.0 |
| JJWT (api/impl/jackson) | 347 | Apache-2.0 |
| Apache Shiro | 807 permissions | Apache-2.0 |
| Hibernate Validator + Glassfish EL | 094 bean validation | Apache-2.0 / EPL-2.0 |
| Hibernate ORM (JPA) | 089 JPQL | LGPL-2.1 |
| JavaMail (com.sun.mail) | 297 | CDDL-1.1 / GPL-2.0+CE |
| Apache Commons Email | 297 | Apache-2.0 |
| Apache Commons Lang3 | 338 | Apache-2.0 |
| Apache HttpClient | 522 basic auth | Apache-2.0 |
| RabbitMQ amqp-client | 273 | Apache-2.0 / MPL-2.0 / GPL-2.0 |
| AWS SDK core | 798 | Apache-2.0 |
| Spring Web / Security / Boot Actuator | 352, 200 | Apache-2.0 |
| Google Guava | 200 temp dir | Apache-2.0 |
| Stapler | 352 Stapler | BSD-3-Clause |
| JAX-RS API | 1004 | CDDL-1.1 / GPL-2.0 |
| JUnit 5 | tests | EPL-2.0 |
## Endpoint map (84 classes)
Every row maps an original class → its endpoint. Unless noted, endpoints are `GET` and reachable at
`http://localhost:8080<path>`. Inputs shown are illustrative markers, **not** working payloads. Each servlet class is named `CWE_<id>_<OriginalClass>` (e.g. `CWE_020_ExternalAPISinkExample`, file `src/main/java/com/example/vulnapp/servlets/CWE_020_ExternalAPISinkExample.java`) so it maps 1:1 to the original snippet in the table below.
| CWE | Original class | Endpoint | Example |
|---|---|---|---|
| 020 | ExternalAPISinkExample | `/api-sink` | `?page=x` (→404) |
| 020 | ExternalAPITaintStepExample | `/api-taint-step` | `?user_id=1` |
| 022 | TaintedPath | `/file` | `?filename=/etc/hostname` |
| 022 | ZipSlipBad | `/zipslip` | `?entryName=../evil.txt` |
| 023 | PartialPathTraversalBad | `/partialpath` | `?path=../x` |
| 074 | JndiInjection | `/jndi` | `?name=rmi://h/x` |
| 074 | XsltInjection | `/xslt` | `?xslt=<stylesheet>` |
| 078 | ExecTainted | `/exec` | `?script=true` |
| 078 | ExecRelative | `/exec-relative` | (none) |
| 078 | ExecTaintedEnvironmentName | `/exec-env-name` | `?attribute=X&value=Y` |
| 078 | ExecTaintedEnvironmentValue | `/exec-env-value` | `?path=/tmp` |
| 078 | ExecUnescaped | `/exec-unescaped` | `?latlonCoords=1` |
| 079 | XSS | `/xss` | `?page=<script>` |
| 089 | SqlTainted | `/sql` | `?category=tools` |
| 089 | SqlConcatenated | `/sql-concat` | `?category=tools` |
| 089 | SqlTaintedPersistence | `/sql-jpa` | `?category=tools` |
| 090 | LdapInjectionJndi | `/ldap` | `?organization_name=example&username=alice` |
| 094 | GroovyInjectionBad | `/groovy` | `?script=2*3` |
| 094 | InsecureBeanValidation | `/beanvalidation` | `?value=x` |
| 094 | MvelExpressionEvaluation | `/mvel` | `?expression=2*3` |
| 094 | SaferSpelExpressionEvaluation | `/spel-safer` | `?expression=2*3` |
| 094 | SSTIBad | `/ssti` | `?code=hello` |
| 094 | UnsafeJexlExpressionEvaluation | `/jexl` | `?input=2*3` |
| 094 | UnsafeSpelExpressionEvaluation | `/spel` | `?expression=2*3` |
| 1004 | SensitiveCookieNotHttpOnly | `/cookie-httponly` | `?jwt_token=abc` |
| 113 | NettyRequestSplitting | `/netty-request-split` | `?uri=/x` |
| 113 | NettyResponseSplitting | `/netty-response-split` | (none) |
| 113 | ResponseSplitting | `/split` | `?name=x` |
| 117 | LogInjectionBad | `/log` | `?username=Guest` |
| 1204 | BadStaticInitializationVector | `/static-iv` | (none) |
| 129 | ImproperValidationOfArrayConstructionCodeSpecified | `/array-construct-code` | (none) |
| 129 | ImproperValidationOfArrayConstruction | `/array-construct` | `?numberOfItems=1` |
| 129 | ImproperValidationOfArrayIndexCodeSpecified | `/array-index-code` | `?productSearchTerm=Choco` |
| 129 | ImproperValidationOfArrayIndex | `/array-index` | `?productID=0` |
| 134 | ExternallyControlledFormatString | `/format-string` | `?cardSecurityCode=123` |
| 190 | ArithmeticTainted | `/arith-tainted` | `?data=5` |
| 190 | ArithmeticUncontrolled | `/arith-uncontrolled` | (none) |
| 190 | ArithmeticWithExtremeValues | `/arith-extreme` | (none) |
| 190 | ComparisonWithWiderType | `/comparison-wider` | (none) |
| 200 | SpringBootActuators | `/actuators` | (none) |
| 200 | TempDirUsageVulnerable | `/tempdir` | (none) |
| 209 | SensitiveDataExposureThroughErrorMessage | `/sensitive-error` | `?detail=x` (→500) |
| 209 | StackTraceExposure | `/stacktrace` | (none) |
| 273 | UnsafeCertTrust | `/unsafe-cert-trust` | (none) |
| 295 | InsecureTrustManager | `/insecure-trustmanager` | (none) |
| 297 | JavaMail | `/javamail` | (none) |
| 297 | SimpleMail | `/simplemail` | (none) |
| 297 | UnsafeHostnameVerification | `/hostname-verify` | (none) |
| 312 | CleartextStorage | `/cleartext-storage` | `?user=bob` |
| 319 | HttpsUrls | `/https-urls` | (none) |
| 319 | UseSSL | `/use-ssl` | (none) |
| 319 | UseSSLSocketFactories | `/ssl-socket-factories` | (none) |
| 326 | InsufficientKeySizeBad | `/key-size` | (none) |
| 327 | BrokenCryptoAlgorithm | `/broken-crypto` | `?input=secret` |
| 330 | InsecureRandomnessCookie | `/insecure-random` | (none) |
| 335 | PredictableSeed | `/predictable-seed` | (none) |
| 338 | JHipsterGeneratedPRNGVulnerable | `/jhipster-prng` | (none) |
| 347 | MissingJWTSignatureCheck | `/jwt` | `?token=a.b.c` |
| 352 | CsrfUnprotectedRequestTypeBadSpring | `/csrf-spring` | (none) |
| 352 | CsrfUnprotectedRequestTypeBadStapler | `/csrf-stapler` | (none) |
| 352 | SpringCSRFProtection | `/spring-csrf` | (none) |
| 367 | TOCTOURace | `/toctou` | (none) |
| 421 | SocketAuthRace | `/socket-auth-race` | `?username=bob` |
| 501 | TrustBoundaryVulnerable | `/trust-boundary` | `?username=bob` |
| 502 | UnsafeDeserializationBad | `/deserialize` | `POST` serialized object |
| 522 | InsecureBasicAuth | `/basic-auth` | `?username=u&password=p` |
| 522 | LdapAuthUseLdap | `/ldap-auth` | `?username=u&password=p` |
| 532 | SensitiveInfoLog | `/sensitive-log` | `?password=secret` |
| 552 | UrlForward | `/forward` | `?target=/welcome.html` |
| 601 | UrlRedirect | `/redirect` | `?target=http://evil` (→302) |
| 611 | XXEBad | `/xxe` | `POST` XML |
| 614 | InsecureCookie | `/insecure-cookie` | (none) |
| 643 | XPathInjection | `/xpath` | `?user=aaa&pass=pass1` |
| 681 | NumericCastTainted | `/numeric-cast` | `?data=5` |
| 730 | RegexInjection | `/regex` | `?regex=a.*&input=abc` |
| 780 | RsaWithoutOaep | `/rsa-no-oaep` | (none) |
| 798 | HardcodedAWSCredentials | `/hardcoded-aws` | (none) |
| 798 | HardcodedCredentialsApiCall | `/hardcoded-creds` | (none) |
| 807 | ConditionalBypass | `/conditional-bypass` | `?admin=false` |
| 807 | TaintedPermissionsCheck | `/tainted-permissions` | `?action=read` |
| 833 | LockOrderInconsistency | `/lock-order` | (none) |
| 835 | InfiniteLoopBad | `/infinite-loop` | (none) |
| 917 | OgnlInjection | `/ognl` | `?expression=1` |
| 918 | RequestForgery | `/ssrf` | `?uri=http://169.254.169.254/` |
## Faithfulness notes / environment-specific adaptations
The vulnerable **source→sink** of every class is preserved. A few endpoints required small,
documented accommodations that do **not** change the vulnerability:
- **CWE-190 ComparisonWithWiderType** (`/comparison-wider`) and **CWE-835 InfiniteLoopBad**
  (`/infinite-loop`): the exact buggy loop runs in a watchdog thread with a short join timeout so the
  endpoint returns; the incidental multi-gigabyte buffer in the CWE-190 snippet is reduced to avoid
  `OutOfMemoryError` (the defect is the short/long comparison, not the buffer).
- **CWE-421 SocketAuthRace** (`/socket-auth-race`): the blocking `accept()`/`write(secretData)` sink
  runs in a background thread that a local client connects to, so the write executes deterministically.
- **CWE-200 SpringBootActuators** (`/actuators`): uses Spring Security 5.7's `requestMatcher`, the
  equivalent of the snippet's 5.8 `securityMatcher`; the `permitAll()` misconfiguration is unchanged.
- Endpoints whose sink legitimately fails without external infrastructure (JNDI/RMI lookup, LDAP
  bind, outbound mail, MySQL connect, remote JWT) wrap the sink in `try/catch` and still return 200 —
  the vulnerable call is reached and executed; only the downstream network/IO fails.
## README summary (per requirements)
- **Vulnerability types / CWE IDs:** all classes from `examplesVulnerable.txt` (CWE-020 … CWE-918); see table.
- **Source:** an HTTP request parameter or request body, per endpoint.
- **Sink:** the exact vulnerable API call from the original snippet, per endpoint.
- **Root cause:** missing validation/encoding/authentication/etc. as in each original snippet — not fixed.
- **Automated tests:** `EndToEndTest` starts real Jetty and issues genuine HTTP requests to every
  endpoint (86 tests / 84 classes). Run with `mvn test`.
- **Disclaimer:** intentionally vulnerable code for research/testing only; **not for production**.
