package com.example.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.demo.model.PurchaseHistory;
import com.example.demo.model.User;
import com.example.demo.respository.UserDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<List<User>> getUsers() {
    List<User> users = (List<User>) userDAO.getAll();
    if(users.size()>0) {
      return ResponseEntity.ok().body(users);
    }
    return ResponseEntity.badRequest().body(null);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUser(@PathVariable(value = "id") String id) {
    User user = userDAO.get(Integer.parseInt(id));
    if(user.getId()>0) {
      return ResponseEntity.ok().body(user);
    }
    return ResponseEntity.badRequest().body(null);
  }

  // The top x users by total transaction amount of masks within a date range
  // ex : {url}/api/users/most_transaction?top=10&start=2021-01-01&end=2021-01-07
  @GetMapping("/most_transaction")
  public ResponseEntity<ArrayList<String>> soldBy(@RequestParam(required = true) String top,
      @RequestParam(required = true) String start, 
      @RequestParam(required = true) String end) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    ArrayList<String> result = new ArrayList<>();
    try {
      sdf.parse(start);
      sdf.parse(end);
    } catch (ParseException e) {
      //e.printStackTrace();
      result.add("Time not valid");
      return ResponseEntity.badRequest().body(result);
    }
    result = userDAO.topXUsersBetween(start, end, top);
    if(result.size()>0) {
      return ResponseEntity.ok().body(result);
    }
    result.add("No user found");
    return ResponseEntity.badRequest().body(result);
  }

  // Process a user purchases a mask from a pharmacy, and handle all relevant data
  // changes in an atomic transaction
  // ex : {url}/api/users/purchase?buyer=Eric Underwood
  @PostMapping("/purchase")
  public ResponseEntity<Map<String, Number>> purchase(@RequestParam(required = true) String buyer, 
      @RequestBody PurchaseHistory ph) {
    Map<String, Number> updates = userDAO.purchase(buyer, ph);
    if(updates.size()==1) {
      return ResponseEntity.badRequest().body(updates);
    }
    if(updates.size()==3) {
      return ResponseEntity.ok().body(updates);
    }
    return ResponseEntity.badRequest().body(null);
  }
}