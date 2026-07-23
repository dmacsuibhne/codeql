import org.kohsuke.stapler.HttpRedirect;
import org.kohsuke.stapler.verb.GET;

public class CsrfUnprotectedRequestTypeBadStapler {

    HttpRedirect transfer() {
        return null;
    }

    HttpRedirect post() {
        return null;
    }

    // BAD - a safe HTTP request like GET should not be used for a state-changing action
    @GET
    public HttpRedirect doTransfer() {
        return transfer();
    }

    // BAD - no HTTP request type is specified, so safe HTTP requests are allowed
    public HttpRedirect doPost() {
        return post();
    }
}
