package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.controllers.MainViewController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * Code sample from https://stackoverflow.com/questions/36868391/using-javafx-controller-without-fxml/36873768
 */
public class IOTApp extends Application {

  private Stage primaryStage;
  private SmartHomeControllerFactory aSmartHomeControllerFactory;

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.aSmartHomeControllerFactory = new SmartHomeControllerFactory();

    MainViewController mainViewController = aSmartHomeControllerFactory.getMainViewController();
    mainViewController.showAuthenticationView();

    Scene scene = new Scene(mainViewController.getView(), 1200, 600);
    primaryStage.setScene(scene);

    primaryStage.show();
  }

  public void setSceneToUserScene() {}

  public void setSceneToAdminScene() {}

  public static void main(String[] args) {
    launch(args);
  }
}
