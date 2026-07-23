import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImproperValidationOfArrayIndexCodeSpecified extends HttpServlet {

    private String[] productDescriptions = new String[]{"Chocolate bar", "Fizzy drink"};

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Search for products in productDescriptions that match the search term
        String searchTerm = request.getParameter("productSearchTerm");
        int foundProductID = -1;
        for (int i = 0; i < productDescriptions.length; i++) {
            if (productDescriptions[i].contains(searchTerm)) {
                // Found matching product
                foundProductID = i;
                break;
            }
        }

        // BAD We may not have found a product in which case the index would be -1
        response.getWriter().write(productDescriptions[foundProductID]);

        if (foundProductID >= 0) {
            // GOOD We have checked we found a product first
            response.getWriter().write(productDescriptions[foundProductID]);
        } else {
            response.getWriter().write("No product found");
        }
    }
}