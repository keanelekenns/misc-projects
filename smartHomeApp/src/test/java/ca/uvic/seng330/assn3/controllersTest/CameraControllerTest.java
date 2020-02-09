package ca.uvic.seng330.assn3.controllersTest;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import ca.uvic.seng330.assn3.controllers.ActivityLogController;
import ca.uvic.seng330.assn3.controllers.CameraController;
import ca.uvic.seng330.assn3.controllers.LightbulbController;
import ca.uvic.seng330.assn3.controllers.RegistrationException;
import ca.uvic.seng330.assn3.models.CurrentUser;
import ca.uvic.seng330.assn3.models.devices.Camera;
import ca.uvic.seng330.assn3.models.devices.Status;
import ca.uvic.seng330.assn3.views.CameraView;
import java.io.File;
import javafx.embed.swing.JFXPanel;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class CameraControllerTest {

  private CameraController controller;

  @Mock private CameraView view;
  @Mock private CurrentUser currentUser;
  @Mock private LightbulbController lbc;

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

  @BeforeClass
  public static void protectDataFiles() {
    File newCameras = new File("tempCameras.json");
    File newMap = new File("tempMap.json");
    File oldCameras = new File("Cameras.json");
    File oldMap = new File("CameraUserMap.json");
    oldCameras.renameTo(newCameras);
    oldMap.renameTo(newMap);
  }

  @After
  public void resetFiles() {
    File oldCameras = new File("Cameras.json");
    File oldMap = new File("CameraUserMap.json");
    oldCameras.delete();
    oldMap.delete();
  }

  @AfterClass
  public static void restoreDataFiles() {
    File newCameras = new File("tempCameras.json");
    File newMap = new File("tempMap.json");
    File oldCameras = new File("Cameras.json");
    File oldMap = new File("CameraUserMap.json");
    oldCameras.delete();
    oldMap.delete();
    newCameras.renameTo(oldCameras);
    newMap.renameTo(oldMap);
  }

  @Before
  public void setUpData() {
    JFXPanel p = new JFXPanel(); // DO NOT REMOVE used to prevent javafx error
    view = mock(CameraView.class);
    lbc = mock(LightbulbController.class);
    currentUser = mock(CurrentUser.class);
    controller = new CameraController(view, currentUser, mock(ActivityLogController.class));
    view.setController(controller);
    controller.setLightbulbController(lbc);
  }

  @Test
  public void shouldNotifyLightbulbControllerIfCameraDetectsPersonAndIsOn() {
    try {
      controller.createCamera("PersonCam");
    } catch (Exception e) {

    }
    Camera c = controller.getCameras().get("PersonCam");
    c.powerOnDevice();
    c.detectPerson();
    verify(lbc, times(1)).turnLightsOn();
  }

  @Test
  public void shouldNotifyLightbulbControllerIfCameraDetectsPersonLeavingAndIsOn() {
    try {
      controller.createCamera("PersonCam2");
    } catch (Exception e) {

    }
    Camera c = controller.getCameras().get("PersonCam2");
    c.powerOnDevice();
    c.detectPersonLeave();
    verify(lbc, times(1)).turnLightsOff();
  }

  @Test
  public void shouldStartVideoStreamWhenCameraTurnedOn() {
    try {
      controller.createCamera("VidCam");
    } catch (Exception e) {

    }
    Camera c = controller.getCameras().get("VidCam");
    controller.setCameraOnOff(c);
    c = controller.getCameras().get("VidCam");
    verify(view, times(1)).playVideoFromCamera(isA(Camera.class));
  }

  @Test
  public void shouldStopVideoStreamWhenCameraTurnedOff() {
    try {
      controller.createCamera("VidCam");
    } catch (Exception e) {

    }
    Camera c = controller.getCameras().get("VidCam");
    controller.setCameraOnOff(c);
    controller.setCameraOnOff(c);
    verify(view, times(1)).closeVideo();
  }

  @Test
  public void shouldCreateCamera() {
    try {
      controller.createCamera("test");
      assertTrue(controller.getCameras().containsKey("test"));
    } catch (RegistrationException e) {
      TestCase.fail();
    }
  }

  @Test
  public void shouldRemoveCamera() {
    try {
      controller.createCamera("test");
      controller.removeCamera("test");
      assertTrue(!controller.getCameras().containsKey("test"));
    } catch (RegistrationException e) {
      TestCase.fail();
    }
  }

  @Test
  public void shouldThrowRegistrationExceptionIfAlreadyRegistered() {
    try {
      controller.createCamera("test");
      controller.createCamera("test");
      TestCase.fail();
    } catch (RegistrationException e) {
      assertTrue("Camera name \"test\" has already been used.".equals(e.getMessage()));
    }
  }

  @Test
  public void shouldThrowRegistrationExceptionIfNotRegistered() {
    try {
      controller.removeCamera("fake");
      TestCase.fail();
    } catch (RegistrationException e) {
      assertTrue("Camera name \"fake\" was not in the system.".equals(e.getMessage()));
    }
  }

  @Test
  public void shouldSetCameraToOnIfOffAndOffIfOn() {
    try {
      controller.createCamera("setOnOff");
    } catch (RegistrationException e) {
      e.printStackTrace();
    }
    controller.setCameraOnOff(controller.getCameras().get("setOnOff"));
    assertTrue(controller.getCameras().get("setOnOff").getStatus().equals(Status.FUNCTIONING));
    controller.setCameraOnOff(controller.getCameras().get("setOnOff"));
    assertTrue(controller.getCameras().get("setOnOff").getStatus().equals(Status.OFFLINE));
  }

  @Test
  public void shouldSetCameraRecordIfCameraNotFull() {
    try {
      controller.createCamera("setRecord");
    } catch (RegistrationException e) {
      e.printStackTrace();
    }
    controller.setCameraOnOff(controller.getCameras().get("setRecord"));
    Camera t = controller.getCameras().get("setRecord");
    controller.setCameraRecord(t);
    assertTrue(controller.getCameras().get("setRecord").isRecording() == true);
  }

  @Test
  public void shouldThrowErrorIfTryRecordWhenCameraFull() {
    try {
      controller.createCamera("full");
    } catch (RegistrationException e) {
      e.printStackTrace();
    }
    controller.setCameraOnOff(controller.getCameras().get("full"));
    for (int i = 0; i < 9; i++) { // disksize taken up increases with each record for now
      controller.setCameraRecord(controller.getCameras().get("full"));
    }
    verify(view, times(1)).setWarningText("Camera with name: full memory is full.");
  }

  @Test
  public void shouldThrowErrorIfTryRecordWhenCameraOff() {
    try {
      controller.createCamera("off");
    } catch (RegistrationException e) {
      e.printStackTrace();
    }
    controller.setCameraRecord(controller.getCameras().get("off"));
    verify(view, times(1)).setWarningText("Camera with name: off isn't powered on!");
  }
}
