package ca.uvic.seng330.assn3.controllers;

import ca.uvic.seng330.assn3.models.CurrentUser;
import ca.uvic.seng330.assn3.models.devices.Camera;
import ca.uvic.seng330.assn3.models.devices.CameraEvent;
import ca.uvic.seng330.assn3.models.devices.CameraException;
import ca.uvic.seng330.assn3.models.devices.Device;
import ca.uvic.seng330.assn3.models.devices.Status;
import ca.uvic.seng330.assn3.views.CameraView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;

public class CameraController implements Runnable {
  private final CurrentUser currentUser;
  private HashMap<String, List<String>> cameraToUserMap;
  private HashMap<String, Camera> aCameras;
  private final ActivityLogController logger;
  private final CameraView view;
  public LightbulbController aLightBulbController;

  public CameraController(CameraView view, CurrentUser currentUser, ActivityLogController logger) {
    this.view = view;
    this.logger = logger;
    this.currentUser = currentUser;
    cameraToUserMap = new HashMap<String, List<String>>();
    aCameras = new HashMap<String, Camera>();
    readCamerasFromFile();
    readCameraToUserMapFromFile();
  }

  private void readCamerasFromFile() {
    FileReader file;
    Gson gson = new Gson();
    try {
      file = new FileReader("Cameras.json");
      aCameras = gson.fromJson(file, new TypeToken<HashMap<String, Camera>>() {}.getType());
      file.close();
    } catch (IOException e) {

    }

    if (aCameras == null) {
      aCameras = new HashMap<String, Camera>();
    } else {
      for (Camera c : aCameras.values()) {
        c.setController(this);
      }
    }
    updateCamerasInView();
  }

  private void readCameraToUserMapFromFile() {
    FileReader file;
    Gson gson = new Gson();
    try {
      file = new FileReader("CameraUserMap.json");
      cameraToUserMap =
          gson.fromJson(file, new TypeToken<HashMap<String, List<String>>>() {}.getType());
      file.close();
    } catch (IOException e) {

    }

    if (cameraToUserMap == null) {
      cameraToUserMap = new HashMap<String, List<String>>();
    }
    updateCamerasInView();
  }

  private void saveCamerasToFile() {
    Gson gson = new Gson();
    String o = gson.toJson(aCameras, new TypeToken<HashMap<String, Camera>>() {}.getType());
    FileWriter file;
    try {
      file = new FileWriter("Cameras.json");
      file.write(o);
      file.close();
    } catch (IOException e) {

    }
  }

  private void saveCameraToUserMap() {
    Gson gson = new Gson();
    String o =
        gson.toJson(cameraToUserMap, new TypeToken<HashMap<String, List<String>>>() {}.getType());
    FileWriter file;
    try {
      file = new FileWriter("CameraUserMap.json");
      file.write(o);
      file.close();
    } catch (IOException e) {

    }
  }

  @Override
  public void run() {

    for (Camera cam : aCameras.values()) {
      String statusInfo = cam.getName() + " is " + cam.getStatus();
      Platform.runLater(
          () -> {
            logger.info(statusInfo);
          });
    }
  }

  public CameraView getView() {
    return view;
  }

  public void setLightbulbController(LightbulbController pLightbulbController) {
    this.aLightBulbController = pLightbulbController;
  }

  public void updateCamerasInView() {
    if (!currentUser.isLoggedIn()) {
      return; // no need to update view if nobody is logged in
    } else if (currentUser.isAdmin()) {
      view.setCameras(FXCollections.observableArrayList(aCameras.values()));
      // admins see every device
      view.setWarningText("");
      return;
    }
    String name = currentUser.getCurrentUser().getName();
    List<Camera> displayDevices = new ArrayList<Camera>();
    for (String device : cameraToUserMap.keySet()) {
      if (cameraToUserMap.get(device).contains(name)) {
        displayDevices.add(aCameras.get(device));
      }
    }
    view.setCameras(FXCollections.observableArrayList(displayDevices));
    view.setWarningText("");
  }

  public void createCamera(String pName) throws RegistrationException {
    if (aCameras.containsKey(pName)) {
      throw new RegistrationException("Camera name \"" + pName + "\" has already been used.");
    } else {
      aCameras.put(pName, new Camera(pName, this));
      cameraToUserMap.put(pName, new ArrayList<String>());
      updateCamerasInView();
      saveCamerasToFile();
      saveCameraToUserMap();
      logger.info("\"" + pName + "\" was successfully added to cameras.");
    }
  }

  public void removeCamera(String pName) throws RegistrationException {
    if (!aCameras.containsKey(pName)) {

      throw new RegistrationException("Camera name \"" + pName + "\" was not in the system.");
    } else {
      cameraToUserMap.remove(pName);
      aCameras.remove(pName);
      updateCamerasInView();
      saveCamerasToFile();
      saveCameraToUserMap();
      logger.info("\"" + pName + "\" was successfully removed from cameras.");
    }
  }

  public void linkUserToCamera(String pUserName, String pCameraName) {
    if (!aCameras.containsKey(pCameraName) || !cameraToUserMap.containsKey(pCameraName)) {
      logger.error("The name: '" + pCameraName + "' is not a known camera name");
    } else if (!cameraToUserMap.get(pCameraName).contains(pUserName)) {
      cameraToUserMap.get(pCameraName).add(pUserName);
      saveCameraToUserMap();
    }
  }

  public void unlinkUserToCamera(String pUserName, String pCameraName) {
    if (!aCameras.containsKey(pCameraName) || !cameraToUserMap.containsKey(pCameraName)) {
      logger.error("The name: '" + pCameraName + "' is not a known camera name");
    } else {
      cameraToUserMap.get(pCameraName).remove(pUserName);
      saveCameraToUserMap();
    }
  }

  public void setCameraRecord(Camera cam) {
    if (cam == null) {
      return;
    }
    try {
      aCameras.get(cam.getName()).record();
      updateCamerasInView();
      saveCamerasToFile();
      logger.info("Record button was toggled for \"" + cam.getName() + "\".");
    } catch (CameraException e) {
      view.setWarningText(e.getMessage());
      logger.error(e.getMessage());
    }
  }

  public void setCameraOnOff(Camera cam) {
    if (cam == null) {
      return;
    }
    Camera targetCamera = aCameras.get(cam.getName());
    if (targetCamera.getStatus().equals(Status.OFFLINE)) {
      targetCamera.powerOnDevice();
      view.playVideoFromCamera(targetCamera);
    } else {
      targetCamera.powerOffDevice();
      stopVideoPlayback();
    }
    updateCamerasInView();
    saveCamerasToFile();
  }

  public HashMap<String, Camera> getCameras() {
    HashMap<String, Camera> copy = new HashMap<String, Camera>();
    for (Device device : aCameras.values()) {
      copy.put(device.getName(), (Camera) device.copyDevice());
    }
    return copy;
  }

  public HashMap<String, List<String>> getCameraToUserMap() {
    HashMap<String, List<String>> copy = new HashMap<String, List<String>>();
    for (String device : cameraToUserMap.keySet()) {
      List<String> copyList = new ArrayList<String>();
      for (String username : cameraToUserMap.get(device)) {
        copyList.add(username);
      }
      copy.put(device, copyList);
    }

    return copy;
  }

  public void shutDownDevices() {
    stopVideoPlayback();
    for (Camera device : aCameras.values()) {
      device.powerOffDevice();
    }
  }

  public void stopVideoPlayback() {
    view.closeVideo();
  }

  public void alert(Camera camera, CameraEvent e) {
    if (e.equals(CameraEvent.PERSON_ENTERED_ROOM)) {
      logger.info("Camera: " + camera.getName() + " detected a person and turned on the lights");
      aLightBulbController.turnLightsOn();
    } else if (e.equals(CameraEvent.PERSON_EXITED_ROOM)) {
      logger.info(
          "Camera: " + camera.getName() + " detected a person leaving and turned off the lights");
      aLightBulbController.turnLightsOff();
    }
  }
}
