package ca.uvic.seng330.assn3.models;

import java.util.Date;

public class ActivityLog {
  private Date date;
  private String msg;

  public ActivityLog(String msg) {
    this.msg = msg;
    this.date = new Date();
  }
}
