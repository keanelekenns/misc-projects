package ca.uvic.seng330.assn3.controllersTest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import ca.uvic.seng330.assn3.controllers.ActivityLogController;
import ca.uvic.seng330.assn3.controllers.AuthenticationController;
import ca.uvic.seng330.assn3.controllers.MainViewController;
import ca.uvic.seng330.assn3.controllers.UserController;
import ca.uvic.seng330.assn3.models.CurrentUser;
import ca.uvic.seng330.assn3.views.AuthenticationView;
import javafx.embed.swing.JFXPanel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class AuthenticationControllerTest {
  private AuthenticationController controller;
  private UserController userController;
  private CurrentUser currentUser;

  @Mock private MainViewController mvc;

  @Mock private AuthenticationView view;

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Before
  public void setUpData() {
    JFXPanel p = new JFXPanel(); // DO NOT REMOVE used to prevent javafx error
    view = mock(AuthenticationView.class);
    mvc = mock(MainViewController.class);
    userController = new UserController();
    currentUser = new CurrentUser();
    controller =
        new AuthenticationController(
            view, currentUser, userController, mock(ActivityLogController.class));
    controller.setMainViewController(mvc);
    view.setController(controller);
  }

  @Test
  public void shouldCreateNewUser() {
    controller.createNewUser("Dana", "1234", false);
    assertTrue(userController.getUsers().get("Dana") != null);
  }

  @Test
  public void shouldCreateAdminUser() {
    controller.createNewUser("admin", "1234", true);
    assertTrue(userController.getUsers().get("admin").isAdmin() == true);
  }

  @Test
  public void shouldCreateBasicUser() {
    controller.createNewUser("basic", "1234", false);
    assertTrue(userController.getUsers().get("basic").isAdmin() == false);
  }

  @Test
  public void shouldSetWarningIfNameIsBlank() {
    controller.createNewUser("", "1234", false);
    verify(view, times(1)).setWarningText("Invalid name or password, name:  password: 1234");
  }

  @Test
  public void shouldSetWarningIfPasswordIsBlank() {
    controller.createNewUser("d", "", false);
    verify(view, times(1)).setWarningText("Invalid name or password, name: d password: ");
  }

  @Test
  public void shouldSetWarningIfNameAlreadyInUse() {
    controller.createNewUser("d", "1234", false);
    controller.createNewUser("d", "1234", false);
    verify(view, times(1)).setWarningText("User name: d, already exists.");
  }

  @Test
  public void shouldLoginAsAdminIfUserIsAdmin() {
    controller.createNewUser("admin", "1234", true);
    controller.loginUser("admin", "1234");
    verify(mvc, times(1)).showAdminView();
    assertTrue(currentUser.getCurrentUser().isAdmin() == true);
  }

  @Test
  public void shouldLoginAsBasicUserIfUserIsNotAdmin() {
    controller.createNewUser("notadmin", "1234", false);
    controller.loginUser("notadmin", "1234");
    verify(mvc, times(1)).showBasicUserView();
    assertTrue(currentUser.getCurrentUser().isAdmin() == false);
  }

  @Test
  public void shouldShowWarningIfPasswordIsWrong() {
    controller.createNewUser("w", "1234", false);
    controller.loginUser("w", "wrong");
    verify(view, times(1)).setWarningText("Incorrect password entered, password: wrong");
    verify(mvc, times(0)).showAdminView();
    verify(mvc, times(0)).showBasicUserView();
  }

  @Test
  public void shouldShowWarningIfUsernameIsWrong() {
    controller.createNewUser("w", "1234", false);
    controller.loginUser("wrongwrong", "1234");
    verify(view, times(1)).setWarningText("Username does not exist: wrongwrong");
    verify(mvc, times(0)).showAdminView();
    verify(mvc, times(0)).showBasicUserView();
  }
}
