package ca.uvic.seng330.assn3.views;

import ca.uvic.seng330.assn3.controllers.ThermostatController;
import ca.uvic.seng330.assn3.models.devices.Status;
import ca.uvic.seng330.assn3.models.devices.Temperature.Unit;
import ca.uvic.seng330.assn3.models.devices.Thermostat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class ThermostatView extends GridPane {
  private ThermostatController controller;
  private Label thermostatListLabel;
  private Label warningText;
  private ListView<Thermostat> thermostatListView;
  private Button setTempButton;
  private Button convertTempButton;
  private Button onOffButton;
  private ComboBox<Unit> metricDropdown;
  private TextField tempField;

  public ThermostatView() {

    createAndConfigurePane();
    createAndLayoutControls();
  }

  public void setController(ThermostatController controller) {
    this.controller = controller;
  }

  private void createAndLayoutControls() {

    warningText = new Label();

    setTempButton = new Button("No thermostat selected");
    convertTempButton = new Button("No thermostat selected");
    onOffButton = new Button("No thermostat selected");
    Button stimulateClimateChangeButton = new Button("Stimulate Climate Change");
    stimulateClimateChangeButton.setId("stimulateClimateChangeButton");

    thermostatListLabel = new Label("THERMOSTATS:");
    ObservableList<Thermostat> thermostatList = FXCollections.observableArrayList();
    thermostatListView = new ListView<Thermostat>(thermostatList);
    thermostatListView.setOnMouseClicked(
        actionEvent -> {
          updateSetTempButtonText();
          updateConvertTempButtonText();
          updateOnOffButtonText();
        });
    thermostatListView.setMinWidth(400);
    thermostatListView.setMaxHeight(200);

    tempField = new TextField();
    configTextFieldForDoubles(tempField);
    tempField.setMaxWidth(100);
    tempField.setId("tempField");

    ObservableList<Unit> options = FXCollections.observableArrayList(Unit.CELSIUS, Unit.FAHRENHEIT);
    metricDropdown = new ComboBox<Unit>(options);
    metricDropdown.getSelectionModel().selectFirst();

    setTempButton.setOnAction(
        actionEvent -> {
          try {
            double temp = controller.convertStringtoDouble(tempField.getText());
            controller.setThermostatTemp(
                thermostatListView.getFocusModel().getFocusedItem(),
                metricDropdown.getSelectionModel().getSelectedItem(),
                temp);
          } catch (NumberFormatException e) {
            setWarningText("You must enter a decimal value for temperature.");
          }
        });

    convertTempButton.setOnAction(
        actionEvent -> {
          controller.convertTemp(thermostatListView.getFocusModel().getFocusedItem());
        });

    onOffButton.setOnAction(
        actionEvent -> {
          controller.setThermostatOnOff(thermostatListView.getFocusModel().getFocusedItem());
          updateOnOffButtonText();
        });

    stimulateClimateChangeButton.setOnAction(
        actionEvent -> {
          if (thermostatListView.getFocusModel().getFocusedItem() == null) {
            setWarningText("A thermostat must be selected to test this feature.");
            return;
          }
          if (!thermostatListView
              .getFocusModel()
              .getFocusedItem()
              .getStatus()
              .equals(Status.FUNCTIONING)) {
            setWarningText("The thermostat must be on to test this feature.");
            return;
          }
          thermostatListView.getFocusModel().getFocusedItem().stimulateClimateChange();
        });

    addRow(0, thermostatListLabel);
    addRow(1, warningText);
    addRow(2, thermostatListView);
    addRow(4, tempField, metricDropdown);
    addRow(5, setTempButton, convertTempButton);
    addRow(6, stimulateClimateChangeButton, onOffButton);
  }

  private void updateSetTempButtonText() {
    Thermostat selected = thermostatListView.getFocusModel().getFocusedItem();
    if (selected == null) {
      setTempButton.setText("No thermostat selected");
    } else {
      setTempButton.setText("Set Temperature");
    }
  }

  private void updateConvertTempButtonText() {
    Thermostat selected = thermostatListView.getFocusModel().getFocusedItem();
    if (selected == null) {
      convertTempButton.setText("No thermostat selected");
    } else {
      convertTempButton.setText("Convert Temperature");
    }
  }

  private void updateOnOffButtonText() {
    Thermostat selected = thermostatListView.getFocusModel().getFocusedItem();
    if (selected == null) {
      onOffButton.setText("No thermostat selected");
    } else if (selected.getStatus().equals(Status.OFFLINE)) {
      onOffButton.setText("Turn on");
    } else {
      onOffButton.setText("Turn off");
    }
  }

  public void setWarningText(String txt) {
    warningText.setText(txt);
  }

  public void setThermostats(ObservableList<Thermostat> thermostats) {
    thermostatListView.setItems(thermostats);
    thermostatListView.refresh();
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

  private void configTextFieldForDoubles(TextField field) {
    field.setTextFormatter(
        new TextFormatter<Double>(
            (Change c) -> {
              if (c.getControlNewText().matches("-?\\d*.?\\d*")) {
                return c;
              }
              return null;
            }));
  }
}
