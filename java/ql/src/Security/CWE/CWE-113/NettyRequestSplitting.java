import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

public class NettyRequestSplitting {
    private HttpVersion httpVersion;
    private HttpMethod method;
    private String uri;

    // BAD: Disables the internal request splitting verification
    private final DefaultHttpHeaders badHeaders = new DefaultHttpHeaders(false);

    // GOOD: Verifies headers passed don't contain CRLF characters
    private final DefaultHttpHeaders goodHeaders = new DefaultHttpHeaders();

    // BAD: Disables the internal request splitting verification
    private final DefaultHttpRequest badRequest = new DefaultHttpRequest(httpVersion, method, uri, false);

    // GOOD: Verifies headers passed don't contain CRLF characters
    private final DefaultHttpRequest goodResponse = new DefaultHttpRequest(httpVersion, method, uri);
}
