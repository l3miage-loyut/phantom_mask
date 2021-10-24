package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.Pharmacy;
import com.example.demo.respository.PharmacyDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/pharmacies")
public class PharmacyController {

  @Autowired
  private PharmacyDAO pharmacyDAO;

  @GetMapping("/")
  public List<Pharmacy> getPharmacies() {
    List<Pharmacy> pharmacies = (List<Pharmacy>) pharmacyDAO.getAll();
    return pharmacies;
  }

  @GetMapping("/{id}")
  public Pharmacy getPharmacy(@PathVariable(value = "id") String id) {
    Pharmacy pharmacy = pharmacyDAO.get(Integer.parseInt(id));
    return pharmacy;
  }
  
  // List all pharmacies that are open at a certain time, and on a day of the week if requested
  // ex : {url}/api/pharmacies/open_at?time=11:00&day=TUE
  @GetMapping("/open_at")
  public ArrayList<String> openAt(@RequestParam(required = true) String time, @RequestParam(required = false) String day) {
    ArrayList<String> pharmacies = pharmacyDAO.isOpen(time, day);
    return pharmacies;
  }

  // List all pharmacies that have more or less than x mask products within a price range
  // ex : {url}/api/pharmacies/masks_amount?start=20&end=40&more_than=1&less_than=
  @GetMapping("/masks_amount")
  public ArrayList<String> soldBy(@RequestParam(required = true) String start,
      @RequestParam(required = true) String end, 
      @RequestParam(required = true) String more_than, 
      @RequestParam(required = true) String less_than) {
    if((more_than.equals("") && less_than.equals("")) || (!more_than.equals("") && !less_than.equals(""))) {
      System.out.println("URL not valid");
      return null;
    } else {
      ArrayList<String> pharmacies = pharmacyDAO.masksAmount(start, end, more_than, less_than);
      return pharmacies;
    }
  }

  // Search for pharmacies by name, ranked by relevance to search term
  // ex : {url}/api/pharmacies/search?key=Sav
  @GetMapping("/search")
  public ArrayList<String> relevance(@RequestParam(required = true) String key) {
    ArrayList<String> pharmacies = pharmacyDAO.relevance(key);
    return pharmacies;
  }
}
