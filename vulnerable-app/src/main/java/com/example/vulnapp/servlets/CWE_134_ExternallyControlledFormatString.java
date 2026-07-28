package com.example.vulnapp.servlets;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** CWE-134: externally controlled format string. Source: "cardSecurityCode". Sink: System.out.format(csc + ...). */
public class CWE_134_ExternallyControlledFormatString extends HttpServlet {

    private boolean notValid(String value) {
        return true;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Calendar expirationDate = new GregorianCalendar(2017, GregorianCalendar.SEPTEMBER, 1);
        String cardSecurityCode = request.getParameter("cardSecurityCode");
        if (notValid(cardSecurityCode)) {
            // BAD: user provided value is included in the format string
            System.out.format(cardSecurityCode +
                    " is not the right value. Hint: the card expires in %1$ty.", expirationDate);

            // GOOD: %s is used to include the user-provided cardSecurityCode in the output
            System.out.format("%s is not the right value. Hint: the card expires in %2$ty.",
                    cardSecurityCode,
                    expirationDate);
        }
        response.getWriter().print("formatted");
    }
}
