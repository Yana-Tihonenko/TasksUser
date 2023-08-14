package de.ait.listtask.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RestException extends RuntimeException {


  private String code;
  private String rejectedValue;
  private String message;
  private HttpStatus httpCode;

  public RestException(String code, String rejectedValue, HttpStatus httpCode,MessageSource messageSource) {

    this.code = code;
    this.rejectedValue = rejectedValue;
    this.message = messageSource.getMessage(code, null, Locale.getDefault());
    this.httpCode = httpCode;
  }


}
