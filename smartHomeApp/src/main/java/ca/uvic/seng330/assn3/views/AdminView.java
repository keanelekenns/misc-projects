package ca.uvic.seng330.assn3.views;

import ca.uvic.seng330.assn3.controllers.AdminController;
import ca.uvic.seng330.assn3.models.devices.DeviceType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class AdminView extends GridPane {
  private TextField deviceNameField;
  private Label infoLabel;
  private ComboBox<DeviceType> deviceTypeSelector;
  private ComboBox<String> userLinkField;
  private TextField userNameField;
  private TextField passwordField;
  private ComboBox<String> adminSelector;

  private AdminController controller;

  public AdminView() {

    createAndConfigurePane();

    createAndLayoutControls();
  }

  public void setController(AdminController controller) {
    this.controller = controller;
  }

  private void createAndLayoutControls() {

    Label title = new Label("ADMIN CONTROLS:");

    infoLabel = new Label();

    deviceNameField = new TextField();
    deviceNameField.setMaxWidth(100);
    deviceNameField.setId("deviceNameField");

    userNameField = new TextField();
    userNameField.setMaxWidth(100);
    userNameField.setId("userNameField");

    passwordField = new TextField();
    passwordField.setId("passwordField");
    passwordField.setMaxWidth(100);

    ObservableList<String> adminOptions = FXCollections.observableArrayList("Admin", "Basic");
    adminSelector = new ComboBox<String>(adminOptions);
    adminSelector.getSelectionModel().selectFirst();

    Button createUserButton = new Button("Create User");
    createUserButton.setId("createUserButton");

    createUserButton.setOnAction(
        actionEvent ->
            controller.createNewUser(
                userNameField.getText(),
                passwordField.getText(),
                adminSelector.getValue().equals("Admin")));

    Button removeUserButton = new Button("Remove User");
    removeUserButton.setId("removeUserButton");

    removeUserButton.setOnAction(
        actionEvent -> controller.removeUser(userNameField.getText(), passwordField.getText()));

    ObservableList<DeviceType> deviceOptions =
        FXCollections.observableArrayList(
            DeviceType.CAMERA, DeviceType.THERMOSTAT, DeviceType.SMARTPLUG, DeviceType.LIGHTBULB);
    deviceTypeSelector = new ComboBox<DeviceType>(deviceOptions);
    deviceTypeSelector.getSelectionModel().selectFirst();

    Button createDeviceButton = new Button("Create device");
    createDeviceButton.setOnAction(
        actionEvent ->
            controller.createNewDevice(
                deviceNameField.getText(),
                deviceTypeSelector.getSelectionModel().getSelectedItem()));

    Button removeDeviceButton = new Button("Remove device");
    removeDeviceButton.setOnAction(
        actionEvent ->
            controller.removeDevice(
                deviceNameField.getText(),
                deviceTypeSelector.getSelectionModel().getSelectedItem()));

    userLinkField = new ComboBox<String>();
    userLinkField.setMaxWidth(100);
    userLinkField.setId("userLinkField");

    TextField deviceLinkField = new TextField();
    deviceLinkField.setId("deviceLinkField");
    deviceLinkField.setMaxWidth(100);

    ObservableList<String> notificationOptions =
        FXCollections.observableArrayList("All", "None", "Custom");
    ComboBox<String> notificationSelector = new ComboBox<String>(notificationOptions);
    notificationSelector.getSelectionModel().selectFirst();

    ComboBox<DeviceType> deviceLinkSelector = new ComboBox<DeviceType>(deviceOptions);
    deviceLinkSelector.getSelectionModel().selectFirst();

    Button setNotificationButton = new Button("Set Notifications");
    setNotificationButton.setId("setNotificationButton");

    setNotificationButton.setOnAction(
        actionEvent ->
            controller.setNotification(
                userLinkField.getSelectionModel().getSelectedItem(),
                deviceLinkField.getText(),
                deviceLinkSelector.getSelectionModel().getSelectedItem(),
                notificationSelector.getSelectionModel().getSelectedItem()));

    add(title, 0, 1);
    add(infoLabel, 1, 1);
    add(new Label("Manage Devices:"), 0, 2);
    add(new Label("Device Name:"), 0, 3);
    add(deviceNameField, 1, 3);
    add(new Label("Device Type:"), 0, 4);
    add(deviceTypeSelector, 1, 4);
    add(createDeviceButton, 0, 5);
    add(removeDeviceButton, 1, 5);
    add(new Label("Set User Notifications:"), 2, 2);
    add(new Label("User Name:"), 2, 3);
    add(userLinkField, 3, 3);
    add(new Label("Notified by:"), 4, 3);
    add(notificationSelector, 5, 3);
    add(new Label("Device Name:"), 2, 4);
    add(deviceLinkField, 3, 4);
    add(new Label("Device Type:"), 4, 4);
    add(deviceLinkSelector, 5, 4);
    add(setNotificationButton, 5, 5);
    add(new Label("\t\t"), 6, 2); // for spacing
    add(new Label("Manage Users:"), 7, 2);
    add(new Label("User Name:"), 7, 3);
    add(userNameField, 8, 3);
    add(new Label("Password:"), 7, 4);
    add(passwordField, 8, 4);
    add(adminSelector, 9, 4);
    add(createUserButton, 8, 5);
    add(removeUserButton, 9, 5);
  }

  private void createAndConfigurePane() {

    setPadding(new Insets(10, 10, 10, 10));

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

  public void updateUsers(ObservableList<String> users) {
    userLinkField.setItems(users);
    userLinkField.getSelectionModel().selectFirst();
  }

  public void setInfoLabel(String txt) {
    infoLabel.setText(txt);
  }

  public String getInfoLabel() {
    return infoLabel.getText();
  }
}
