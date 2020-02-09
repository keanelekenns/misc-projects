package ca.uvic.seng330.assn3.models.devices;

public abstract class Device {

  private final String aName;
  private Status aStatus = Status.OFFLINE;
  private DeviceType aDeviceType;

  public Device(String pName, DeviceType pType) {
    aName = pName;
    aDeviceType = pType;
  }

  public Device(Device pDevice) {
    aName = pDevice.aName;
    aStatus = pDevice.aStatus;
    aDeviceType = pDevice.aDeviceType;
  }

  /**
   * Returns a Status enumeration of the device. Possible values include OFF, ATTENTION_NEEDED, and
   * NORMAL.
   */
  public Status getStatus() {
    synchronized (this) {
      return aStatus;
    }
  }

  /**
   * Changes the status of the device to one of the three values of the Status enumeration (ie. OFF,
   * ATTENTION_NEEDED, NORMAL).
   *
   * @param pStatus
   */
  public void setStatus(Status pStatus) {
    synchronized (this) {
      aStatus = pStatus;
    }
  }

  /** @return the name for this device */
  public String getName() {
    return aName;
  }

  public DeviceType getDeviceType() {
    return aDeviceType;
  }

  /** Initializes (or turns on) a device, whatever that may look like for any specific device. */
  public abstract void powerOnDevice();

  /** Powers down a device, whatever that may look like for any specific device. */
  public abstract void powerOffDevice();

  /** @return a copy of the device */
  public abstract Device copyDevice();
}
