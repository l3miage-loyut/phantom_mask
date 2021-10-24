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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MaskDAO implements DAO<Mask> {

  @Autowired
  protected DataSource dataSource;

  @Override
  public Mask get(int id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ArrayList<Mask> getAll() {
    // TODO Auto-generated method stub
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM mask");
      ArrayList<Mask> masks = new ArrayList<Mask>();
      while (rs.next()) {
        Mask mask = new Mask();
        mask.setId(rs.getInt("id"));
        mask.setName(rs.getString("name"));
        mask.setPrice(rs.getFloat("price"));
        masks.add(mask);
      }
      return masks;
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public Mask save(Mask t) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Mask update(Mask t, Connection connection) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(Mask t) {
    // TODO Auto-generated method stub

  }

  public ArrayList<Mask> getMasksByPharmacy(int idPharmacy, Connection connection) {
    try {
      PreparedStatement stmt = connection.prepareStatement(
          "SELECT m.id, m.\"name\", m.price FROM mask m JOIN pharmacy p ON m.idpharmacy = p.id WHERE idpharmacy = ?");
      stmt.setInt(1, idPharmacy);
      ResultSet rs = stmt.executeQuery();

      ArrayList<Mask> masks = new ArrayList<Mask>();

      while (rs.next()) {
        Mask mask = new Mask();
        mask.setId(rs.getInt("id"));
        mask.setName(rs.getString("name"));
        mask.setPrice(rs.getFloat("price"));

        masks.add(mask);
      }
      return masks;
    } catch (Exception e) {
      return null;
    }
  }

  public ArrayList<Mask> soldBy(String pharmacy, String sortedBy) {
    try {
      Connection connection = dataSource.getConnection();
      PreparedStatement stmt;
      if (sortedBy != null && sortedBy.equals("name")) {
        System.out.println("name");
        stmt = connection.prepareStatement(
            "SELECT DISTINCT m.id id, m.\"name\" maskName, price FROM mask m JOIN pharmacy p ON m.idpharmacy = p.id WHERE p.\"name\" = ? ORDER BY maskName;");
      } else if (sortedBy != null && sortedBy.equals("price")) {
        stmt = connection.prepareStatement(
            "SELECT DISTINCT m.id id, m.\"name\" maskName, price FROM mask m JOIN pharmacy p ON m.idpharmacy = p.id WHERE p.\"name\" = ? ORDER BY price;");
      } else {
        stmt = connection.prepareStatement(
            "SELECT DISTINCT m.id id, m.\"name\" maskName, price FROM mask m JOIN pharmacy p ON m.idpharmacy = p.id WHERE p.\"name\" = ?;");
      }
      stmt.setString(1, pharmacy);
      ResultSet rs = stmt.executeQuery();

      ArrayList<Mask> masks = new ArrayList<Mask>();

      while (rs.next()) {
        Mask mask = new Mask();
        mask.setId(rs.getInt("id"));
        mask.setName(rs.getString("maskName"));
        mask.setPrice(rs.getFloat("price"));
        masks.add(mask);
      }
      return masks;
    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  public Map<String, Integer> transaction(String start, String end) {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement stmt = connection.prepareStatement(
        "SELECT SUM(amount) totalAmount, SUM(value) totalValue " +
        "FROM (SELECT m.id, SUM(amount) as amount, SUM(amount)* m.price as value " +
        "FROM purchase_history ph JOIN mask m ON ph.idmask = m.id " +
        "WHERE ph.transactiondate BETWEEN Cast(? as timestamp) and Cast(? as timestamp) " +
        "GROUP BY m.id ) AS byItem");
      stmt.setString(1, start + " 00:00:00");
      stmt.setString(2, end + " 23:59:59");
      ResultSet rs = stmt.executeQuery();

      Map<String, Integer> map = new HashMap<>();

      if (rs.next()) {
        map.put("total amount", rs.getInt("totalAmount"));
        map.put("total value", rs.getInt("totalValue"));
      }
      return map;
    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  public ArrayList<String> relevance(String key) {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT \"name\" FROM mask WHERE \"name\" LIKE ?");
      stmt.setString(1, "%" + key + "%");
      ResultSet rs = stmt.executeQuery();

      ArrayList<String> masks = new ArrayList<String>();

      while (rs.next()) {
        masks.add(rs.getString("name"));
      }
      return masks;
    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  public Mask maskExist(String mask, String pharmacy, Connection connection) {
    try {
      PreparedStatement stmt = connection.prepareStatement("SELECT m.id, m.\"name\", m.price " +
      "FROM mask m JOIN pharmacy p ON m.idpharmacy = p.id " +
      "WHERE m.\"name\" = ? and p.\"name\" = ?");
      stmt.setString(1, mask);
      stmt.setString(2, pharmacy);
      ResultSet rs = stmt.executeQuery();

      Mask findMask = new Mask();

      if (rs.next()) {
        findMask.setId(rs.getInt("id"));
        findMask.setName(rs.getString("name"));
        findMask.setPrice(rs.getFloat("price"));
      }
      return findMask;
    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }
}
