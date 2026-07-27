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
import static java.lang.annotation.ElementType.FIELD;
/** CWE-094 insecure bean validation. Source: bean property. Sink: buildConstraintViolationWithTemplate(value). */
public class CWE_094_InsecureBeanValidation extends HttpServlet {
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
            // BAD: user-controlled value used directly as the message template (EL interpolation)
            constraintContext.buildConstraintViolationWithTemplate(value)
                    .addConstraintViolation().disableDefaultConstraintViolation();
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
