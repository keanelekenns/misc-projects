package ca.uvic.seng330.assn3.models.devices;

import ca.uvic.seng330.assn3.controllers.ThermostatController;
import ca.uvic.seng330.assn3.models.devices.Temperature.Unit;
import java.text.DecimalFormat;

public class Thermostat extends Device {

  private Temperature aTemp;
  private ThermostatController controller;

  public Thermostat(String pName, ThermostatController controller) {
    super(pName, DeviceType.THERMOSTAT);
    this.controller = controller;
  }

  public Thermostat(Thermostat pThermostat) {
    super(pThermostat);
    aTemp = pThermostat.aTemp;
    this.controller = pThermostat.controller;
  }

  /**
   * Changes the Thermostat's temperature and throws an exception if the temperature is absurd (ie.
   * >1000 degrees Celsius or Fahrenheit).
   *
   * @param pTemp
   * @pre pTemp != null
   * @throws Temperature.ThermostatException
   */
  public void setTemp(Unit metric, Double temp) throws ThermostatException {
    if (!getStatus().equals(Status.FUNCTIONING)) {
      throw new ThermostatException("Thermostat with name: " + getName() + " isn't powered on!");
    } else if (temp >= Temperature.MAX_TEMP_FAHRENHEIT && metric.equals(Unit.FAHRENHEIT)) {
      throw new ThermostatException("Temperature reading is far too high.");
    } else if (temp >= Temperature.MAX_TEMP_CELSIUS && metric.equals(Unit.CELSIUS)) {
      throw new ThermostatException("Temperature reading is far too high.");
    } else if (metric.equals(Unit.FAHRENHEIT)) {
      aTemp = new Temperature(metric, temp);
    } else {
      aTemp = new Temperature(metric, temp);
    }
  }

  public Temperature getTemp() {
    return aTemp;
  }

  public void stimulateClimateChange() {
    double degree = (double) (Math.random() * 100);
    Unit metric = Unit.CELSIUS;
    if (getTemp() != null) {
      metric = getTemp().getUnit();
    }
    try {
      setTemp(metric, degree);
      controller.alert(this);
    } catch (ThermostatException e) {
    }
  }

  @Override
  public void powerOnDevice() {
    setStatus(Status.FUNCTIONING);
  }

  @Override
  public void powerOffDevice() {
    aTemp = null;
    setStatus(Status.OFFLINE);
  }

  @Override
  public Device copyDevice() {
    Device copyThermostat = new Thermostat(this);
    return copyThermostat;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Name: '");
    sb.append(getName());
    sb.append("' Status: '");
    sb.append(getStatus());
    sb.append("' Temperature: '");
    if (aTemp == null) {
      sb.append("N/A");
    } else {
      DecimalFormat df = new DecimalFormat("#.#");
      sb.append(df.format(getTemp().getDegrees()) + " " + getTemp().getUnit().toString());
    }
    sb.append("'");
    return sb.toString();
  }
}
