package com.example.demo.respository;

import java.sql.Connection;
import java.util.ArrayList;

public interface DAO<T> {
  T get(int id);
  ArrayList<T> getAll();
  T save(T t);
  T update(T t, Connection connection);
  void delete(T t);
}
