package ee.ria.datatransformationmodule.data;

import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = UuidValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RUNTIME)
public @interface ValidUuid {

    String message() default "{invalid.uuid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
