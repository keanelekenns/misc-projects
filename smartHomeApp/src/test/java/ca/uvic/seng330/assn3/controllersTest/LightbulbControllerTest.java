package ca.uvic.seng330.assn3.controllersTest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import ca.uvic.seng330.assn3.controllers.ActivityLogController;
import ca.uvic.seng330.assn3.controllers.LightbulbController;
import ca.uvic.seng330.assn3.controllers.RegistrationException;
import ca.uvic.seng330.assn3.models.CurrentUser;
import ca.uvic.seng330.assn3.models.devices.Lightbulb;
import ca.uvic.seng330.assn3.models.devices.Status;
import ca.uvic.seng330.assn3.views.LightbulbView;
import javafx.embed.swing.JFXPanel;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class LightbulbControllerTest {
  private LightbulbController controller;

  @Mock private LightbulbView view;
  @Mock private CurrentUser currentUser;

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Before
  public void setUpData() {
    JFXPanel p = new JFXPanel(); // DO NOT REMOVE used to prevent javafx error
    view = mock(LightbulbView.class);
    currentUser = mock(CurrentUser.class);
    controller = new LightbulbController(view, currentUser, mock(ActivityLogController.class));
    view.setController(controller);
  }

  @Test
  public void shouldCreateLightbulb() {
    try {
      controller.createLightbulb("test");
      assertTrue(controller.getLightbulbs().containsKey("test"));
    } catch (RegistrationException e) {
      TestCase.fail();
    }
  }

  @Test
  public void shouldRemoveLightbulb() {
    try {
      controller.createLightbulb("test");
      controller.removeLightbulb("test");
      assertTrue(!controller.getLightbulbs().containsKey("test"));
    } catch (RegistrationException e) {
      TestCase.fail();
    }
  }

  @Test
  public void shouldThrowRegistrationExceptionIfAlreadyRegistered() {
    try {
      controller.createLightbulb("test");
      controller.createLightbulb("test");
      TestCase.fail();
    } catch (RegistrationException e) {
      assertTrue("Lightbulb name \"test\" has already been used.".equals(e.getMessage()));
    }
  }

  @Test
  public void shouldThrowRegistrationExceptionIfNotRegistered() {
    try {
      controller.removeLightbulb("fake");
      TestCase.fail();
    } catch (RegistrationException e) {
      assertTrue("Lightbulb name \"fake\" was not in the system.".equals(e.getMessage()));
    }
  }

  @Test
  public void shouldSetLightbulbToOnIfOffAndOffIfOn() {
    try {
      controller.createLightbulb("setOnOff");
    } catch (RegistrationException e) {
      e.printStackTrace();
    }
    controller.setLightbulbOnOff(controller.getLightbulbs().get("setOnOff"));
    assertTrue(controller.getLightbulbs().get("setOnOff").getStatus().equals(Status.FUNCTIONING));
    controller.setLightbulbOnOff(controller.getLightbulbs().get("setOnOff"));
    assertTrue(controller.getLightbulbs().get("setOnOff").getStatus().equals(Status.OFFLINE));
  }

  @Test
  public void shouldToggleLightbulbIfItIsFunctioning() {
    try {
      controller.createLightbulb("powerOn");
    } catch (RegistrationException e) {
      e.printStackTrace();
    }
    controller.setLightbulbOnOff(controller.getLightbulbs().get("powerOn"));
    controller.toggleLightbulb(controller.getLightbulbs().get("powerOn"));
    assertTrue(controller.getLightbulbs().get("powerOn").getCondition() == true);
  }

  @Test
  public void shouldThrowErrorIfTryToggleWhenLightbulbOff() {
    try {
      controller.createLightbulb("off");
    } catch (RegistrationException e) {
      e.printStackTrace();
    }
    controller.toggleLightbulb(controller.getLightbulbs().get("off"));
    verify(view, times(1)).setWarningText("Lightbulb with name: \"off\" is not powered on!");
  }

  @Test
  public void shouldTurnOnAllLights() {
    controller.turnLightsOn();
    for (Lightbulb l : controller.getLightbulbs().values()) {
      assertTrue(l.getStatus().equals(Status.FUNCTIONING) && l.getCondition() == true);
    }
  }

  @Test
  public void shouldTurnOffAllLights() {
    controller.turnLightsOn();
    controller.turnLightsOff();
    for (Lightbulb l : controller.getLightbulbs().values()) {
      assertTrue(l.getStatus().equals(Status.FUNCTIONING) && l.getCondition() == false);
    }
  }
}
