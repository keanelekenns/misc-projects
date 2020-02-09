package ca.uvic.seng330.assn3.controllersTest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import ca.uvic.seng330.assn3.controllers.ActivityLogController;
import ca.uvic.seng330.assn3.controllers.RegistrationException;
import ca.uvic.seng330.assn3.controllers.ThermostatController;
import ca.uvic.seng330.assn3.models.CurrentUser;
import ca.uvic.seng330.assn3.models.devices.Status;
import ca.uvic.seng330.assn3.models.devices.Temperature;
import ca.uvic.seng330.assn3.models.devices.Temperature.Unit;
import ca.uvic.seng330.assn3.models.devices.Thermostat;
import ca.uvic.seng330.assn3.views.ThermostatView;
import javafx.embed.swing.JFXPanel;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class ThermostatControllerTest {
  private ThermostatController controller;

  @Mock private ThermostatView view;
  @Mock private CurrentUser currentUser;

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Before
  public void setUpData() {
    JFXPanel p = new JFXPanel(); // DO NOT REMOVE used to prevent javafx error
    view = mock(ThermostatView.class);
    currentUser = mock(CurrentUser.class);
    controller = new ThermostatController(view, currentUser, mock(ActivityLogController.class));
    view.setController(controller);
  }

  @Test
  public void shouldCreateThermostat() {
    try {
      controller.createThermostat("test");
      assertTrue(controller.getThermostats().containsKey("test"));
    } catch (RegistrationException e) {
      TestCase.fail();
    }
  }

  @Test
  public void shouldRemoveThermostat() {
    try {
      controller.createThermostat("test");
      controller.removeThermostat("test");
      assertTrue(!controller.getThermostats().containsKey("test"));
    } catch (RegistrationException e) {
      TestCase.fail();
    }
  }

  @Test
  public void shouldThrowRegistrationExceptionIfAlreadyRegistered() {
    try {
      controller.createThermostat("test");
      controller.createThermostat("test");
      TestCase.fail();
    } catch (RegistrationException e) {
      assertTrue("Thermostat name \"test\" has already been used.".equals(e.getMessage()));
    }
  }

  @Test
  public void shouldThrowRegistrationExceptionIfNotRegistered() {
    try {
      controller.removeThermostat("fake");
      TestCase.fail();
    } catch (RegistrationException e) {
      assertTrue("Thermostat name \"fake\" was not in the system.".equals(e.getMessage()));
    }
  }

  @Test
  public void shouldSetThermostatToOnIfOffAndOffIfOn() {
    try {
      controller.createThermostat("setOnOff");
    } catch (RegistrationException e) {
      e.printStackTrace();
    }
    controller.setThermostatOnOff(controller.getThermostats().get("setOnOff"));
    assertTrue(controller.getThermostats().get("setOnOff").getStatus().equals(Status.FUNCTIONING));
    controller.setThermostatOnOff(controller.getThermostats().get("setOnOff"));
    assertTrue(controller.getThermostats().get("setOnOff").getStatus().equals(Status.OFFLINE));
  }

  @Test
  public void shouldSetThermostatTempIfProperTempEntered() {
    try {
      controller.createThermostat("setTemp");
    } catch (RegistrationException e) {
      e.printStackTrace();
    }
    controller.setThermostatOnOff(controller.getThermostats().get("setTemp"));
    Thermostat t = controller.getThermostats().get("setTemp");
    controller.setThermostatTemp(t, Unit.CELSIUS, 20.0);
    Temperature temp = new Temperature(Unit.CELSIUS, 20.0);
    assertTrue(controller.getThermostats().get("setTemp").getTemp().equals(temp));
  }

  @Test
  public void shouldThrowErrorIfTempEnteredIsTooHigh() {
    try {
      controller.createThermostat("setTempTooHigh");
    } catch (RegistrationException e) {
      e.printStackTrace();
    }
    controller.setThermostatOnOff(controller.getThermostats().get("setTempTooHigh"));
    Thermostat t = controller.getThermostats().get("setTempTooHigh");
    controller.setThermostatTemp(t, Unit.CELSIUS, 2000.0);
    verify(view, times(1)).setWarningText("Temperature reading is far too high.");
  }

  @Test
  public void shouldConvertTempProperly() {
    try {
      controller.createThermostat("convertTemp");
    } catch (RegistrationException e) {
      e.printStackTrace();
    }
    controller.setThermostatOnOff(controller.getThermostats().get("convertTemp"));
    controller.setThermostatTemp(
        controller.getThermostats().get("convertTemp"), Unit.FAHRENHEIT, 32.0);
    controller.convertTemp(controller.getThermostats().get("convertTemp"));
    Temperature temp = new Temperature(Unit.CELSIUS, 0);
    assert (controller.getThermostats().get("convertTemp").getTemp().equals(temp));
    controller.convertTemp(controller.getThermostats().get("convertTemp"));
    temp = new Temperature(Unit.FAHRENHEIT, 32.0);
    assert (controller.getThermostats().get("convertTemp").getTemp().equals(temp));
  }
}
