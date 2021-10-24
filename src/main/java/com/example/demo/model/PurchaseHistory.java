package com.example.demo.model;

import java.sql.Timestamp;

public class PurchaseHistory {
  private int id;
  //public int idUser;
  //public int idPharmacy;
  //public int idMask;
  //public String user;
  private String pharmacy;
  private String mask;
  private float amount;
  private Timestamp transactionDate;

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getPharmacy() {
    return pharmacy;
  }
  public void setPharmacy(String pharmacy) {
    this.pharmacy = pharmacy;
  }
  public String getMask() {
    return mask;
  }
  public void setMask(String mask) {
    this.mask = mask;
  }
  public float getAmount() {
    return amount;
  }
  public void setAmount(float amount) {
    this.amount = amount;
  }
  public Timestamp getTransactionDate() {
    return transactionDate;
  }
  public void setTransactionDate(Timestamp transactionDate) {
    this.transactionDate = transactionDate;
  }
}
