package com.example.vulnapp.servlets;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import javax.validation.Validation;
import javax.validation.Validator;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.lang.annotation.ElementType.FIELD;
/** CWE-094 insecure bean validation. Source: bean property. Sink: buildConstraintViolationWithTemplate(value). */
public class CWE_094_InsecureBeanValidation extends HttpServlet {

    public static class InterpolationHelper {
        public static final char BEGIN_TERM = '{';
        public static final char END_TERM = '}';
        public static final char EL_DESIGNATOR = '$';
        public static final char ESCAPE_CHARACTER = '\\';

        private static final Pattern ESCAPE_MESSAGE_PARAMETER_PATTERN = Pattern.compile("([\\" + ESCAPE_CHARACTER + BEGIN_TERM + END_TERM + EL_DESIGNATOR + "])");

        private InterpolationHelper() {
        }

        public static String escapeMessageParameter(String messageParameter) {
            if (messageParameter == null) {
                return null;
            }
            return ESCAPE_MESSAGE_PARAMETER_PATTERN.matcher(messageParameter).replaceAll(Matcher.quoteReplacement(String.valueOf(ESCAPE_CHARACTER)) + "$1");
        }
    }

    @Target({FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = InsecureValidator.class)
    public @interface NotSafe {
        String message() default "invalid";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }
    public static class InsecureValidator implements ConstraintValidator<NotSafe, String> {
        public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
            String value = object + " is invalid";

            // Bad: Bean properties (normally user-controlled) are passed directly to `buildConstraintViolationWithTemplate`
            constraintContext.buildConstraintViolationWithTemplate(value).addConstraintViolation().disableDefaultConstraintViolation();

            // Good: Bean properties (normally user-controlled) are escaped
            String escaped = InterpolationHelper.escapeMessageParameter(value);
            constraintContext.buildConstraintViolationWithTemplate(escaped).addConstraintViolation().disableDefaultConstraintViolation();

            // Good: Bean properties (normally user-controlled) are parameterized
            HibernateConstraintValidatorContext context = constraintContext.unwrap(HibernateConstraintValidatorContext.class);
            context.addMessageParameter("prop", object);
            context.buildConstraintViolationWithTemplate("{prop} is invalid").addConstraintViolation();
            return false;
        }
    }
    public static class Bean {
        @NotSafe
        public String value;
        public Bean(String value) { this.value = value; }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Bean>> violations = validator.validate(new Bean(request.getParameter("value")));
        response.getWriter().print("violations=" + violations.size());
    }
}
