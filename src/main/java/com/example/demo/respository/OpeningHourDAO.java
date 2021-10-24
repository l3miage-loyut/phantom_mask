package com.example.demo.respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.sql.DataSource;

import com.example.demo.model.OpeningHour;
import com.example.demo.model.TypeDay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OpeningHourDAO implements DAO<OpeningHour>{

  @Autowired
  protected DataSource dataSource;

  @Override
  public OpeningHour get(int id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ArrayList<OpeningHour> getAll() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public OpeningHour save(OpeningHour t) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public OpeningHour update(OpeningHour t) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(OpeningHour t) {
    // TODO Auto-generated method stub
    
  }

  public ArrayList<OpeningHour> getHoursByPharmacy(int idPharmacy, Connection connection){
    try {
      PreparedStatement stmt = connection.prepareStatement(
          "SELECT oh.id, open_at, close_at, day FROM opening_hour oh JOIN pharmacy p ON oh.idpharmacy =p.id WHERE idpharmacy = ?");
      stmt.setInt(1, idPharmacy);
      ResultSet rs = stmt.executeQuery();

      ArrayList<OpeningHour> openingHours= new ArrayList<OpeningHour>();

      while (rs.next()) {
        OpeningHour openingHour = new OpeningHour();
        openingHour.setId(rs.getInt("id"));
        openingHour.setOpen_at(rs.getTime("open_at"));
        openingHour.setClose_at(rs.getTime("close_at"));
        openingHour.setDay(TypeDay.valueOf(rs.getString("day")));

        openingHours.add(openingHour);
      }
      return openingHours;
    } catch (Exception e) {
      return null;
    }
  }
}
