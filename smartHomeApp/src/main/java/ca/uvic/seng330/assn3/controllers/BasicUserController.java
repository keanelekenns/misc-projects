package ca.uvic.seng330.assn3.controllers;

import ca.uvic.seng330.assn3.models.CurrentUser;
import ca.uvic.seng330.assn3.views.BasicUserView;
import javafx.scene.Node;

public class BasicUserController {
  private BasicUserView aView;
  private CurrentUser aCurrentUser;
  private MainViewController aMainViewController;
  private final ActivityLogController aLogger;
  private CameraController aCameraController;
  private ThermostatController aThermostatController;
  private SmartPlugController aSmartPlugController;
  private LightbulbController aLightbulbController;

  public BasicUserController(
      BasicUserView pView, CurrentUser pCurrentUser, ActivityLogController pLogger) {
    aView = pView;
    aLogger = pLogger;
    aCurrentUser = pCurrentUser;
  }

  public void setMainViewController(MainViewController pMainViewController) {
    aMainViewController = pMainViewController;
  }

  public void setCameraController(CameraController pCameraController) {
    aCameraController = pCameraController;
  }

  public void setThermostatController(ThermostatController pThermostatController) {
    aThermostatController = pThermostatController;
  }

  public void setSmartPlugController(SmartPlugController pSmartPlugController) {
    aSmartPlugController = pSmartPlugController;
  }

  public void setLightbulbController(LightbulbController pLightbulbController) {
    aLightbulbController = pLightbulbController;
  }

  public Node getView() {
    return aView;
  }

  public void logoutCurrentUser() {
    aCameraController.stopVideoPlayback();
    if (aCurrentUser.getCurrentUser() != null) {
      aLogger.info("Successfully logged out \"" + aCurrentUser.getCurrentUser().getName());
    }
    aCurrentUser.clearCurrentUser();
    aMainViewController.showAuthenticationView();
  }

  public void shutdownSystem() {
    aLightbulbController.shutDownDevices();
    aCameraController.shutDownDevices();
    aSmartPlugController.shutDownDevices();
    aThermostatController.shutDownDevices();
    updateDevicesInView();
    aView.setInfoLabel("System shut down safely.");
    aLogger.info("System shut down safely.");
  }

  public void updateDevicesInView() {
    aThermostatController.updateThermostatsInView();
    aCameraController.updateCamerasInView();
    aLightbulbController.updateLightbulbsInView();
    aSmartPlugController.updateSmartPlugsInView();
  }
}
