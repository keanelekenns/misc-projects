package ca.uvic.seng330.assn3.views;

import ca.uvic.seng330.assn3.controllers.BasicUserController;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class BasicUserView extends ScrollPane {
  private BasicUserController controller;
  private Button logoutButton;
  private VBox content;
  private Label infoLabel;

  public BasicUserView() {
    createAndConfigurePane();

    createAndLayoutControls();
  }

  public void setController(BasicUserController controller) {
    this.controller = controller;
  }

  private void createAndLayoutControls() {
    logoutButton = new Button("Logout");
    logoutButton.setId("logoutButton");
    logoutButton.setOnAction(actionEvent -> controller.logoutCurrentUser());
    Button shutdownButton = new Button("Shut Down");
    shutdownButton.setId("shutdownButton");
    shutdownButton.setOnAction(actionEvent -> controller.shutdownSystem());
    infoLabel = new Label();
    content.getChildren().addAll(logoutButton, shutdownButton, infoLabel);
  }

  public void addView(Node view) {
    content.getChildren().add(view);
  }

  public void setInfoLabel(String txt) {
    infoLabel.setText(txt);
  }

  private void createAndConfigurePane() {
    this.setHeight(600);
    this.setMaxHeight(600);
    this.setMaxWidth(1000);
    this.setWidth(600);
    content = new VBox();
    this.setContent(content);
  }
}
