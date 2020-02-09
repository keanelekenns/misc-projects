package ca.uvic.seng330.assn3.models.devices;

public class LightbulbException extends Exception {
  public LightbulbException() {
    super();
  }

  public LightbulbException(String message) {
    super(message);
  }

  public LightbulbException(String message, Throwable cause) {
    super(message, cause);
  }

  public LightbulbException(Throwable cause) {
    super(cause);
  }
}
