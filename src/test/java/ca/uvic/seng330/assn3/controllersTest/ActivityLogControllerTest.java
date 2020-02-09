package ca.uvic.seng330.assn3.controllersTest;

import static org.junit.Assert.assertTrue;

import ca.uvic.seng330.assn3.SmartHomeControllerFactory;
import ca.uvic.seng330.assn3.controllers.ActivityLogController;
import ca.uvic.seng330.assn3.controllers.CameraController;
import ca.uvic.seng330.assn3.controllers.LightbulbController;
import ca.uvic.seng330.assn3.controllers.MainViewController;
import ca.uvic.seng330.assn3.controllers.SmartPlugController;
import ca.uvic.seng330.assn3.controllers.ThermostatController;
import ca.uvic.seng330.assn3.views.AuthenticationView;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.testfx.framework.junit.ApplicationTest;

public class ActivityLogControllerTest extends ApplicationTest {

  private ActivityLogController controller;
  private Stage primaryStage;
  private SmartHomeControllerFactory f;
  private CameraController c;
  private ThermostatController t;
  private SmartPlugController s;
  private LightbulbController l;
  @Mock private AuthenticationView view;

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

  public void resetData() {
    File oldLog = new File("ActivityLogs.json");
    oldLog.delete();
  }

  @AfterClass
  public static void restoreDataFiles() {
    File newLog = new File("TempActivityLogs.json");
    File oldLog = new File("ActivityLogs.json");
    oldLog.delete();
    newLog.renameTo(oldLog);
  }

  @BeforeClass
  public static void protectDataFiles() {
    File newLog = new File("TempActivityLogs.json");
    File oldLog = new File("ActivityLogs.json");
    oldLog.renameTo(newLog);

    FileWriter file;

    // write TEST json to file for test
    try {
      file = new FileWriter("ActivityLogs.json");
      file.write("[\"TEST\"]");
      file.close();
    } catch (IOException e) {

    }
  }

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    f = new SmartHomeControllerFactory();

    MainViewController mainViewController = f.getMainViewController();
    mainViewController.showAuthenticationView();

    controller = f.getActivityLogController();
    l = f.getLightbulbController();
    c = f.getCameraController();
    t = f.getThermostatController();
    s = f.getSmartPlugController();
    controller.setCameraController(c);
    controller.setLightbulbController(l);
    controller.setSmartPlugController(s);
    controller.setThermostatController(t);

    Scene scene = new Scene(mainViewController.getView(), 1200, 600);
    primaryStage.setScene(scene);
  }

  @Test
  public void shouldBePopulatedWithLogsFromFile() {
    assertTrue(controller.getView().getActivityLogs().get(0).equals("TEST"));
  }

  @Test
  public void shouldAppendInfoStringWhenInfoCalled() {
    controller.info("TEST");
    ObservableList<String> logs = controller.getView().getActivityLogs();
    String log = logs.get(logs.size() - 1);
    assertTrue(log.contains("INFO: user: \"NO CURRENT USER\" msg: TEST"));
  }

  @Test
  public void shouldAppendWarnStringWhenWarnCalled() {
    controller.warn("TEST");
    ObservableList<String> logs = controller.getView().getActivityLogs();
    String log = logs.get(logs.size() - 1);
    assertTrue(log.contains("WARN: user: \"NO CURRENT USER\" msg: TEST"));
  }

  @Test
  public void shouldAppendErrorStringWhenErrorCalled() {
    controller.error("TEST");
    ObservableList<String> logs = controller.getView().getActivityLogs();
    String log = logs.get(logs.size() - 1);
    assertTrue(log.contains("ERROR: user: \"NO CURRENT USER\" msg: TEST"));
  }
}
