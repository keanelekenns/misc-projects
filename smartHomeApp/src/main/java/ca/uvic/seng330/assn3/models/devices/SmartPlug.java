package ca.uvic.seng330.assn3.models.devices;

public class SmartPlug extends Device {

  private boolean plugOn;

  public SmartPlug(String pName) {
    super(pName, DeviceType.SMARTPLUG);
    plugOn = false;
  }

  public SmartPlug(SmartPlug pSmartPlug) {
    super(pSmartPlug);
    plugOn = pSmartPlug.plugOn;
  }

  public void toggle() throws SmartPlugException {
    if (getStatus().equals(Status.FUNCTIONING)) {
      plugOn = !plugOn;
    } else
      throw new SmartPlugException("SmartPlug with name: \"" + getName() + "\" is not powered on!");
  }

  public boolean getCondition() {
    return plugOn;
  }

  @Override
  public Device copyDevice() {
    Device copySmartPlug = new SmartPlug(this);
    return copySmartPlug;
  }

  @Override
  public void powerOnDevice() {
    setStatus(Status.FUNCTIONING);
  }

  @Override
  public void powerOffDevice() {
    plugOn = false;
    setStatus(Status.OFFLINE);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Name: '");
    sb.append(getName());
    sb.append("' Status: '");
    sb.append(getStatus());
    sb.append("' Plug On: '");
    sb.append(getCondition());
    sb.append("'");
    return sb.toString();
  }
}
