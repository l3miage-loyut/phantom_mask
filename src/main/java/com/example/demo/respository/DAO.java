package com.example.demo.respository;

import java.util.ArrayList;

public interface DAO<T> {
  T get(int id);
  ArrayList<T> getAll();
  T save(T t);
  T update(T t);
  void delete(T t);
}
