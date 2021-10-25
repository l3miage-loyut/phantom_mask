package com.example.demo.respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;

import com.example.demo.model.Pharmacy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PharmacyDAO implements DAO<Pharmacy>{

  @Autowired
  protected DataSource dataSource;

  @Override
  public Pharmacy get(int id) {
    // TODO Auto-generated method stub
    try {
      Connection connection = dataSource.getConnection();
      PreparedStatement stmt = connection.prepareStatement("SELECT * FROM pharmacy WHERE id = ?");
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();

      Pharmacy pharmacy = new Pharmacy();

      if (rs.next()) {
        pharmacy.setId(rs.getInt("id"));
        pharmacy.setName(rs.getString("name"));
        pharmacy.setCash(rs.getFloat("cash"));

        OpeningHourDAO openingHourDAO = new OpeningHourDAO();
        pharmacy.setOpening_hours(openingHourDAO.getHoursByPharmacy(pharmacy.getId(), connection));
        
        MaskDAO maskDAO = new MaskDAO();
        pharmacy.setMasks(maskDAO.getMasksByPharmacy(pharmacy.getId(), connection));
      }
      connection.close();
      return pharmacy;
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public Pharmacy get(int id, Connection connection) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ArrayList<Pharmacy> getAll() {
    // TODO Auto-generated method stub
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM pharmacy");
      ArrayList<Pharmacy> pharmacies = new ArrayList<Pharmacy>();
      
      while (rs.next()) {
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setId(rs.getInt("id"));
        pharmacy.setName(rs.getString("name"));
        pharmacy.setCash(rs.getFloat("cash"));

        //OpeningHourDAO openingHourDAO = new OpeningHourDAO();
        //pharmacy.setOpening_hours(openingHourDAO.getHoursByPharmacy(pharmacy.getId()));
        
        //MaskDAO maskDAO = new MaskDAO();
        //pharmacy.setMasks(maskDAO.getMasksByPharmacy(pharmacy.getId(), connection));

        pharmacies.add(pharmacy);
      }
      connection.close();
      return pharmacies;
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public Pharmacy save(Pharmacy t) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Pharmacy save(Pharmacy t, Connection connection) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Pharmacy update(Pharmacy t) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Pharmacy update(Pharmacy t, Connection connection) {
    // TODO Auto-generated method stub
    try {
      PreparedStatement stmt = connection.prepareStatement("UPDATE pharmacy SET name = ?, cash = ? WHERE id = ?");
      stmt.setString(1, t.getName());
      stmt.setFloat(2, t.getCash());
      stmt.setInt(3, t.getId());
      stmt.executeUpdate();
      
      return t;
      
    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  @Override
  public void delete(Pharmacy t) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void delete(Pharmacy t, Connection connection) {
    // TODO Auto-generated method stub

  }

  public ArrayList<String> isOpen(String time, String day) {
    try {
      Connection connection = dataSource.getConnection();
      PreparedStatement stmt ;
      if(day==null || day.equals("")) {
        stmt = connection.prepareStatement(
            "SELECT DISTINCT name FROM opening_hour oh JOIN pharmacy p ON oh.idpharmacy = p.id WHERE (open_at< Cast(? as time) and close_at>Cast(? as time))");
      } else {
        stmt = connection.prepareStatement(
            "SELECT DISTINCT name FROM opening_hour oh JOIN pharmacy p ON oh.idpharmacy = p.id WHERE (open_at< Cast(? as time) and close_at>Cast(? as time) and day=Cast(? as TypeDay))");
        stmt.setString(3, day);
      }
      stmt.setString(1, time);
      stmt.setString(2, time);
      ResultSet rs = stmt.executeQuery();

      ArrayList<String> pharmacies = new ArrayList<String>();

      while (rs.next()) {
        pharmacies.add(rs.getString("name"));
      }
      connection.close();
      return pharmacies;
    } catch (Exception e) {
      return null;
    }
  }

  public ArrayList<String> masksAmount(String start, String end, String more_than, String less_than) {
    try {
      Connection connection = dataSource.getConnection();
      PreparedStatement stmt = null;
      if(more_than!=null) {
        // case more than
        stmt = connection.prepareStatement(
          "SELECT p.\"name\" pharmacy, COUNT(m.id) masks FROM mask m JOIN pharmacy p ON m.idpharmacy = p.id WHERE m.price>? and m.price<? GROUP BY p.\"name\" HAVING COUNT(m.id)>?");
        stmt.setInt(3, Integer.parseInt(more_than));
      }
      if(less_than!=null) {
        // case less than
        stmt = connection.prepareStatement(
          "SELECT p.\"name\" pharmacy, COUNT(m.id) masks FROM mask m JOIN pharmacy p ON m.idpharmacy = p.id WHERE m.price>? and m.price<? GROUP BY p.\"name\" HAVING COUNT(m.id)<?");
        stmt.setInt(3, Integer.parseInt(less_than));
      }
      stmt.setInt(1, Integer.parseInt(start));
      stmt.setInt(2, Integer.parseInt(end));
      ResultSet rs = stmt.executeQuery();

      ArrayList<String> pharmacies = new ArrayList<String>();

      while (rs.next()) {
        pharmacies.add(rs.getString("pharmacy"));
      }
      connection.close();
      return pharmacies;
    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  public ArrayList<String> relevance(String key) {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT \"name\" FROM pharmacy WHERE \"name\" LIKE ?");
      stmt.setString(1, "%" + key + "%");
      ResultSet rs = stmt.executeQuery();

      ArrayList<String> pharmacies = new ArrayList<String>();

      while (rs.next()) {
        pharmacies.add(rs.getString("name"));
      }
      connection.close();
      return pharmacies;
    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  public Pharmacy getPharmacyByName(String pharmacyName, Connection connection) {
    try {
      PreparedStatement stmt = connection.prepareStatement("SELECT * FROM pharmacy WHERE \"name\" = ?");
      stmt.setString(1, pharmacyName);
      ResultSet rs = stmt.executeQuery();

      Pharmacy pharmacy = new Pharmacy();
      if (rs.next()) {
        pharmacy.setId(rs.getInt("id"));
        pharmacy.setName(rs.getString("name"));
        pharmacy.setCash(rs.getFloat("cash"));
      }
      return pharmacy;
    } catch (Exception e) {
      return null;
    }
  }
}
