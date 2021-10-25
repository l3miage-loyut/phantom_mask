package com.example.demo.respository;

import java.sql.Connection;
import java.util.ArrayList;

public interface DAO<T> {

  T get(int id);
  
  T get(int id, Connection connection);

  ArrayList<T> getAll();

  T save(T t);
  
  T save(T t, Connection connection);

  T update(T t);

  T update(T t, Connection connection);

  void delete(T t);

  void delete(T t, Connection connection);

}
