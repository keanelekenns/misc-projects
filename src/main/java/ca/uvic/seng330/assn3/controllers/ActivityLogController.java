package ca.uvic.seng330.assn3.controllers;

import ca.uvic.seng330.assn3.models.CurrentUser;
import ca.uvic.seng330.assn3.views.ActivityLogView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import javafx.collections.FXCollections;

public class ActivityLogController {
  private ActivityLogView view;
  private LinkedList<String> activityLogs;
  private CurrentUser currentUser;
  private CameraController aCameraController;
  private ThermostatController aThermostatController;
  private SmartPlugController aSmartPlugController;
  private LightbulbController aLightbulbController;

  public ActivityLogController(ActivityLogView view, CurrentUser currentUser) {
    this.currentUser = currentUser;
    this.view = view;
    this.activityLogs = new LinkedList<String>();

    view.setController(this);
    readLogFromFile();
  }

  private void readLogFromFile() {
    FileReader file;
    Gson gson = new Gson();
    try {
      file = new FileReader("ActivityLogs.json");
      activityLogs = gson.fromJson(file, new TypeToken<LinkedList<String>>() {}.getType());
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (activityLogs == null) {
      activityLogs = new LinkedList<String>();
    }
    view.setActivityLogs(FXCollections.observableArrayList(activityLogs));
  }

  public void info(String msg) {
    StringBuilder sb = new StringBuilder();
    sb.append(new Date());
    sb.append(" INFO: ");
    sb.append("user: \"");
    sb.append(
        currentUser.isLoggedIn() ? currentUser.getCurrentUser().getName() : "NO CURRENT USER");
    sb.append("\" msg: ");
    sb.append(msg);
    addLogToList(sb.toString());
    addLogToViewAndSave();
  }

  public void warn(String msg) {
    StringBuilder sb = new StringBuilder();
    sb.append(new Date());
    sb.append(" WARN: ");
    sb.append("user: \"");
    sb.append(
        currentUser.isLoggedIn() ? currentUser.getCurrentUser().getName() : "NO CURRENT USER");
    sb.append("\" msg: ");
    sb.append(msg);
    addLogToList(sb.toString());
    addLogToViewAndSave();
  }

  public void error(String msg) {
    StringBuilder sb = new StringBuilder();
    sb.append(new Date());
    sb.append(" ERROR: ");
    sb.append("user: \"");
    sb.append(
        currentUser.isLoggedIn() ? currentUser.getCurrentUser().getName() : "NO CURRENT USER");
    sb.append("\" msg: ");
    sb.append(msg);
    addLogToList(sb.toString());

    addLogToViewAndSave();
  }

  private void addLogToList(String msg) {
    activityLogs.add(msg);
    if (activityLogs.size() > 100) {
      activityLogs.pop();
    }
  }

  private void addLogToViewAndSave() {
    view.setActivityLogs(FXCollections.observableArrayList(activityLogs));

    Gson gson = new Gson();
    String o = gson.toJson(activityLogs);
    FileWriter file;
    try {
      file = new FileWriter("ActivityLogs.json");
      file.write(o);
      file.close();
    } catch (IOException e) {

    }
  }

  public void statusCheck() {
    Thread cameraThread = new Thread(aCameraController, "cameraThread");
    Thread thermostatThread = new Thread(aThermostatController, "thermostatThread");
    Thread lightbulbThread = new Thread(aLightbulbController, "lightbulbThread");
    Thread smartPlugThread = new Thread(aSmartPlugController, "smartPlugThread");
    cameraThread.start();
    thermostatThread.start();
    lightbulbThread.start();
    smartPlugThread.start();
  }

  public ActivityLogView getView() {
    return view;
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
}
