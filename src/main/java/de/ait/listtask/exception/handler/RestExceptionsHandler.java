package de.ait.listtask.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.ait.listtask.exception.RestException;
import de.ait.listtask.exception.dto.ErrorDTO;
import de.ait.listtask.validation.dto.ValidationErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice

public class RestExceptionsHandler {
  @ExceptionHandler(RestException.class)
  public ResponseEntity<ErrorDTO> handleUserNotFoundException(RestException ex) {
    return ResponseEntity
        .status(ex.getHttpCode())
        .body(ErrorDTO.builder()
            .errorCode(ex.getCode())
            .message(ex.getMessage())
            .rejectedValue(ex.getRejectedValue())
            .build());
  }
}
