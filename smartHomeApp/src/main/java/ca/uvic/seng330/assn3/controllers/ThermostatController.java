package ca.uvic.seng330.assn3.controllers;

import ca.uvic.seng330.assn3.models.CurrentUser;
import ca.uvic.seng330.assn3.models.devices.Device;
import ca.uvic.seng330.assn3.models.devices.Status;
import ca.uvic.seng330.assn3.models.devices.Temperature.Unit;
import ca.uvic.seng330.assn3.models.devices.Thermostat;
import ca.uvic.seng330.assn3.models.devices.ThermostatException;
import ca.uvic.seng330.assn3.views.ThermostatView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;

public class ThermostatController implements Runnable {
  private final CurrentUser currentUser;
  private final HashMap<String, List<String>> thermostatToUserMap;
  private final HashMap<String, Thermostat> aThermostats;
  private final ActivityLogController logger;
  private final ThermostatView view;

  public ThermostatController(
      ThermostatView view, CurrentUser currentUser, ActivityLogController logger) {
    this.view = view;
    this.logger = logger;
    this.currentUser = currentUser;
    thermostatToUserMap = new HashMap<String, List<String>>();
    aThermostats = new HashMap<String, Thermostat>();
  }

  @Override
  public void run() {
    for (Thermostat therm : aThermostats.values()) {
      String statusInfo = therm.getName() + " is " + therm.getStatus();
      Platform.runLater(
          () -> {
            logger.info(statusInfo);
          });
    }
  }

  public ThermostatView getView() {
    return view;
  }

  public void updateThermostatsInView() {
    if (!currentUser.isLoggedIn()) {
      return; // no need to update view if nobody is logged in
    } else if (currentUser.isAdmin()) {
      view.setThermostats(FXCollections.observableArrayList(aThermostats.values()));
      // admins see every device
      return;
    }
    String name = currentUser.getCurrentUser().getName();
    List<Thermostat> displayDevices = new ArrayList<Thermostat>();
    for (String device : thermostatToUserMap.keySet()) {
      if (thermostatToUserMap.get(device).contains(name)) {
        displayDevices.add(aThermostats.get(device));
      }
    }
    view.setThermostats(FXCollections.observableArrayList(displayDevices));
  }

  public void createThermostat(String pName) throws RegistrationException {
    if (aThermostats.containsKey(pName)) {
      String txt = "Thermostat name \"" + pName + "\" has already been used.";
      logger.error(txt);
      // or we could make this throw an exception and have the caller log the
      // exception message. We probably also want to add the message to the view so the
      // admin knows that they didn't create a new device.
      throw new RegistrationException(txt);
    } else {
      aThermostats.put(pName, new Thermostat(pName, this));
      thermostatToUserMap.put(pName, new ArrayList<String>());
      updateThermostatsInView();
      view.setWarningText("");
      logger.info("\"" + pName + "\" was successfully added to thermostats.");
    }
  }

  public void removeThermostat(String pName) throws RegistrationException {
    if (!aThermostats.containsKey(pName)) {
      throw new RegistrationException("Thermostat name \"" + pName + "\" was not in the system.");
    } else {
      thermostatToUserMap.remove(pName);
      aThermostats.remove(pName);
      updateThermostatsInView();
      view.setWarningText("");
      logger.info("\"" + pName + "\" was successfully removed from thermostats.");
    }
  }

  public void linkUserToThermostat(String pUserName, String pThermostatName) {
    if (!aThermostats.containsKey(pThermostatName)
        || !thermostatToUserMap.containsKey(pThermostatName)) {
      logger.error("The name \"" + pThermostatName + "\" is not associated with a thermostat.");
    } else if (!thermostatToUserMap.get(pThermostatName).contains(pUserName)) {
      thermostatToUserMap.get(pThermostatName).add(pUserName);
    }
  }

  public void unlinkUserToThermostat(String pUserName, String pThermostatName) {
    if (!aThermostats.containsKey(pThermostatName)
        || !thermostatToUserMap.containsKey(pThermostatName)) {
      logger.error("The name \"" + pThermostatName + "\" is not associated with a thermostat.");
    } else {
      thermostatToUserMap.get(pThermostatName).remove(pUserName);
    }
  }

  public void setThermostatTemp(Thermostat therm, Unit metric, Double temp) {
    if (therm == null) {
      return;
    }
    try {
      aThermostats.get(therm.getName()).setTemp(metric, temp);
      updateThermostatsInView();
      view.setWarningText("");
      logger.info("Temperature was set for \"" + therm.getName() + "\".");
    } catch (ThermostatException e) {
      logger.error(e.getMessage());
      view.setWarningText(e.getMessage());
    }
  }

  public void setThermostatOnOff(Thermostat therm) {
    if (therm == null) {
      return;
    }
    Thermostat targetTherm = aThermostats.get(therm.getName());
    if (targetTherm.getStatus().equals(Status.OFFLINE)) {
      targetTherm.powerOnDevice();
    } else {
      targetTherm.powerOffDevice(); // what if it was at ATTENTION_NEEDED
    }
    updateThermostatsInView();
    view.setWarningText("");
  }

  public void alert(Thermostat therm) {
    DecimalFormat df = new DecimalFormat("#.#");
    view.setWarningText(
        therm.getName()
            + " has been set to "
            + df.format(therm.getTemp().getDegrees())
            + " "
            + therm.getTemp().getUnit().toString()
            + " due to climate change outside.");
    logger.info(
        therm.getName()
            + " has been set to "
            + df.format(therm.getTemp().getDegrees())
            + " "
            + therm.getTemp().getUnit().toString()
            + " due to climate change outside.");
    updateThermostatsInView();
  }

  public double convertStringtoDouble(String s) throws NumberFormatException {
    try {
      double num = Double.parseDouble(s);
      return num;
    } catch (NumberFormatException e) {
      throw e;
    }
  }

  public void convertTemp(Thermostat therm) {
    if (therm.getTemp() == null) {
      String txt = "No temperature set for '" + therm.getName() + "'";
      logger.error(txt);
      view.setWarningText(txt);
    } else if (therm.getTemp().getUnit().equals(Unit.FAHRENHEIT)) {
      double newTemp = (5 * (therm.getTemp().getDegrees() - 32)) / 9;
      try {
        aThermostats.get(therm.getName()).setTemp(Unit.CELSIUS, newTemp);
        updateThermostatsInView();
        view.setWarningText("");
        logger.info("Temperature was converted to Celsius for \"" + therm.getName() + "\".");
      } catch (ThermostatException e) {
        logger.error(e.getMessage());
        view.setWarningText(e.getMessage());
      }
    } else {
      double newTemp = (9 * therm.getTemp().getDegrees()) / 5 + 32;
      try {
        aThermostats.get(therm.getName()).setTemp(Unit.FAHRENHEIT, newTemp);
        updateThermostatsInView();
        view.setWarningText("");
        logger.info("Temperature was converted to Fahrenheit for \"" + therm.getName() + "\".");
      } catch (ThermostatException e) {
        logger.error(e.getMessage());
        view.setWarningText(e.getMessage());
      }
    }
  }

  public HashMap<String, Thermostat> getThermostats() {
    HashMap<String, Thermostat> copy = new HashMap<String, Thermostat>();
    for (Device device : aThermostats.values()) {
      copy.put(device.getName(), (Thermostat) device.copyDevice());
    }
    return copy;
  }

  public HashMap<String, List<String>> getThermostatToUserMap() {
    HashMap<String, List<String>> copy = new HashMap<String, List<String>>();
    for (String device : thermostatToUserMap.keySet()) {
      List<String> copyList = new ArrayList<String>();
      for (String username : thermostatToUserMap.get(device)) {
        copyList.add(username);
      }
      copy.put(device, copyList);
    }

    return copy;
  }

  public void shutDownDevices() {
    for (Thermostat device : aThermostats.values()) {
      device.powerOffDevice();
    }
  }
}
