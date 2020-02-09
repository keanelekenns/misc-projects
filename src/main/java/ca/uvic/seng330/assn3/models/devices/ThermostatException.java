package ca.uvic.seng330.assn3.models.devices;

public class ThermostatException extends Exception {

  public ThermostatException() {
    super();
  }

  public ThermostatException(String message) {
    super(message);
  }

  public ThermostatException(String message, Throwable cause) {
    super(message, cause);
  }

  public ThermostatException(Throwable cause) {
    super(cause);
  }
}
