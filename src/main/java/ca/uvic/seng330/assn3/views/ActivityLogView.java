package ca.uvic.seng330.assn3.views;

import ca.uvic.seng330.assn3.controllers.ActivityLogController;
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

public class ActivityLogView extends GridPane {
  private Label warningText;
  private ListView<String> activityLogs;
  private ActivityLogController controller;

  public ActivityLogView() {
    createAndConfigurePane();

    createAndLayoutControls();
  }

  public void setController(ActivityLogController controller) {
    this.controller = controller;
  }

  private void createAndLayoutControls() {

    warningText = new Label();

    Label logLabel = new Label("Activity Log:");
    logLabel.setAlignment(Pos.TOP_LEFT);
    Button statusCheckButton = new Button("Run Status Check");
    statusCheckButton.setId("statusCheckButton");
    statusCheckButton.setOnAction(
        actionEvent -> {
          controller.statusCheck();
        });

    ObservableList<String> logList = FXCollections.observableArrayList();

    activityLogs = new ListView<String>(logList);
    activityLogs.setPrefHeight(800);
    activityLogs.setMinWidth(500);
    add(logLabel, 0, 0);
    add(activityLogs, 0, 1);
    add(statusCheckButton, 0, 2);
  }

  public void setWarningText(String txt) {
    warningText.setText(txt);
  }

  private void createAndConfigurePane() {

    setPadding(new Insets(10, 10, 20, 10));
    ColumnConstraints leftCol = new ColumnConstraints();
    leftCol.setHalignment(HPos.RIGHT);
    leftCol.setHgrow(Priority.NEVER);
    ColumnConstraints rightCol = new ColumnConstraints();
    rightCol.setHgrow(Priority.SOMETIMES);

    getColumnConstraints().addAll(leftCol, rightCol);

    setAlignment(Pos.TOP_RIGHT);
    setHgap(5);
    setVgap(10);
  }

  public synchronized void setActivityLogs(ObservableList<String> list) {
    activityLogs.setItems(list);
    activityLogs.refresh();
    activityLogs.scrollTo(list.size() - 1);
  }

  public ObservableList<String> getActivityLogs() {
    return activityLogs.getItems();
  }
}
