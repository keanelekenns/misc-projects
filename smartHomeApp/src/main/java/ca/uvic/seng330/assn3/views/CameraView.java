package ca.uvic.seng330.assn3.views;

import ca.uvic.seng330.assn3.controllers.CameraController;
import ca.uvic.seng330.assn3.models.devices.Camera;
import ca.uvic.seng330.assn3.models.devices.Status;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class CameraView extends GridPane {
  private CameraController controller;
  private Label cameraListLabel;
  private WebView videoDisplay;
  private WebEngine webEngine;
  private Label warningText;
  private ListView<Camera> cameraListView;
  private Button recordButton;
  private Button onOffButton;

  public CameraView() {
    createAndConfigurePane();

    createAndLayoutControls();
  }

  public void setController(CameraController cameraController) {
    this.controller = cameraController;
  }

  private void createAndLayoutControls() {

    warningText = new Label();
    recordButton = new Button("No camera selected");
    onOffButton = new Button("No camera selected");

    videoDisplay = new WebView();
    videoDisplay.setMaxWidth(300);
    videoDisplay.setMaxHeight(200);
    webEngine = videoDisplay.getEngine();
    webEngine.loadContent("<p>No camera selected</p>");

    cameraListLabel = new Label("CAMERAS:");
    cameraListLabel.setAlignment(Pos.TOP_LEFT);
    ObservableList<Camera> cameraList = FXCollections.observableArrayList();
    cameraListView = new ListView<Camera>(cameraList);
    cameraListView.setOnMouseClicked(
        actionEvent -> {
          updateRecordButtonText();
          updateOnOffButtonText();
          playVideoFromCamera(cameraListView.getSelectionModel().getSelectedItem());
        });

    cameraListView.setMinWidth(300);
    cameraListView.setMaxHeight(200);

    Button testPersonDetectedButton = new Button("Test person entered room");
    Button testPersonLeftButton = new Button("Test person left room");

    testPersonDetectedButton.setOnAction(
        actionEvent -> {
          if (cameraListView.getFocusModel().getFocusedIndex() == -1) {
            warningText.setText("A camera must be selected to test this feature.");
            return;
          }
          if (!cameraListView
              .getFocusModel()
              .getFocusedItem()
              .getStatus()
              .equals(Status.FUNCTIONING)) {
            warningText.setText("The camera must be on to test this feature.");
            return;
          }
          cameraListView.getFocusModel().getFocusedItem().detectPerson();
        });

    testPersonLeftButton.setOnAction(
        actionEvent -> {
          if (cameraListView.getFocusModel().getFocusedItem() == null) {
            warningText.setText("A camera must be selected to test this feature.");
            return;
          }
          if (cameraListView.getFocusModel().getFocusedItem().getStatus().equals(Status.OFFLINE)) {
            warningText.setText("The camera must be on to test this feature.");
            return;
          }
          cameraListView.getFocusModel().getFocusedItem().detectPersonLeave();
        });

    recordButton.setOnAction(
        actionEvent -> {
          controller.setCameraRecord(cameraListView.getFocusModel().getFocusedItem());
          updateRecordButtonText();
          updateOnOffButtonText();
        });

    onOffButton.setOnAction(
        actionEvent -> {
          controller.setCameraOnOff(cameraListView.getFocusModel().getFocusedItem());
          updateRecordButtonText();
          updateOnOffButtonText();
        });
    addRow(0, cameraListLabel);
    addRow(1, warningText);
    addRow(2, cameraListView, new Label("Video stream:"), videoDisplay);
    addRow(3, recordButton, onOffButton);
    addRow(4, testPersonDetectedButton, testPersonLeftButton);
  }

  private void updateRecordButtonText() {
    Camera selected = cameraListView.getFocusModel().getFocusedItem();
    if (selected == null) {
      recordButton.setText("No camera selected");
    } else if (selected.isRecording()) {
      recordButton.setText("Stop recording");
    } else {
      recordButton.setText("Start recording");
    }
  }

  private void updateOnOffButtonText() {
    Camera selected = cameraListView.getFocusModel().getFocusedItem();
    if (selected == null) {
      onOffButton.setText("No camera selected");
    } else if (selected.getStatus().equals(Status.OFFLINE)) {
      onOffButton.setText("Turn on");
    } else {
      onOffButton.setText("Turn off");
    }
  }

  public void setWarningText(String txt) {
    warningText.setText(txt);
  }

  public void closeVideo() {
    webEngine.loadContent("<p>No content to show</p>");
  }

  public void playVideoFromCamera(Camera c) {
    if (c == null) {
      closeVideo();
    } else if (c.getStatus() == Status.OFFLINE) {
      webEngine.loadContent("<p>Camera with name: \"" + c.getName() + "\" is not turned on.");
    } else {
      webEngine.load(c.getVideoUrl());
    }
  }

  public void setCameras(ObservableList<Camera> cameras) {
    cameraListView.setItems(cameras);
    cameraListView.refresh();
  }

  private void createAndConfigurePane() {

    setPadding(new Insets(10, 10, 20, 10));
    ColumnConstraints leftCol = new ColumnConstraints();
    leftCol.setHalignment(HPos.RIGHT);
    leftCol.setHgrow(Priority.NEVER);

    ColumnConstraints rightCol = new ColumnConstraints();
    rightCol.setHgrow(Priority.SOMETIMES);

    getColumnConstraints().addAll(leftCol, rightCol);

    setAlignment(Pos.CENTER);
    setHgap(5);
    setVgap(10);
  }
}
