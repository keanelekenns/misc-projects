package ca.uvic.seng330.assn3.controllers;

import ca.uvic.seng330.assn3.models.CurrentUser;
import ca.uvic.seng330.assn3.models.User;
import ca.uvic.seng330.assn3.models.devices.DeviceType;
import ca.uvic.seng330.assn3.views.AdminView;
import javafx.collections.FXCollections;

public class AdminController {
  private final UserController aUserController;
  private final CurrentUser aCurrentUser;
  private final AdminView aView;
  private final ActivityLogController aLogger;
  private MainViewController aMainViewController;
  private CameraController aCameraController;
  private ThermostatController aThermostatController;
  private SmartPlugController aSmartPlugController;
  private LightbulbController aLightbulbController;

  public AdminController(
      AdminView pView,
      CurrentUser pCurrentUser,
      UserController pUserController,
      ActivityLogController pLogger) {
    aUserController = pUserController;
    aLogger = pLogger;
    aView = pView;
    aCurrentUser = pCurrentUser;

    aView.updateUsers(FXCollections.observableArrayList(aUserController.getUsers().keySet()));
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

  public void createNewUser(String pName, String pPassword, boolean isAdmin) {
    aView.setInfoLabel("");
    if (pName.isEmpty() || pPassword.isEmpty()) {
      String txt = "Invalid name or password, name: " + pName + " password: " + pPassword;
      aLogger.warn("Attempted to create user with invalid name or password.");
      aView.setInfoLabel(txt);
      return;
    }
    User newUser = new User(pName, pPassword, isAdmin);
    try {
      aUserController.registerUser(newUser);
      aView.updateUsers(FXCollections.observableArrayList(aUserController.getUsers().keySet()));
      aView.setInfoLabel("New user with name: " + pName + " created successfully");
      aLogger.info("Created new user: \"" + pName + "\" with Admin Controls.");
    } catch (RegistrationException e) {
      aLogger.warn("Failed to register new user:" + e.getMessage());
      aView.setInfoLabel(e.getMessage());
    }
  }

  public void removeUser(String pName, String pPassword) {
    aView.setInfoLabel("");
    try {
      aUserController.unregisterUser(pName, pPassword);
      aView.updateUsers(FXCollections.observableArrayList(aUserController.getUsers().keySet()));
      aView.setInfoLabel("User with name: " + pName + " removed successfully");
      aLogger.info("User with name: " + pName + " removed successfully with Admin Controls.");
      if (aCurrentUser.getCurrentUser().getName().equals(pName)) {
        aCurrentUser.clearCurrentUser();
        aMainViewController.showAuthenticationView();
      }
    } catch (RegistrationException e) {
      aLogger.warn(e.getMessage());
      aView.setInfoLabel("Failed to remove user:" + e.getMessage());
    }
  }

  public void setNotification(
      String pUserName, String pDeviceName, DeviceType pDeviceType, String pSelection) {
    aView.setInfoLabel("");
    if (!aUserController.getUsers().containsKey(pUserName)) {
      aView.setInfoLabel("User Name: " + pUserName + ", is not registered.");
      aLogger.error("Attempted to set notifications for invalid user name.");
      return;
    } else if (aUserController.getUsers().get(pUserName).isAdmin()) {
      aView.setInfoLabel("Admins always see all devices.");
    } else if (pSelection == "All") {
      setNotificationsAllForUser(pUserName);
    } else if (pSelection == "None") {
      setNotificationsNoneForUser(pUserName);
    } else { // custom
      setNotificationsCustomForUser(pUserName, pDeviceName, pDeviceType);
    }
  }

  private void setNotificationsAllForUser(String pUserName) {
    for (String therm : aThermostatController.getThermostats().keySet()) {
      aThermostatController.linkUserToThermostat(pUserName, therm);
    }
    for (String cam : aCameraController.getCameras().keySet()) {
      aCameraController.linkUserToCamera(pUserName, cam);
    }
    for (String bulb : aLightbulbController.getLightbulbs().keySet()) {
      aLightbulbController.linkUserToLightbulb(pUserName, bulb);
    }
    for (String plug : aSmartPlugController.getSmartPlugs().keySet()) {
      aSmartPlugController.linkUserToSmartPlug(pUserName, plug);
    }
    aView.setInfoLabel("User: " + pUserName + ", will now be notified by all devices.");
    aLogger.info("Notifications set to \"All\" for \"" + pUserName + "\".");
  }

  private void setNotificationsNoneForUser(String pUserName) {
    for (String therm : aThermostatController.getThermostats().keySet()) {
      aThermostatController.unlinkUserToThermostat(pUserName, therm);
    }
    for (String cam : aCameraController.getCameras().keySet()) {
      aCameraController.unlinkUserToCamera(pUserName, cam);
    }
    for (String bulb : aLightbulbController.getLightbulbs().keySet()) {
      aLightbulbController.unlinkUserToLightbulb(pUserName, bulb);
    }
    for (String plug : aSmartPlugController.getSmartPlugs().keySet()) {
      aSmartPlugController.unlinkUserToSmartPlug(pUserName, plug);
    }
    aView.setInfoLabel("User: " + pUserName + ", will no longer be notified by any devices.");
    aLogger.info("Notifications set to \"None\" for \"" + pUserName + "\".");
  }

  private void setNotificationsCustomForUser(
      String pUserName, String pDeviceName, DeviceType pDeviceType) {
    if (pDeviceType.equals(DeviceType.THERMOSTAT)) {
      if (!aThermostatController.getThermostats().containsKey(pDeviceName)) {
        aView.setInfoLabel("There is no thermostat named" + pDeviceName + ".");
        aLogger.warn("Attempted to set notifications for invalid device name.");
        return;
      }
      aThermostatController.linkUserToThermostat(pUserName, pDeviceName);
      aView.setInfoLabel("User: " + pUserName + " will be notified by" + pDeviceName);
      aLogger.info("User: " + pUserName + " will be notified by" + pDeviceName);
    } else if (pDeviceType.equals(DeviceType.CAMERA)) {
      if (!aCameraController.getCameras().containsKey(pDeviceName)) {
        aView.setInfoLabel("There is no camera named" + pDeviceName + ".");
        aLogger.warn("Attempted to set notifications for invalid device name.");
        return;
      }
      aCameraController.linkUserToCamera(pUserName, pDeviceName);
      aView.setInfoLabel("User: " + pUserName + " will be notified by" + pDeviceName);
      aLogger.info("User: " + pUserName + " will be notified by" + pDeviceName);
    } else if (pDeviceType.equals(DeviceType.LIGHTBULB)) {
      if (!aLightbulbController.getLightbulbs().containsKey(pDeviceName)) {
        aView.setInfoLabel("There is no lightbulb named" + pDeviceName + ".");
        aLogger.warn("Attempted to set notifications for invalid device name.");
        return;
      }
      aLightbulbController.linkUserToLightbulb(pUserName, pDeviceName);
      aView.setInfoLabel("User: " + pUserName + " will be notified by" + pDeviceName);
      aLogger.info("User: " + pUserName + " will be notified by" + pDeviceName);
    } else { // smartplug
      if (!aSmartPlugController.getSmartPlugs().containsKey(pDeviceName)) {
        aView.setInfoLabel("There is no smart plug named" + pDeviceName + ".");
        aLogger.warn("Attempted to set notifications for invalid device name.");
        return;
      }
      aSmartPlugController.linkUserToSmartPlug(pUserName, pDeviceName);
      aView.setInfoLabel("User: " + pUserName + " will be notified by" + pDeviceName);
      aLogger.info("User: " + pUserName + " will be notified by" + pDeviceName);
    }
  }

  public void createNewDevice(String pName, DeviceType pType) {
    aView.setInfoLabel("");
    if (pType.equals(DeviceType.CAMERA)) {
      createCamera(pName);
    } else if (pType.equals(DeviceType.LIGHTBULB)) {
      createLightbulb(pName);
    } else if (pType.equals(DeviceType.SMARTPLUG)) {
      createSmartPlug(pName);
    } else {
      createThermostat(pName);
    }
  }

  private void createCamera(String pName) {
    try {
      aCameraController.createCamera(pName);
      String txt = "Camera created; name: " + pName;
      aLogger.info(txt);
    } catch (RegistrationException e) {
      String txt = "Camera name already in use: " + pName;
      aLogger.warn(txt);
      aView.setInfoLabel(txt);
    }
  }

  private void createLightbulb(String pName) {
    try {
      aLightbulbController.createLightbulb(pName);
      String txt = "Lightbulb created; name: " + pName;
      aLogger.info(txt);
    } catch (RegistrationException e) {
      String txt = "Lightbulb name already in use: " + pName;
      aLogger.warn(txt);
      aView.setInfoLabel(txt);
    }
  }

  private void createSmartPlug(String pName) {
    try {
      aSmartPlugController.createSmartPlug(pName);
      String txt = "SmartPlug created; name: " + pName;
      aLogger.info(txt);
    } catch (RegistrationException e) {
      String txt = "SmartPlug name already in use: " + pName;
      aLogger.warn(txt);
      aView.setInfoLabel(txt);
    }
  }

  private void createThermostat(String pName) {
    try {
      aThermostatController.createThermostat(pName);
      String txt = "Thermostat created; name: " + pName;
      aLogger.info(txt);
    } catch (RegistrationException e) {
      String txt = "Thermostat name already in use: " + pName;
      aLogger.warn(txt);
      aView.setInfoLabel(txt);
    }
  }

  public void removeDevice(String pName, DeviceType pType) {
    aView.setInfoLabel("");
    if (pType.equals(DeviceType.CAMERA)) {
      removeCamera(pName);
    } else if (pType.equals(DeviceType.LIGHTBULB)) {
      removeLightbulb(pName);
    } else if (pType.equals(DeviceType.SMARTPLUG)) {
      removeSmartPlug(pName);
    } else {
      removeThermostat(pName);
    }
  }

  private void removeCamera(String pName) {
    try {
      aCameraController.removeCamera(pName);
      String txt = "Camera removed; name: " + pName;
      aLogger.info(txt);
    } catch (RegistrationException e) {
      String txt = "Camera name was not in the system: " + pName;
      aLogger.warn(txt);
      aView.setInfoLabel(txt);
    }
  }

  private void removeLightbulb(String pName) {
    try {
      aLightbulbController.removeLightbulb(pName);
      String txt = "Lightbulb removed; name: " + pName;
      aLogger.info(txt);
    } catch (RegistrationException e) {
      String txt = "Lightbulb name was not in the system: " + pName;
      aLogger.warn(txt);
      aView.setInfoLabel(txt);
    }
  }

  private void removeSmartPlug(String pName) {
    try {
      aSmartPlugController.removeSmartPlug(pName);
      String txt = "SmartPlug removed; name: " + pName;
      aLogger.info(txt);
    } catch (RegistrationException e) {
      String txt = "SmartPlug name was not in the system: " + pName;
      aLogger.warn(txt);
      aView.setInfoLabel(txt);
    }
  }

  private void removeThermostat(String pName) {
    try {
      aThermostatController.removeThermostat(pName);
      String txt = "Thermostat removed; name: " + pName;
      aLogger.info(txt);
    } catch (RegistrationException e) {
      String txt = "Thermostat name was not in the system: " + pName;
      aLogger.warn(txt);
      aView.setInfoLabel(txt);
    }
  }

  public AdminView getView() {
    aView.updateUsers(FXCollections.observableArrayList(aUserController.getUsers().keySet()));
    return aView;
  }
}
