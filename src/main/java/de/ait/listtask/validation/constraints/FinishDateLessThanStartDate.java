package de.ait.listtask.validation.constraints;

import de.ait.listtask.validation.validators.FinishDateAndStarDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FinishDateAndStarDateValidator.class)
public @interface FinishDateLessThanStartDate {
  String message() default "Start date cannot be later than end date";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
