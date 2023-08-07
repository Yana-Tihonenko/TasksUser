package de.ait.listtask.validation.handler;

import de.ait.listtask.exception.NotFoundUserException;
import de.ait.listtask.validation.dto.ValidationErrorDto;
import de.ait.listtask.validation.dto.ValidationErrorsDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ValidationExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorsDto> handleException(MethodArgumentNotValidException e) {
    // собираем список всех ошибок в JSON-виде
    List<ValidationErrorDto> validationErrors = e.getBindingResult().getAllErrors().stream() // пробегаем все ошибки с помощью stream
        .filter(error -> error instanceof FieldError)
        .map(error -> (FieldError) error)
        .map(error -> {
          ValidationErrorDto errorDto = ValidationErrorDto.builder()
              .field(error.getField())
              .message(error.getDefaultMessage())
              .build();

          if (error.getRejectedValue() != null) {
            errorDto.setRejectedValue(error.getRejectedValue().toString());
          }

          return errorDto;
        })
        .collect(Collectors.toList());

    return ResponseEntity // отправляем
        .badRequest()
        .body(ValidationErrorsDto.builder()
            .errors(validationErrors)
            .build());
  }

  @ExceptionHandler(NotFoundUserException.class)
  public ResponseEntity<ValidationErrorDto> handleUserNotFoundException(NotFoundUserException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ValidationErrorDto.builder()
            .field("userId")
            .message("Not found user with id < " + ex.getMessage() + " >")
            .rejectedValue(ex.getMessage())
            .build());
  }

  @ExceptionHandler(DateTimeParseException.class)
  public ResponseEntity<ValidationErrorDto> handleDateTimeParseException(DateTimeParseException ex) {
    return ResponseEntity
        .badRequest()
        .body(ValidationErrorDto.builder()
            .message("Error date format")
            .rejectedValue(ex.getMessage())
            .build());
  }
}
