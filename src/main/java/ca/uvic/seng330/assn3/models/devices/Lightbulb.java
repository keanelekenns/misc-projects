package ca.uvic.seng330.assn3.models.devices;

public class Lightbulb extends Device {

  private boolean lightOn = false;

  public Lightbulb(String pName) {
    super(pName, DeviceType.LIGHTBULB);
  }

  public Lightbulb(Lightbulb pLightbulb) {
    super(pLightbulb);
    lightOn = pLightbulb.lightOn;
  }

  public void toggle() throws LightbulbException {
    if (getStatus().equals(Status.FUNCTIONING)) {
      lightOn = !lightOn;
    } else {
      throw new LightbulbException("Lightbulb with name: \"" + getName() + "\" is not powered on!");
    }
  }

  public boolean getCondition() {
    return lightOn;
  }

  @Override
  public void powerOnDevice() {
    setStatus(Status.FUNCTIONING);
  }

  @Override
  public void powerOffDevice() {
    lightOn = false;
    setStatus(Status.OFFLINE);
  }

  @Override
  public Device copyDevice() {
    Device copyLightbulb = new Lightbulb(this);
    return copyLightbulb;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Name: '");
    sb.append(getName());
    sb.append("' Status: '");
    sb.append(getStatus());
    sb.append("' Light On: '");
    sb.append(getCondition());
    sb.append("'");
    return sb.toString();
  }
}
