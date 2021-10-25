package com.example.demo.respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.example.demo.model.Mask;
import com.example.demo.model.Pharmacy;
import com.example.demo.model.PurchaseHistory;
import com.example.demo.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO implements DAO<User> {

  @Autowired
  protected DataSource dataSource;

  @Override
  public User get(int id) {
    // TODO Auto-generated method stub
    try {
      Connection connection = dataSource.getConnection();
      PreparedStatement stmt = connection.prepareStatement("SELECT * FROM \"user\" WHERE id = ?");
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();

      User user = new User();
      if (rs.next()) {
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setCash(rs.getFloat("cash"));

        PurchaseHistoryDAO purchaseHistorydDao = new PurchaseHistoryDAO();
        user.setPurchase_histories(purchaseHistorydDao.getHistoriesByUser(user.getId(), connection));
      }
      connection.close();
      return user;
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public User get(int id, Connection connection) {
    // TODO Auto-generated method stub
    return null;
  }

  public ArrayList<User> getAll() {
    // TODO Auto-generated method stub
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM \"user\"");
      ArrayList<User> users = new ArrayList<User>();
      while (rs.next()) {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setCash(rs.getFloat("cash"));

        // PurchaseHistoryDAO purchaseHistorydDao = new PurchaseHistoryDAO();
        // user.setPurchase_histories(purchaseHistorydDao.getHistoriesByUser(user.getId(),
        // connection));

        users.add(user);
      }
      connection.close();
      return users;
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public User save(User t) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public User save(User t, Connection connection) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public User update(User t) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public User update(User t, Connection connection) {
    // TODO Auto-generated method stub
    try {
      PreparedStatement stmt = connection.prepareStatement("UPDATE \"user\" SET name = ?, cash = ? WHERE id = ?");
      stmt.setString(1, t.getName());
      stmt.setFloat(2, t.getCash());
      stmt.setInt(3, t.getId());
      stmt.executeUpdate();

      return get(t.getId());

    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  @Override
  public void delete(User t) {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(User t, Connection connection) {
    // TODO Auto-generated method stub

  }

  public ArrayList<String> topXUsersBetween(String start, String end, String amount) {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement stmt = connection.prepareStatement("SELECT u.\"name\" as userName, SUM(amount) amount "
          + "FROM purchase_history ph JOIN \"user\" u ON ph.iduser = u.id "
          + "WHERE transactiondate BETWEEN Cast(? as timestamp) and Cast(? as timestamp) "
          + "GROUP BY userName ORDER BY amount DESC LIMIT ?");
      stmt.setString(1, start + " 00:00:00");
      stmt.setString(2, end + " 23:59:59");
      stmt.setInt(3, Integer.parseInt(amount));
      ResultSet rs = stmt.executeQuery();

      ArrayList<String> masks = new ArrayList<String>();

      while (rs.next()) {
        masks.add(rs.getString("userName"));
      }
      connection.close();
      return masks;
    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  public Map<String, Number> purchase(String buyer, PurchaseHistory ph) {
    try (Connection connection = dataSource.getConnection()) {
      Map<String, Number> updates = new HashMap<>();
      PurchaseHistoryDAO purchaseHistoryDAO = new PurchaseHistoryDAO();
      PharmacyDAO pharmacyDAO = new PharmacyDAO();

      MaskDAO maskDAO = new MaskDAO();
      Mask findMask = maskDAO.maskExist(ph.getMask(), ph.getPharmacy(), connection);

      if (findMask.getId() <= 0) {
        updates.put(ph.getMask() + " does not exist in " + ph.getPharmacy(), -1);
        connection.close();
        return updates;
      }

      // mask exist in pharmacy indicated
      User user = this.getUserByName(buyer);
      if (user.getId() <= 0) {
        updates.put("user " + buyer + " does not exist", -2);
        connection.close();
        return updates;
      }
      int result = purchaseHistoryDAO.save(ph, user.getId(), findMask.getId(), connection);
      if (result <= 0) {
        updates.put("purchase history create fail", -3);
        connection.close();
        return updates;
      }
      
      updates.put("purchase history create sucessfully", result);

      Float spend = ph.getAmount() * findMask.getPrice();

      // update user's cash
      user.setCash(user.getCash() - spend);
      User newUser = this.update(user, connection);
      updates.put(buyer + "'s cash has updated to", newUser.getCash());

      // update pharmacy's cash
      Pharmacy pharmacy = pharmacyDAO.getPharmacyByName(ph.getPharmacy(), connection);
      pharmacy.setCash(pharmacy.getCash() + spend);
      Pharmacy newPharmacy = pharmacyDAO.update(pharmacy, connection);
      updates.put(newPharmacy.getName() + "'s cash has updated to", newPharmacy.getCash());
      connection.close();
      return updates;
    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  private User getUserByName(String name) {
    try {
      Connection connection = dataSource.getConnection();
      PreparedStatement stmt = connection.prepareStatement("SELECT * FROM \"user\" WHERE \"name\" = ?");
      stmt.setString(1, name);
      ResultSet rs = stmt.executeQuery();

      User user = new User();
      if (rs.next()) {
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setCash(rs.getFloat("cash"));
      }
      connection.close();
      return user;
    } catch (Exception e) {
      return null;
    }
  }
}
