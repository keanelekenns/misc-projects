package ca.uvic.seng330.assn3.controllers;

import ca.uvic.seng330.assn3.models.CurrentUser;
import ca.uvic.seng330.assn3.models.devices.Device;
import ca.uvic.seng330.assn3.models.devices.SmartPlug;
import ca.uvic.seng330.assn3.models.devices.SmartPlugException;
import ca.uvic.seng330.assn3.models.devices.Status;
import ca.uvic.seng330.assn3.views.SmartPlugView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;

public class SmartPlugController implements Runnable {
  private final CurrentUser currentUser;
  private final HashMap<String, List<String>> smartPlugToUserMap;
  private final HashMap<String, SmartPlug> aSmartPlugs;
  private final ActivityLogController logger;
  private final SmartPlugView view;

  public SmartPlugController(
      SmartPlugView view, CurrentUser currentUser, ActivityLogController logger) {
    this.view = view;
    this.logger = logger;
    this.currentUser = currentUser;
    smartPlugToUserMap = new HashMap<String, List<String>>();
    aSmartPlugs = new HashMap<String, SmartPlug>();
  }

  @Override
  public void run() {
    for (SmartPlug plug : aSmartPlugs.values()) {
      String statusInfo = plug.getName() + " is " + plug.getStatus();
      Platform.runLater(
          () -> {
            logger.info(statusInfo);
          });
    }
  }

  public SmartPlugView getView() {
    return view;
  }

  public void updateSmartPlugsInView() {
    if (!currentUser.isLoggedIn()) {
      return; // no need to update view if nobody is logged in
    } else if (currentUser.isAdmin()) {
      view.setSmartPlugs(FXCollections.observableArrayList(aSmartPlugs.values()));
      // admins see every device
      view.setWarningText("");
      return;
    }
    String name = currentUser.getCurrentUser().getName();
    List<SmartPlug> displayDevices = new ArrayList<SmartPlug>();
    for (String device : smartPlugToUserMap.keySet()) {
      if (smartPlugToUserMap.get(device).contains(name)) {
        displayDevices.add(aSmartPlugs.get(device));
      }
    }
    view.setSmartPlugs(FXCollections.observableArrayList(displayDevices));
    view.setWarningText("");
  }

  public void createSmartPlug(String pName) throws RegistrationException {
    if (aSmartPlugs.containsKey(pName)) {
      throw new RegistrationException("SmartPlug name \"" + pName + "\" has already been used.");
    } else {
      aSmartPlugs.put(pName, new SmartPlug(pName));
      smartPlugToUserMap.put(pName, new ArrayList<String>());
      updateSmartPlugsInView();
      logger.info("\"" + pName + "\" was successfully added to smart plugs.");
    }
  }

  public void removeSmartPlug(String pName) throws RegistrationException {
    if (!aSmartPlugs.containsKey(pName)) {

      throw new RegistrationException("SmartPlug name \"" + pName + "\" was not in the system.");
    } else {
      smartPlugToUserMap.remove(pName);
      aSmartPlugs.remove(pName);
      updateSmartPlugsInView();
      logger.info("\"" + pName + "\" was successfully removed from smart plugs.");
    }
  }

  public void linkUserToSmartPlug(String pUserName, String pSmartPlugName) {
    if (!aSmartPlugs.containsKey(pSmartPlugName)
        || !smartPlugToUserMap.containsKey(pSmartPlugName)) {
      logger.error("The name \"" + pSmartPlugName + "\" is not associated with a smartplug.");
    } else if (!smartPlugToUserMap.get(pSmartPlugName).contains(pUserName)) {
      smartPlugToUserMap.get(pSmartPlugName).add(pUserName);
    }
  }

  public void unlinkUserToSmartPlug(String pUserName, String pSmartPlugName) {
    if (!aSmartPlugs.containsKey(pSmartPlugName)
        || !smartPlugToUserMap.containsKey(pSmartPlugName)) {
      logger.error("The name \"" + pSmartPlugName + "\" is not associated with a smartplug.");
    } else {
      smartPlugToUserMap.get(pSmartPlugName).remove(pUserName);
    }
  }

  public void toggleSmartPlug(SmartPlug plug) {
    if (plug == null) {
      return;
    }
    try {
      aSmartPlugs.get(plug.getName()).toggle();
      updateSmartPlugsInView();
      logger.info("\"" + plug.getName() + "\" was toggled.");
    } catch (SmartPlugException e) {
      logger.error(e.getMessage());
      view.setWarningText(e.getMessage());
    }
  }

  public void setSmartPlugOnOff(SmartPlug plug) {
    if (plug == null) {
      return;
    }
    SmartPlug targetCamera = aSmartPlugs.get(plug.getName());
    if (targetCamera.getStatus().equals(Status.OFFLINE)) {
      targetCamera.powerOnDevice();
    } else {
      targetCamera.powerOffDevice();
    }
    updateSmartPlugsInView();
  }

  public HashMap<String, SmartPlug> getSmartPlugs() {
    HashMap<String, SmartPlug> copy = new HashMap<String, SmartPlug>();
    for (Device device : aSmartPlugs.values()) {
      copy.put(device.getName(), (SmartPlug) device.copyDevice());
    }
    return copy;
  }

  public HashMap<String, List<String>> getSmartPlugToUserMap() {
    HashMap<String, List<String>> copy = new HashMap<String, List<String>>();
    for (String device : smartPlugToUserMap.keySet()) {
      List<String> copyList = new ArrayList<String>();
      for (String username : smartPlugToUserMap.get(device)) {
        copyList.add(username);
      }
      copy.put(device, copyList);
    }

    return copy;
  }

  public void shutDownDevices() {
    for (SmartPlug device : aSmartPlugs.values()) {
      device.powerOffDevice();
    }
  }
}
