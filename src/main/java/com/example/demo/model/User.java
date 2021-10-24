package com.example.demo.model;

import java.util.ArrayList;

public class User {
  private int id;
  private String name;
  private float cash;
  private ArrayList<PurchaseHistory> purchase_histories;

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
  public ArrayList<PurchaseHistory> getPurchase_histories() {
    return purchase_histories;
  }
  public void setPurchase_histories(ArrayList<PurchaseHistory> purchase_histories) {
    this.purchase_histories = purchase_histories;
  }
}