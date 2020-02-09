package ca.uvic.seng330.assn3.controllers;

import ca.uvic.seng330.assn3.models.CurrentUser;
import ca.uvic.seng330.assn3.models.devices.Device;
import ca.uvic.seng330.assn3.models.devices.Lightbulb;
import ca.uvic.seng330.assn3.models.devices.LightbulbException;
import ca.uvic.seng330.assn3.models.devices.Status;
import ca.uvic.seng330.assn3.views.LightbulbView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;

public class LightbulbController implements Runnable {
  private final CurrentUser currentUser;
  private final HashMap<String, List<String>> lightbulbToUserMap;
  private final HashMap<String, Lightbulb> aLightbulbs;
  private final ActivityLogController logger;
  private final LightbulbView view;

  public LightbulbController(
      LightbulbView view, CurrentUser currentUser, ActivityLogController logger) {
    this.view = view;
    this.logger = logger;
    this.currentUser = currentUser;
    lightbulbToUserMap = new HashMap<String, List<String>>();
    aLightbulbs = new HashMap<String, Lightbulb>();
  }

  @Override
  public void run() {
    for (Lightbulb bulb : aLightbulbs.values()) {
      String statusInfo = bulb.getName() + " is " + bulb.getStatus();
      Platform.runLater(
          () -> {
            logger.info(statusInfo);
          });
    }
  }

  public LightbulbView getView() {
    return view;
  }

  public void updateLightbulbsInView() {
    if (!currentUser.isLoggedIn()) {
      return; // no need to update view if nobody is logged in
    } else if (currentUser.isAdmin()) {
      view.setLightbulbs(FXCollections.observableArrayList(aLightbulbs.values()));
      // admins see every device
      view.setWarningText("");
      return;
    }
    String name = currentUser.getCurrentUser().getName();
    List<Lightbulb> displayDevices = new ArrayList<Lightbulb>();
    for (String device : lightbulbToUserMap.keySet()) {
      if (lightbulbToUserMap.get(device).contains(name)) {
        displayDevices.add(aLightbulbs.get(device));
      }
    }
    view.setLightbulbs(FXCollections.observableArrayList(displayDevices));
    view.setWarningText("");
  }

  public void createLightbulb(String pName) throws RegistrationException {
    if (aLightbulbs.containsKey(pName)) {
      throw new RegistrationException("Lightbulb name \"" + pName + "\" has already been used.");
    } else {
      aLightbulbs.put(pName, new Lightbulb(pName));
      lightbulbToUserMap.put(pName, new ArrayList<String>());
      updateLightbulbsInView();
      logger.info("\"" + pName + "\" was successfully added to lightbulbs.");
    }
  }

  public void removeLightbulb(String pName) throws RegistrationException {
    if (!aLightbulbs.containsKey(pName)) {
      throw new RegistrationException("Lightbulb name \"" + pName + "\" was not in the system.");
    } else {
      lightbulbToUserMap.remove(pName);
      aLightbulbs.remove(pName);
      updateLightbulbsInView();
      logger.info("\"" + pName + "\" was successfully removed from lightbulbs.");
    }
  }

  public void linkUserToLightbulb(String pUserName, String pLightbulbName) {
    if (!aLightbulbs.containsKey(pLightbulbName)
        || !lightbulbToUserMap.containsKey(pLightbulbName)) {
      logger.error("The name '" + pLightbulbName + "' is not associated with a lightbulb.");
    } else if (!lightbulbToUserMap.get(pLightbulbName).contains(pUserName)) {
      lightbulbToUserMap.get(pLightbulbName).add(pUserName);
    }
  }

  public void unlinkUserToLightbulb(String pUserName, String pLightbulbName) {
    if (!aLightbulbs.containsKey(pLightbulbName)
        || !lightbulbToUserMap.containsKey(pLightbulbName)) {
      logger.error("The name '" + pLightbulbName + "' is not associated with a lightbulb.");
    } else {
      lightbulbToUserMap.get(pLightbulbName).remove(pUserName);
    }
  }

  public void toggleLightbulb(Lightbulb bulb) {
    if (bulb == null) {
      return;
    }
    try {
      aLightbulbs.get(bulb.getName()).toggle();
      updateLightbulbsInView();
      logger.info("\"" + bulb.getName() + "\" was toggled.");
    } catch (LightbulbException e) {
      logger.error(e.getMessage());
      view.setWarningText(e.getMessage());
    }
  }

  public void setLightbulbOnOff(Lightbulb bulb) {
    if (bulb == null) {
      return;
    }
    Lightbulb targetLightbulb = aLightbulbs.get(bulb.getName());
    if (targetLightbulb.getStatus().equals(Status.OFFLINE)) {
      targetLightbulb.powerOnDevice();
    } else {
      targetLightbulb.powerOffDevice();
    }
    updateLightbulbsInView();
  }

  public HashMap<String, Lightbulb> getLightbulbs() {
    HashMap<String, Lightbulb> copy = new HashMap<String, Lightbulb>();
    for (Device device : aLightbulbs.values()) {
      copy.put(device.getName(), (Lightbulb) device.copyDevice());
    }
    return copy;
  }

  public HashMap<String, List<String>> getLightbulbToUserMap() {
    HashMap<String, List<String>> copy = new HashMap<String, List<String>>();
    for (String device : lightbulbToUserMap.keySet()) {
      List<String> copyList = new ArrayList<String>();
      for (String username : lightbulbToUserMap.get(device)) {
        copyList.add(username);
      }
      copy.put(device, copyList);
    }

    return copy;
  }

  public void shutDownDevices() {
    for (Lightbulb device : aLightbulbs.values()) {
      device.powerOffDevice();
    }
  }

  public void turnLightsOn() {
    for (Lightbulb lb : aLightbulbs.values()) {
      lb.powerOnDevice();
      if (lb.getCondition() == false) {
        try {
          lb.toggle();
        } catch (LightbulbException e) {

        }
      }
    }
    updateLightbulbsInView();
    logger.info("The lights were turned on by another device");
  }

  public void turnLightsOff() {
    for (Lightbulb lb : aLightbulbs.values()) {
      if (lb.getCondition() == true) {
        try {
          lb.toggle();
        } catch (LightbulbException e) {

        }
      }
    }
    updateLightbulbsInView();
    logger.info("The lights were turned off by another device");
  }
}
