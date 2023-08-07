package de.ait.listtask.validation.validators;

import de.ait.listtask.validation.constraints.BeforeCurrentDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateTaskValidator implements ConstraintValidator<BeforeCurrentDate, String> {
  @Override
  public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
    if (LocalDate.parse(date).isBefore(LocalDate.now())) {
      return false;
    }
    return true;
  }
}
