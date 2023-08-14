package de.ait.listtask.validation.handler;

import de.ait.listtask.validation.dto.ValidationErrorDto;
import de.ait.listtask.validation.dto.ValidationErrorsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ValidationExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorsDto> handleException(MethodArgumentNotValidException e) {
    // собираем список всех ошибок в JSON-виде
    // собираем список всех ошибок в JSON-виде
    List<ValidationErrorDto> validationErrors = e.getBindingResult().getAllErrors().stream() // пробегаем все ошибки с помощью stream
        .map(error -> { // собираем информацию об ошибке в формате JSON
          ValidationErrorDto errorDto = ValidationErrorDto.builder()
              .message(error.getDefaultMessage())
              .build();

          if (error instanceof FieldError fieldError) { // если это ошибка, связанная со значением конкретного поля
            errorDto.setField(fieldError.getField()); // указываем поле

            if (fieldError.getRejectedValue() != null) { // если пользователь ввел значение, которое не нравится валидатору
              errorDto.setRejectedValue(fieldError.getRejectedValue().toString()); // то добавим это значение в ответ
            }
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

}
