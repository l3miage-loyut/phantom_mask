package com.example.demo.model;

import java.sql.Time;

public class OpeningHour {
  private int id;
  //private int idPharmacy;
  //private String pharmacy;
  private Time open_at;
  private Time close_at;
  private TypeDay day;

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public Time getOpen_at() {
    return open_at;
  }
  public void setOpen_at(Time open_at) {
    this.open_at = open_at;
  }
  public Time getClose_at() {
    return close_at;
  }
  public void setClose_at(Time close_at) {
    this.close_at = close_at;
  }
  public TypeDay getDay() {
    return day;
  }
  public void setDay(TypeDay day) {
    this.day = day;
  }
}
