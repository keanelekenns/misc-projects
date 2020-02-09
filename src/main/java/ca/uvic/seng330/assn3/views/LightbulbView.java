package ca.uvic.seng330.assn3.views;

import ca.uvic.seng330.assn3.controllers.LightbulbController;
import ca.uvic.seng330.assn3.models.devices.Lightbulb;
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

public class LightbulbView extends GridPane {
  private LightbulbController controller;
  private Label lightbulbListLabel;
  private Label warningText;
  private ListView<Lightbulb> lightbulbListView;
  private Button toggleButton;
  private Button onOffButton;

  public LightbulbView() {
    createAndConfigurePane();

    createAndLayoutControls();
  }

  public void setController(LightbulbController controller) {
    this.controller = controller;
  }

  private void createAndLayoutControls() {

    warningText = new Label();

    toggleButton = new Button("No lightbulb selected");
    onOffButton = new Button("No lightbulb selected");

    lightbulbListLabel = new Label("LIGHTBULBS:");
    ObservableList<Lightbulb> lightbulbList = FXCollections.observableArrayList();
    lightbulbListView = new ListView<Lightbulb>(lightbulbList);
    lightbulbListView.setOnMouseClicked(
        actionEvent -> {
          updateToggleButtonText();
          updateOnOffButtonText();
        });
    lightbulbListView.setMinWidth(400);
    lightbulbListView.setMaxHeight(200);

    toggleButton.setOnAction(
        actionEvent -> {
          controller.toggleLightbulb(lightbulbListView.getFocusModel().getFocusedItem());
          updateToggleButtonText();
        });

    onOffButton.setOnAction(
        actionEvent -> {
          controller.setLightbulbOnOff(lightbulbListView.getFocusModel().getFocusedItem());
          updateToggleButtonText();
          updateOnOffButtonText();
        });

    addRow(0, lightbulbListLabel);
    addRow(1, warningText);
    addRow(2, lightbulbListView);
    addRow(3, toggleButton, onOffButton);
  }

  private void updateToggleButtonText() {
    Lightbulb selected = lightbulbListView.getFocusModel().getFocusedItem();
    if (selected == null) {
      toggleButton.setText("No lightbulb selected");
    } else {
      toggleButton.setText("Toggle");
    }
  }

  private void updateOnOffButtonText() {
    Lightbulb selected = lightbulbListView.getFocusModel().getFocusedItem();
    if (selected == null) {
      onOffButton.setText("No lightbulb selected");
    } else if (selected.getStatus().equals(Status.OFFLINE)) {
      onOffButton.setText("Turn on");
    } else {
      onOffButton.setText("Turn off");
    }
  }

  public void setWarningText(String txt) {
    warningText.setText(txt);
  }

  public void setLightbulbs(ObservableList<Lightbulb> lightbulbs) {
    lightbulbListView.setItems(lightbulbs);
    lightbulbListView.refresh();
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
