package com.example.demo.respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;

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
      return user;
    } catch (Exception e) {
      return null;
    }
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

        //PurchaseHistoryDAO purchaseHistorydDao = new PurchaseHistoryDAO();
        //user.setPurchase_histories(purchaseHistorydDao.getHistoriesByUser(user.getId(), connection));

        users.add(user);
      }
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
  public User update(User t) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(User t) {
    // TODO Auto-generated method stub
    
  }
  
}
