package ca.uvic.seng330.assn3.controllers;

import ca.uvic.seng330.assn3.models.CurrentUser;
import ca.uvic.seng330.assn3.models.User;
import ca.uvic.seng330.assn3.views.AuthenticationView;

public class AuthenticationController {
  private final UserController aUserController;
  private final CurrentUser aCurrentUser;
  private final AuthenticationView aView;
  private final ActivityLogController aLogger;;
  private MainViewController aMainViewController;

  public AuthenticationController(
      AuthenticationView pView,
      CurrentUser pCurrentUser,
      UserController pUserController,
      ActivityLogController pLogger) {
    aUserController = pUserController;
    aLogger = pLogger;
    aView = pView;
    aCurrentUser = pCurrentUser;
  }

  public void createNewUser(String pName, String pPassword, boolean isAdmin) {
    if (pName.isEmpty() || pPassword.isEmpty()) {
      String txt = "Invalid name or password, name: " + pName + " password: " + pPassword;
      aLogger.warn(txt);
      aView.setWarningText(txt);
      return;
    }
    User newUser = new User(pName, pPassword, isAdmin);
    try {
      aUserController.registerUser(newUser);
      aView.setWarningText("New user with name: " + pName + " created successfully");
      aLogger.info("New user with name: " + pName + " created successfully");
    } catch (RegistrationException e) {
      aLogger.warn(e.getMessage());
      aView.setWarningText(e.getMessage());
    }
  }

  public void loginUser(String pName, String pPassword) {
    aView.setWarningText("");
    if (aUserController.getUsers().containsKey(pName)) {
      User user = aUserController.getUsers().get(pName); // not actual User, just copy
      if (user.isCorrectPassword(pPassword)) {
        aCurrentUser.setCurrentUser(user);
        aLogger.info("Successfully logged in as \"" + pName + "\".");
        if (user.isAdmin()) {
          aMainViewController.showAdminView();
        } else {
          aMainViewController.showBasicUserView();
        }
      } else {
        String txt = "Incorrect password entered, password: " + pPassword;
        aLogger.warn(txt);
        aView.setWarningText(txt);
      }
    } else {
      String txt = "Username does not exist: " + pName;
      aLogger.warn(txt);
      aView.setWarningText(txt);
    }
  }

  public AuthenticationView getView() {
    return aView;
  }

  public void setMainViewController(MainViewController pMainViewController) {
    aMainViewController = pMainViewController;
  }
}
