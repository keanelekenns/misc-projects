package ca.uvic.seng330.assn3.modelsTest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import ca.uvic.seng330.assn3.controllers.ThermostatController;
import ca.uvic.seng330.assn3.models.devices.Temperature;
import ca.uvic.seng330.assn3.models.devices.Temperature.Unit;
import ca.uvic.seng330.assn3.models.devices.Thermostat;
import ca.uvic.seng330.assn3.models.devices.ThermostatException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class ThermostatTest {
  private Thermostat t;
  @Mock private ThermostatController controller;

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Before
  public void setUpData() {
    t = new Thermostat("t", mock(ThermostatController.class));
  }

  @Test
  public void shouldSetTempIfDeviceIsFunctioningCelsius() {
    t.powerOnDevice();
    Temperature temp = new Temperature(Unit.CELSIUS, 50.0);
    try {
      t.setTemp(Unit.CELSIUS, 50.0);
    } catch (ThermostatException e) {

    }
    assertTrue(t.getTemp().equals(temp));
  }

  @Test
  public void shouldSetTempIfDeviceIsFunctioningFahrenheit() {
    t.powerOnDevice();
    Temperature temp = new Temperature(Unit.FAHRENHEIT, 50.0);
    try {
      t.setTemp(Unit.FAHRENHEIT, 50.0);
    } catch (ThermostatException e) {

    }
    assertTrue(t.getTemp().equals(temp));
  }

  @Test
  public void shouldNotSetTempIfDeviceIsOff() {
    Temperature temp = new Temperature(Unit.CELSIUS, 50.0);
    try {
      t.setTemp(Unit.CELSIUS, 50.0);
      TestCase.fail();
    } catch (ThermostatException e) {
      assertTrue(e.getMessage().equals("Thermostat with name: t isn't powered on!"));
    }
  }

  @Test
  public void shouldNotSetTempIfTempTooHighCelsius() {
    try {
      t.powerOnDevice();
      t.setTemp(Unit.CELSIUS, 500000.0);
      TestCase.fail();
    } catch (ThermostatException e) {
      assertTrue(e.getMessage().equals("Temperature reading is far too high."));
    }
  }

  @Test
  public void shouldNotSetTempIfTempTooHighFahrenheit() {
    try {
      t.powerOnDevice();
      t.setTemp(Unit.FAHRENHEIT, 50000.0);
      TestCase.fail();
    } catch (ThermostatException e) {
      assertTrue(e.getMessage().equals("Temperature reading is far too high."));
    }
  }
}
