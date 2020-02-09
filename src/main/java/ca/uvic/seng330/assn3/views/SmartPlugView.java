package ca.uvic.seng330.assn3.views;

import ca.uvic.seng330.assn3.controllers.SmartPlugController;
import ca.uvic.seng330.assn3.models.devices.SmartPlug;
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

public class SmartPlugView extends GridPane {
  private SmartPlugController controller;
  private Label smartPlugListLabel;
  private Label warningText;
  private ListView<SmartPlug> smartPlugListView;
  private Button toggleButton;
  private Button onOffButton;

  public SmartPlugView() {
    createAndConfigurePane();

    createAndLayoutControls();
  }

  public void setController(SmartPlugController controller) {
    this.controller = controller;
  }

  private void createAndLayoutControls() {

    warningText = new Label();

    toggleButton = new Button("No smartplug selected");
    onOffButton = new Button("No smartplug selected");

    smartPlugListLabel = new Label("SMARTPLUGS:");
    ObservableList<SmartPlug> smartPlugList = FXCollections.observableArrayList();
    smartPlugListView = new ListView<SmartPlug>(smartPlugList);
    smartPlugListView.setOnMouseClicked(
        actionEvent -> {
          updateToggleButtonText();
          updateOnOffButtonText();
        });
    smartPlugListView.setMinWidth(400);
    smartPlugListView.setMaxHeight(200);

    toggleButton.setOnAction(
        actionEvent -> {
          controller.toggleSmartPlug(smartPlugListView.getFocusModel().getFocusedItem());
          updateToggleButtonText();
        });

    onOffButton.setOnAction(
        actionEvent -> {
          controller.setSmartPlugOnOff(smartPlugListView.getFocusModel().getFocusedItem());
          updateToggleButtonText();
          updateOnOffButtonText();
        });

    addRow(0, smartPlugListLabel);
    addRow(1, warningText);
    addRow(2, smartPlugListView);
    addRow(3, toggleButton, onOffButton);
  }

  private void updateToggleButtonText() {
    SmartPlug selected = smartPlugListView.getFocusModel().getFocusedItem();
    if (selected == null) {
      toggleButton.setText("No smartplug selected");
    } else {
      toggleButton.setText("Toggle");
    }
  }

  private void updateOnOffButtonText() {
    SmartPlug selected = smartPlugListView.getFocusModel().getFocusedItem();
    if (selected == null) {
      onOffButton.setText("No smartplug selected");
    } else if (selected.getStatus().equals(Status.OFFLINE)) {
      onOffButton.setText("Turn on");
    } else {
      onOffButton.setText("Turn off");
    }
  }

  public void setWarningText(String txt) {
    warningText.setText(txt);
  }

  public void setSmartPlugs(ObservableList<SmartPlug> smartplugs) {
    smartPlugListView.setItems(smartplugs);
    smartPlugListView.refresh();
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
