package ca.uvic.seng330.assn3.controllers;

import ca.uvic.seng330.assn3.models.User;
import java.util.HashMap;

public class UserController {

  private HashMap<String, User> aUsers;

  public UserController() {
    aUsers = new HashMap<String, User>();
  }

  public void registerUser(User pUser) throws RegistrationException {
    if (pUser == null) {
      throw new RegistrationException("User reference was null!");
    } else if (aUsers.containsKey(pUser.getName())) {
      throw new RegistrationException("User name: " + pUser.getName() + ", already exists.");
    } else {
      aUsers.put(pUser.getName(), pUser);
    }
  }

  public void unregisterUser(String pName, String pPassword) throws RegistrationException {
    if (!aUsers.containsKey(pName)) {
      throw new RegistrationException("User name: " + pName + ", was not registered to system.");
    } else if (!aUsers.get(pName).isCorrectPassword(pPassword)) {
      throw new RegistrationException("Incorrect password, password: " + pPassword);
    }
    aUsers.remove(pName);
  }

  public HashMap<String, User> getUsers() {
    HashMap<String, User> copyHashMap = new HashMap<String, User>();
    for (User user : aUsers.values()) {
      copyHashMap.put(user.getName(), user.copyUser());
    }
    return copyHashMap;
  }
}
