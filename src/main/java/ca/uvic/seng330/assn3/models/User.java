package ca.uvic.seng330.assn3.models;

public class User {
  private final boolean isAdmin;
  private final String aName;
  private final String aPassword;

  public User(String pName, String pPassword, boolean isAdmin) {

    this.isAdmin = isAdmin;
    aName = pName;
    aPassword = pPassword;
  }

  public User(User pUser) {
    isAdmin = pUser.isAdmin;
    aName = pUser.aName;
    aPassword = pUser.aPassword;
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public String getName() {
    return aName;
  }

  public boolean isCorrectPassword(String pPassword) {
    return aPassword.equals(pPassword);
  }

  public User copyUser() {
    User copyUser = new User(this);
    return copyUser;
  }
}
