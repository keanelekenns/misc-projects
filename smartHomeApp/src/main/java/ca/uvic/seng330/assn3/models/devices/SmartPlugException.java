package ca.uvic.seng330.assn3.models.devices;

public class SmartPlugException extends Exception {
  public SmartPlugException() {
    super();
  }

  public SmartPlugException(String message) {
    super(message);
  }

  public SmartPlugException(String message, Throwable cause) {
    super(message, cause);
  }

  public SmartPlugException(Throwable cause) {
    super(cause);
  }
}
