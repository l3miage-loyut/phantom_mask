package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.demo.model.PurchaseHistory;
import com.example.demo.model.User;
import com.example.demo.respository.UserDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  // The top x users by total transaction amount of masks within a date range
  // ex : {url}/api/users/most_transaction?top=10&start=2021-01-01&end=2021-01-07
  @GetMapping("/most_transaction")
  public ArrayList<String> soldBy(@RequestParam(required = true) String top,
      @RequestParam(required = true) String start, 
      @RequestParam(required = true) String end) {
    ArrayList<String> users = userDAO.topXUsersBetween(start, end, top);
    return users;
  }

  // Process a user purchases a mask from a pharmacy, and handle all relevant data
  // changes in an atomic transaction
  // ex : {url}/api/users/purchase?buyer=Eric Underwood
  @PostMapping("/purchase")
  public Map<String, Integer> purchase(@RequestParam(required = true) String buyer, @RequestBody PurchaseHistory ph) {
    Map<String, Integer> updates = userDAO.purchase(buyer, ph);
    return updates;
  }
}