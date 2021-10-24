package com.example.demo.controller;

import java.util.List;

import com.example.demo.model.User;
import com.example.demo.respository.UserDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
class UserController {

  @Autowired
  private UserDAO userDAO;

  @GetMapping("/")
  public List<User> getUsers() {
    List<User> users = (List<User>) userDAO.getAll();
    return users;
  }

  @GetMapping("/{id}")
  public User getUser(@PathVariable(value="id") String id) {
    User user = userDAO.get(Integer.parseInt(id));
    return user;
  }
}