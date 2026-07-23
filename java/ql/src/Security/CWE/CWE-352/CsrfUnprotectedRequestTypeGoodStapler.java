import org.kohsuke.stapler.HttpRedirect;
import org.kohsuke.stapler.verb.POST;

public class CsrfUnprotectedRequestTypeGoodStapler {

    HttpRedirect transfer() {
        return null;
    }

    HttpRedirect post() {
        return null;
    }

    // GOOD - use POST
    @POST
    public HttpRedirect doTransfer() {
        return transfer();
    }

    // GOOD - use POST
    @POST
    public HttpRedirect doPost() {
        return post();
    }
}
