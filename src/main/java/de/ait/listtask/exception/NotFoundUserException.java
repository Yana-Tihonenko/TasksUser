package de.ait.listtask.exception;

public class NotFoundUserException extends RuntimeException {
  public NotFoundUserException(Long message) {
    super(String.valueOf(message));
  }
}
