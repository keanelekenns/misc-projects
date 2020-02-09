package ca.uvic.seng330.assn3.controllersTest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import ca.uvic.seng330.assn3.controllers.ActivityLogController;
import ca.uvic.seng330.assn3.controllers.RegistrationException;
import ca.uvic.seng330.assn3.controllers.SmartPlugController;
import ca.uvic.seng330.assn3.models.CurrentUser;
import ca.uvic.seng330.assn3.models.devices.Status;
import ca.uvic.seng330.assn3.views.SmartPlugView;
import javafx.embed.swing.JFXPanel;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class SmartPlugControllerTest {
  private SmartPlugController controller;

  @Mock private SmartPlugView view;
  @Mock private CurrentUser currentUser;

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Before
  public void setUpData() {
    JFXPanel p = new JFXPanel(); // DO NOT REMOVE used to prevent javafx error
    view = mock(SmartPlugView.class);
    currentUser = mock(CurrentUser.class);
    controller = new SmartPlugController(view, currentUser, mock(ActivityLogController.class));
    view.setController(controller);
  }

  @Test
  public void shouldCreateSmartPlug() {
    try {
      controller.createSmartPlug("test");
      assertTrue(controller.getSmartPlugs().containsKey("test"));
    } catch (RegistrationException e) {
      TestCase.fail();
    }
  }

  @Test
  public void shouldRemoveSmartPlug() {
    try {
      controller.createSmartPlug("test");
      controller.removeSmartPlug("test");
      assertTrue(!controller.getSmartPlugs().containsKey("test"));
    } catch (RegistrationException e) {
      TestCase.fail();
    }
  }

  @Test
  public void shouldThrowRegistrationExceptionIfAlreadyRegistered() {
    try {
      controller.createSmartPlug("test");
      controller.createSmartPlug("test");
      TestCase.fail();
    } catch (RegistrationException e) {
      assertTrue("SmartPlug name \"test\" has already been used.".equals(e.getMessage()));
    }
  }

  @Test
  public void shouldThrowRegistrationExceptionIfNotRegistered() {
    try {
      controller.removeSmartPlug("fake");
      TestCase.fail();
    } catch (RegistrationException e) {
      assertTrue("SmartPlug name \"fake\" was not in the system.".equals(e.getMessage()));
    }
  }

  @Test
  public void shouldSetSmartPlugToOnIfOffAndOffIfOn() {
    try {
      controller.createSmartPlug("setOnOff");
    } catch (RegistrationException e) {
      e.printStackTrace();
    }
    controller.setSmartPlugOnOff(controller.getSmartPlugs().get("setOnOff"));
    assertTrue(controller.getSmartPlugs().get("setOnOff").getStatus().equals(Status.FUNCTIONING));
    controller.setSmartPlugOnOff(controller.getSmartPlugs().get("setOnOff"));
    assertTrue(controller.getSmartPlugs().get("setOnOff").getStatus().equals(Status.OFFLINE));
  }

  @Test
  public void shouldToggleSmartPlugIfItIsFunctioning() {
    try {
      controller.createSmartPlug("powerOn");
    } catch (RegistrationException e) {
      e.printStackTrace();
    }
    controller.setSmartPlugOnOff(controller.getSmartPlugs().get("powerOn"));
    controller.toggleSmartPlug(controller.getSmartPlugs().get("powerOn"));
    assertTrue(controller.getSmartPlugs().get("powerOn").getCondition() == true);
  }

  @Test
  public void shouldThrowErrorIfTryToggleWhenSmartPlugOff() {
    try {
      controller.createSmartPlug("off");
    } catch (RegistrationException e) {
      e.printStackTrace();
    }
    controller.toggleSmartPlug(controller.getSmartPlugs().get("off"));
    verify(view, times(1)).setWarningText("SmartPlug with name: \"off\" is not powered on!");
  }
}
