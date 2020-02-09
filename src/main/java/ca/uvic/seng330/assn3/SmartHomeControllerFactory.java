package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.controllers.ActivityLogController;
import ca.uvic.seng330.assn3.controllers.AdminController;
import ca.uvic.seng330.assn3.controllers.AuthenticationController;
import ca.uvic.seng330.assn3.controllers.BasicUserController;
import ca.uvic.seng330.assn3.controllers.CameraController;
import ca.uvic.seng330.assn3.controllers.LightbulbController;
import ca.uvic.seng330.assn3.controllers.MainViewController;
import ca.uvic.seng330.assn3.controllers.RegistrationException;
import ca.uvic.seng330.assn3.controllers.SmartPlugController;
import ca.uvic.seng330.assn3.controllers.ThermostatController;
import ca.uvic.seng330.assn3.controllers.UserController;
import ca.uvic.seng330.assn3.models.CurrentUser;
import ca.uvic.seng330.assn3.views.ActivityLogView;
import ca.uvic.seng330.assn3.views.AdminView;
import ca.uvic.seng330.assn3.views.AuthenticationView;
import ca.uvic.seng330.assn3.views.BasicUserView;
import ca.uvic.seng330.assn3.views.CameraView;
import ca.uvic.seng330.assn3.views.LightbulbView;
import ca.uvic.seng330.assn3.views.MainView;
import ca.uvic.seng330.assn3.views.SmartPlugView;
import ca.uvic.seng330.assn3.views.ThermostatView;

// stores references to all the controllers in the system
// ensures that only one of each controller is created and shared
// as well as ensures that each controller is setup correctly before use
public class SmartHomeControllerFactory {
  private CurrentUser currentUser;
  private UserController userController;
  private MainViewController mainViewController;
  private AuthenticationController authenticationController;
  private AdminController adminController;
  private BasicUserController basicUserController;
  private CameraController cameraController;
  private ThermostatController thermostatController;
  private SmartPlugController smartPlugController;
  private LightbulbController lightbulbController;
  private ActivityLogController activityLogController;

  public AuthenticationController getAuthenticationController() {
    if (authenticationController == null) {
      AuthenticationView view = new AuthenticationView();
      authenticationController =
          new AuthenticationController(
              view, getCurrentUser(), getUserController(), getActivityLogController());
      view.setController(authenticationController);
      authenticationController.createNewUser("admin", "password", true);
      authenticationController.createNewUser("basic", "password", false);
      view.setWarningText("");
    }
    return authenticationController;
  }

  public AdminController getAdminController() {
    if (adminController == null) {
      AdminView view = new AdminView();
      adminController =
          new AdminController(
              view, getCurrentUser(), getUserController(), getActivityLogController());
      view.setController(adminController);
      adminController.setCameraController(getCameraController());
      adminController.setThermostatController(getThermostatController());
      adminController.setSmartPlugController(getSmartPlugController());
      adminController.setLightbulbController(getLightbulbController());
    }
    return adminController;
  }

  public BasicUserController getBasicUserController() {
    if (basicUserController == null) {
      BasicUserView view = new BasicUserView();
      view.addView(getCameraController().getView());
      view.addView(getThermostatController().getView());
      view.addView(getSmartPlugController().getView());
      view.addView(getLightbulbController().getView());
      basicUserController =
          new BasicUserController(view, getCurrentUser(), getActivityLogController());
      view.setController(basicUserController);
      basicUserController.setCameraController(getCameraController());
      basicUserController.setThermostatController(getThermostatController());
      basicUserController.setSmartPlugController(getSmartPlugController());
      basicUserController.setLightbulbController(getLightbulbController());
    }
    return basicUserController;
  }

  public CameraController getCameraController() {
    if (cameraController == null) {
      CameraView cameraView = new CameraView();
      cameraController =
          new CameraController(cameraView, getCurrentUser(), getActivityLogController());
      cameraController.setLightbulbController(getLightbulbController());
      cameraView.setController(cameraController);
      try {
        cameraController.createCamera("Fujifilm");
        cameraController.createCamera("Canon");
        cameraController.createCamera("Nikon");
      } catch (RegistrationException e) {

      }
    }

    return cameraController;
  }

  public ThermostatController getThermostatController() {
    if (thermostatController == null) {
      ThermostatView thermostatView = new ThermostatView();
      thermostatController =
          new ThermostatController(thermostatView, getCurrentUser(), getActivityLogController());
      thermostatView.setController(thermostatController);
      try {
        thermostatController.createThermostat("Living room");
        thermostatController.createThermostat("Bed room");
        thermostatController.createThermostat("Dining room");
      } catch (RegistrationException e) {

      }
    }

    return thermostatController;
  }

  public SmartPlugController getSmartPlugController() {
    if (smartPlugController == null) {
      SmartPlugView smartPlugView = new SmartPlugView();
      smartPlugController =
          new SmartPlugController(smartPlugView, getCurrentUser(), getActivityLogController());
      smartPlugView.setController(smartPlugController);
      try {
        smartPlugController.createSmartPlug("Lamp plug");
        smartPlugController.createSmartPlug("Microwave plug");
        smartPlugController.createSmartPlug("Christmas tree plug");
      } catch (RegistrationException e) {

      }
    }

    return smartPlugController;
  }

  public LightbulbController getLightbulbController() {
    if (lightbulbController == null) {
      LightbulbView lightbulbView = new LightbulbView();
      lightbulbController =
          new LightbulbController(lightbulbView, getCurrentUser(), getActivityLogController());
      lightbulbView.setController(lightbulbController);
      try {
        lightbulbController.createLightbulb("Lamp");
        lightbulbController.createLightbulb("Garage");
        lightbulbController.createLightbulb("Black light");
      } catch (RegistrationException e) {

      }
    }

    return lightbulbController;
  }

  public MainViewController getMainViewController() {
    if (mainViewController == null) {
      MainView mainView = new MainView();
      AuthenticationController authenticationController = getAuthenticationController();
      AdminController adminController = getAdminController();
      BasicUserController basicUserController = getBasicUserController();
      mainViewController =
          new MainViewController(
              mainView,
              getActivityLogController(),
              getAuthenticationController(),
              getAdminController(),
              getBasicUserController());
      authenticationController.setMainViewController(mainViewController);
      basicUserController.setMainViewController(mainViewController);
      adminController.setMainViewController(mainViewController);
      mainView.setController(mainViewController);
    }
    return mainViewController;
  }

  public ActivityLogController getActivityLogController() {
    if (activityLogController == null) {
      ActivityLogView view = new ActivityLogView();
      activityLogController = new ActivityLogController(view, getCurrentUser());
      activityLogController.setCameraController(getCameraController());
      activityLogController.setThermostatController(getThermostatController());
      activityLogController.setSmartPlugController(getSmartPlugController());
      activityLogController.setLightbulbController(getLightbulbController());
    }
    return activityLogController;
  }

  public UserController getUserController() {
    if (userController == null) {
      this.userController = new UserController();
    }
    return this.userController;
  }

  public CurrentUser getCurrentUser() {
    if (currentUser == null) {
      this.currentUser = new CurrentUser();
    }
    return this.currentUser;
  }
}
