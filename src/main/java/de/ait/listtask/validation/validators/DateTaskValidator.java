package de.ait.listtask.validation.validators;

import de.ait.listtask.exception.RestException;
import de.ait.listtask.validation.constraints.BeforeCurrentDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateTaskValidator implements ConstraintValidator<BeforeCurrentDate, String> {

  @Autowired
  private MessageSource messageSource;

  @Override
  public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
    try {
      return LocalDate.parse(date).isAfter(LocalDate.now());
    } catch (DateTimeParseException e) {
      throw new RestException("101",date,HttpStatus.BAD_REQUEST,messageSource);
    }
  }
}

