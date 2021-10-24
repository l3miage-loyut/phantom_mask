package com.example.demo.respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.sql.DataSource;

import com.example.demo.model.Pharmacy;
import com.example.demo.model.PurchaseHistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PurchaseHistoryDAO implements DAO<PurchaseHistory> {

  @Autowired
  protected DataSource dataSource;

  @Override
  public PurchaseHistory get(int id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ArrayList<PurchaseHistory> getAll() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PurchaseHistory save(PurchaseHistory t) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PurchaseHistory update(PurchaseHistory t, Connection connection) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(PurchaseHistory t) {
    // TODO Auto-generated method stub
    
  }

  public ArrayList<PurchaseHistory> getHistoriesByUser(int idUser, Connection connection) {
    try {
      PreparedStatement stmt = connection.prepareStatement(
          "SELECT ph.id id, m.\"name\" mask, p.\"name\" pharmacy, amount, transactionDate FROM purchase_history ph JOIN pharmacy p ON p.id = ph.idPharmacy JOIN mask m ON m.id=ph.idMask WHERE ph.idUser = ? ORDER BY id");
      stmt.setInt(1, idUser);
      ResultSet rs = stmt.executeQuery();

      ArrayList<PurchaseHistory> purchase_histories = new ArrayList<PurchaseHistory>();

      while (rs.next()) {
        PurchaseHistory purchase_history = new PurchaseHistory();
        purchase_history.setId(rs.getInt("id"));
        purchase_history.setPharmacy(rs.getString("pharmacy"));
        purchase_history.setMask(rs.getString("mask"));
        purchase_history.setAmount(rs.getFloat("amount"));
        purchase_history.setTransactionDate(rs.getTimestamp("transactionDate"));
        purchase_histories.add(purchase_history);
      }
      return purchase_histories;
    } catch (Exception e) {
      return null;
    }
  }

  public int save(PurchaseHistory ph, int idUser, int idMask, Connection connection) {
    try {
      PreparedStatement stmt = connection.prepareStatement(
          "INSERT INTO purchase_history (idUser, idPharmacy, idMask, amount, transactionDate) VALUES (?, ?, ?, ?, ?)");
      stmt.setInt(1, idUser);

      PharmacyDAO pharmacyDAO = new PharmacyDAO();
      Pharmacy pharmacy = pharmacyDAO.getPharmacyByName(ph.getPharmacy(), connection);
      stmt.setInt(2, pharmacy.getId());

      stmt.setInt(3, idMask);
      stmt.setFloat(4, ph.getAmount());
      stmt.setTimestamp(5, ph.getTransactionDate());
      return stmt.executeUpdate();
    } catch (Exception e) {
      System.out.println(e);
      return -1;
    }
  }
}
