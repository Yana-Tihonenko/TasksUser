package de.ait.listtask.validation.validators;

import de.ait.listtask.dto.task.NewTaskDto;
import de.ait.listtask.exception.RestException;
import de.ait.listtask.validation.constraints.FinishDateLessThanStartDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;


public class FinishDateAndStarDateValidator implements ConstraintValidator<FinishDateLessThanStartDate, NewTaskDto> {

  @Autowired
  private MessageSource messageSource;

  @Override
  public boolean isValid(NewTaskDto newTaskDto, ConstraintValidatorContext constraintValidatorContext) {
    LocalDate startDate = null;
    LocalDate finishDate = null;
    try {
      startDate = LocalDate.parse(newTaskDto.getStartDate());
    } catch (DateTimeParseException e) {
      throw new RestException("101",
          startDate.toString(),
          HttpStatus.BAD_REQUEST, messageSource);
    }
    try {
      finishDate = LocalDate.parse(newTaskDto.getFinishDate());

    } catch (DateTimeParseException e) {
      throw new RestException("101",
          finishDate.toString(),
          HttpStatus.BAD_REQUEST, messageSource);
    }
    return startDate.isBefore(finishDate);


  }
}
