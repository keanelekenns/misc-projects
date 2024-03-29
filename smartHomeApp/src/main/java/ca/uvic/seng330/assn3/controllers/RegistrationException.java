package ca.uvic.seng330.assn3.controllers;

public class RegistrationException extends Exception {
  public RegistrationException() {
    super();
  }

  public RegistrationException(String message) {
    super(message);
  }

  public RegistrationException(String message, Throwable cause) {
    super(message, cause);
  }

  public RegistrationException(Throwable cause) {
    super(cause);
  }
}
