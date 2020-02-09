package ca.uvic.seng330.assn3.controllersTest;

import static org.junit.Assert.assertTrue;

import ca.uvic.seng330.assn3.SmartHomeControllerFactory;
import ca.uvic.seng330.assn3.controllers.AdminController;
import ca.uvic.seng330.assn3.controllers.CameraController;
import ca.uvic.seng330.assn3.controllers.LightbulbController;
import ca.uvic.seng330.assn3.controllers.MainViewController;
import ca.uvic.seng330.assn3.controllers.SmartPlugController;
import ca.uvic.seng330.assn3.controllers.ThermostatController;
import ca.uvic.seng330.assn3.controllers.UserController;
import ca.uvic.seng330.assn3.models.devices.DeviceType;
import ca.uvic.seng330.assn3.views.AdminView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.testfx.framework.junit.ApplicationTest;

public class AdminControllerTest extends ApplicationTest {
  private SmartHomeControllerFactory f;
  private CameraController c;
  private ThermostatController t;
  private SmartPlugController s;
  private LightbulbController l;
  private UserController userController;
  private AdminController controller;
  @Mock private AdminView view;
  private Stage primaryStage;

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.f = new SmartHomeControllerFactory();

    MainViewController mainViewController = f.getMainViewController();
    mainViewController.showAuthenticationView();

    controller = f.getAdminController();
    l = f.getLightbulbController();
    c = f.getCameraController();
    t = f.getThermostatController();
    s = f.getSmartPlugController();
    userController = f.getUserController();
    controller.setCameraController(c);
    controller.setLightbulbController(l);
    controller.setSmartPlugController(s);
    controller.setThermostatController(t);
    view = controller.getView();

    Scene scene = new Scene(mainViewController.getView(), 1200, 600);
    primaryStage.setScene(scene);
  }

  @Test
  public void shouldCreateCamera() {
    controller.createNewDevice("camera", DeviceType.CAMERA);
    assertTrue(c.getCameras().get("camera") != null);
  }

  @Test
  public void shouldCreateThermostat() {
    controller.createNewDevice("ther", DeviceType.THERMOSTAT);
    assertTrue(t.getThermostats().get("ther") != null);
  }

  @Test
  public void shouldCreateLightbulb() {
    controller.createNewDevice("bulb", DeviceType.LIGHTBULB);
    assertTrue(l.getLightbulbs().get("bulb") != null);
  }

  @Test
  public void shouldCreateSmartPlug() {
    controller.createNewDevice("smart", DeviceType.SMARTPLUG);
    assertTrue(s.getSmartPlugs().get("smart") != null);
  }

  @Test
  public void shouldNotCreateCameraIfNameUsed() {
    controller.createNewDevice("camera", DeviceType.CAMERA);
    int originalSize = c.getCameras().values().size();
    controller.createNewDevice("camera", DeviceType.CAMERA);
    assertTrue(c.getCameras().values().size() == originalSize);
  }

  @Test
  public void shouldNotCreateThermostatIfNameUsed() {
    controller.createNewDevice("ther", DeviceType.THERMOSTAT);
    int originalSize = t.getThermostats().values().size();
    controller.createNewDevice("ther", DeviceType.THERMOSTAT);
    assertTrue(t.getThermostats().values().size() == originalSize);
  }

  @Test
  public void shouldNotCreateLightbulbIfNameUsed() {
    controller.createNewDevice("bulb", DeviceType.LIGHTBULB);
    int originalSize = l.getLightbulbs().values().size();
    controller.createNewDevice("bulb", DeviceType.LIGHTBULB);
    assertTrue(l.getLightbulbs().values().size() == originalSize);
  }

  @Test
  public void shouldNotCreateSmartPlugIfNameUsed() {
    controller.createNewDevice("smart", DeviceType.SMARTPLUG);
    int originalSize = s.getSmartPlugs().values().size();
    controller.createNewDevice("smart", DeviceType.SMARTPLUG);
    assertTrue(s.getSmartPlugs().values().size() == originalSize);
  }

  @Test
  public void shouldRemoveCamera() {
    controller.createNewDevice("camera", DeviceType.CAMERA);
    controller.removeDevice("camera", DeviceType.CAMERA);
    assertTrue(c.getCameras().get("camera") == null);
  }

  @Test
  public void shouldRemoveSmartPlug() {
    controller.createNewDevice("smart", DeviceType.SMARTPLUG);
    controller.removeDevice("smart", DeviceType.SMARTPLUG);
    assertTrue(s.getSmartPlugs().get("smart") == null);
  }

  @Test
  public void shouldRemoveThermostat() {
    controller.createNewDevice("therm", DeviceType.THERMOSTAT);
    controller.removeDevice("therm", DeviceType.THERMOSTAT);
    assertTrue(t.getThermostats().get("therm") == null);
  }

  @Test
  public void shouldRemoveLightbulb() {
    controller.createNewDevice("bulb", DeviceType.LIGHTBULB);
    controller.removeDevice("bulb", DeviceType.LIGHTBULB);
    assertTrue(l.getLightbulbs().get("bulb") == null);
  }

  @Test
  public void shouldCreateNewUser() {
    controller.createNewUser("Dana", "1234", false);
    assertTrue(userController.getUsers().get("Dana") != null);
  }

  @Test
  public void shouldCreateAdminUser() {
    controller.createNewUser("admin", "1234", true);
    assertTrue(userController.getUsers().get("admin").isAdmin() == true);
  }

  @Test
  public void shouldCreateBasicUser() {
    controller.createNewUser("basic", "1234", false);
    assertTrue(userController.getUsers().get("basic").isAdmin() == false);
  }

  @Test
  public void shouldSetWarningIfNameIsBlank() {
    controller.createNewUser("", "1234", false);
    assertTrue(view.getInfoLabel().equals("Invalid name or password, name:  password: 1234"));
  }

  @Test
  public void shouldSetWarningIfPasswordIsBlank() {
    controller.createNewUser("d", "", false);
    assertTrue(view.getInfoLabel().equals("Invalid name or password, name: d password: "));
  }

  @Test
  public void shouldSetWarningIfNameAlreadyInUse() {
    controller.createNewUser("d", "1234", false);
    controller.createNewUser("d", "1234", false);
    assertTrue(view.getInfoLabel().equals("User name: d, already exists."));
  }
}
