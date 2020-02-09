package ca.uvic.seng330.assn3.models.devices;

import ca.uvic.seng330.assn3.controllers.CameraController;

public class Camera extends Device {

  private boolean isRecording = false;
  private String videoUrl =
      "https://www.youtube.com/embed/WNCl-69POro?autoplay=1&controls=0&start=120";
  private int diskSize = 0;

  private transient CameraController controller;

  public static final int MAX_DISK_SIZE = 40;

  public Camera(String pName, CameraController controller) {
    super(pName, DeviceType.CAMERA);
    this.controller = controller;
  }

  public Camera(Camera pCamera) {
    super(pCamera);
    diskSize = pCamera.diskSize;
    isRecording = pCamera.isRecording;
    this.controller = pCamera.controller;
  }

  public void setController(CameraController controller) {
    this.controller = controller;
  }

  public String getVideoUrl() {
    return videoUrl;
  }

  public void detectPerson() {
    if (!getStatus().equals(Status.OFFLINE)) {
      controller.alert(this, CameraEvent.PERSON_ENTERED_ROOM);
    }
  }

  public void detectPersonLeave() {
    if (!getStatus().equals(Status.OFFLINE)) {
      controller.alert(this, CameraEvent.PERSON_EXITED_ROOM);
    }
  }

  public void record() throws CameraException {
    if (!isRecording && !getStatus().equals(Status.FUNCTIONING)) {
      throw new CameraException("Camera with name: " + getName() + " isn't powered on!");
    }
    if (!isRecording && diskSize >= MAX_DISK_SIZE) {
      isRecording = false;
      throw new CameraException("Camera with name: " + getName() + " memory is full.");
    } else {
      isRecording = !isRecording;
      diskSize = (isRecording) ? diskSize + 10 : diskSize;
    }
  }

  public boolean isRecording() {
    return isRecording;
  }

  @Override
  public void powerOnDevice() {
    setStatus(Status.FUNCTIONING);
  }

  @Override
  public void powerOffDevice() {
    if (isRecording) {
      try {
        record();
      } catch (CameraException camFullExc) {
        // aHub.alert(getName(), LogLevel.WARN, "The camera is full");
        setStatus(Status.ERROR);
        return;
      }
    }
    setStatus(Status.OFFLINE);
  }

  @Override
  public Device copyDevice() {
    Device copyCamera = new Camera(this);
    return copyCamera;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Name: '");
    sb.append(getName());
    sb.append("' Status: '");
    sb.append(getStatus());
    sb.append("' Recording: '");
    sb.append(isRecording());
    sb.append("'");
    return sb.toString();
  }
}
