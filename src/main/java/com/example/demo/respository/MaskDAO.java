package com.example.demo.respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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
  public Mask update(Mask t) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(Mask t) {
    // TODO Auto-generated method stub

  }

  public ArrayList<Mask> getMasksByPharmacy(int idPharmacy, Connection connection){
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
      if(sortedBy!=null && sortedBy.equals("name")) {
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
}
