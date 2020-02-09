package ca.uvic.seng330.assn3.models.devices;

public class CameraException extends Exception {
  public CameraException() {
    super();
  }

  public CameraException(String message) {
    super(message);
  }

  public CameraException(String message, Throwable cause) {
    super(message, cause);
  }

  public CameraException(Throwable cause) {
    super(cause);
  }
}
