package ca.uvic.seng330.assn3.models;

public class CurrentUser {
  private User aUser;

  public void setCurrentUser(User pUser) {
    if (pUser == null) {
      clearCurrentUser();
    } else {
      aUser = pUser;
    }
  }

  public User getCurrentUser() {
    return aUser;
  }

  public boolean isLoggedIn() {
    return aUser != null;
  }

  public boolean isAdmin() {
    return aUser != null && aUser.isAdmin();
  }

  public void clearCurrentUser() {
    aUser = null;
  }
}
