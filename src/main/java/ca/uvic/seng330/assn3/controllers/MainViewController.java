package ca.uvic.seng330.assn3.controllers;

import ca.uvic.seng330.assn3.views.MainView;

public class MainViewController {
  private MainView view;
  private ActivityLogController aLogController;
  private AuthenticationController aAuthenticationController;
  private AdminController aAdminController;
  private BasicUserController aBasicUserController;

  public MainViewController(
      MainView view,
      ActivityLogController pLogController,
      AuthenticationController pAuthenticationController,
      AdminController pAdminController,
      BasicUserController pBasicUserController) {
    this.view = view;
    this.view.setController(this);
    aLogController = pLogController;
    aAuthenticationController = pAuthenticationController;
    aAdminController = pAdminController;
    aBasicUserController = pBasicUserController;
  }

  public void setView(MainView view) {
    this.view = view;
  }

  public MainView getView() {
    return view;
  }

  public void showAuthenticationView() {
    view.setContent(aAuthenticationController.getView());
    view.setRight(null);
    view.setBottom(null);
  }

  public void showAdminView() {
    aBasicUserController.updateDevicesInView();
    view.setContent(aBasicUserController.getView());
    view.setRight(aLogController.getView());
    view.setBottom(aAdminController.getView());
  }

  public void showBasicUserView() {
    aBasicUserController.updateDevicesInView();
    view.setContent(aBasicUserController.getView());
    view.setRight(null);
    view.setBottom(null);
  }
}
