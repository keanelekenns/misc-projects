package ca.uvic.seng330.assn3.views;

import ca.uvic.seng330.assn3.controllers.AuthenticationController;
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

public class AuthenticationView extends GridPane {
  private TextField nameField;
  private TextField passwordField;
  private Label warningText;
  private ComboBox<String> adminSelector;

  private AuthenticationController controller;

  public AuthenticationView() {

    createAndConfigurePane();

    createAndLayoutControls();
  }

  public void setController(AuthenticationController controller) {
    this.controller = controller;
  }

  private void createAndLayoutControls() {

    warningText = new Label();
    warningText.setId("warnText");
    nameField = new TextField();
    nameField.setMaxWidth(100);
    nameField.setId("nameField");

    ObservableList<String> options = FXCollections.observableArrayList("Admin", "Basic");
    adminSelector = new ComboBox<String>(options);
    adminSelector.getSelectionModel().selectFirst();

    passwordField = new TextField();
    passwordField.setId("passwordField");
    passwordField.setMaxWidth(100);

    Button loginButton = new Button("Login");
    loginButton.setId("loginButton");
    Button signupButton = new Button("Signup");
    signupButton.setId("signupButton");
    loginButton.setOnAction(
        actionEvent -> controller.loginUser(nameField.getText(), passwordField.getText()));
    signupButton.setOnAction(
        actionEvent ->
            controller.createNewUser(
                nameField.getText(),
                passwordField.getText(),
                adminSelector.getValue().equals("Admin")));

    addRow(0, warningText);
    addRow(1, new Label("Name:"), nameField);
    addRow(2, new Label("Password:"), passwordField);
    addRow(3, signupButton, adminSelector);
    addRow(4, loginButton);

    this.setStyle("padding-left: 100px;");
  }

  public void setWarningText(String txt) {
    warningText.setText(txt);
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
}
