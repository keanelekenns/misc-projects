package ca.uvic.seng330.assn3.viewsTest;

import static org.testfx.api.FxAssert.verifyThat;

import ca.uvic.seng330.assn3.SmartHomeControllerFactory;
import ca.uvic.seng330.assn3.controllers.MainViewController;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

public class AuthenticationViewTest extends ApplicationTest {
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

  @Test
  public void shouldFailLoginWithNoUsername() {
    clickOn("#loginButton");
    verifyThat("#warnText", LabeledMatchers.hasText("Username does not exist: "));
  }

  @Test
  public void shouldFailSignUpWithNoUsername() {
    clickOn("#signupButton");
    verifyThat("#warnText", LabeledMatchers.hasText("Invalid name or password, name:  password: "));
    // given:
    // clickOn("#yField").write("33");
    // expect:
    // verifyThat("#yField", TextInputControlMatchers.hasText("33"));
  }

  @Test
  public void shouldFailLoginWhenNotRegistered() {
    clickOn("#nameField").write("Hi Neil");
    clickOn("#loginButton");
    verifyThat("#warnText", LabeledMatchers.hasText("Username does not exist: Hi Neil"));
  }

  @Test
  public void shouldFailLoginWhenIncorrectPassword() {
    clickOn("#nameField").write("admin");
    clickOn("#loginButton");
    verifyThat("#warnText", LabeledMatchers.hasText("Incorrect password entered, password: "));
  }

  @Test
  public void shouldSucceedLoginAndOut() {
    clickOn("#nameField").write("admin");
    clickOn("#passwordField").write("password");
    clickOn("#loginButton");
    clickOn("#logoutButton");
    verifyThat("#loginButton", LabeledMatchers.hasText("Login"));
  }
}
