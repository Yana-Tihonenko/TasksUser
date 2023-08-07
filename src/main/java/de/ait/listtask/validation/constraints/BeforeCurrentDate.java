package de.ait.listtask.validation.constraints;

import de.ait.listtask.validation.validators.DateTaskValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateTaskValidator.class)
public @interface BeforeCurrentDate {
  String message() default "The date cannot be less than the current date";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
