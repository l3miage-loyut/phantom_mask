package com.example.demo.model;

import java.util.ArrayList;

public class Pharmacy {
  private int id;
  private String name;
  private float cash;
  private ArrayList<OpeningHour> opening_hours;
  private ArrayList<Mask> masks;

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public float getCash() {
    return cash;
  }
  public void setCash(float cash) {
    this.cash = cash;
  }
  public ArrayList<OpeningHour> getOpening_hours() {
    return opening_hours;
  }
  public void setOpening_hours(ArrayList<OpeningHour> opening_hours) {
    this.opening_hours = opening_hours;
  }
  public ArrayList<Mask> getMasks() {
    return masks;
  }
  public void setMasks(ArrayList<Mask> masks) {
    this.masks = masks;
  }
}
