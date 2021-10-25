package com.example.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.Pharmacy;
import com.example.demo.model.TypeDay;
import com.example.demo.respository.PharmacyDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<List<Pharmacy>> getPharmacies() {
    List<Pharmacy> pharmacies = (List<Pharmacy>) pharmacyDAO.getAll();
    if (pharmacies.size() > 0) {
      return ResponseEntity.ok().body(pharmacies);
    }
    return ResponseEntity.badRequest().body(null);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Pharmacy> getPharmacy(@PathVariable(value = "id") String id) {
    Pharmacy pharmacy = pharmacyDAO.get(Integer.parseInt(id));
    if (pharmacy.getId()>0) {
      return ResponseEntity.ok().body(pharmacy);
    }
    return ResponseEntity.badRequest().body(null);
  }

  // List all pharmacies that are open at a certain time, and on a day of the week if requested
  // ex : {url}/api/pharmacies/open_at?time=11:00&day=TUE
  @GetMapping("/open_at")
  public ResponseEntity<ArrayList<String>> openAt(@RequestParam(required = true) String time,
      @RequestParam(required = false) String day) {
    ArrayList<String> result = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    try {
      sdf.parse(time);
      if(day!=null && day.length()>0) {
        TypeDay.valueOf(day);
      }
    } catch (ParseException e) {
      //e.printStackTrace();
      result.add("Time not valid");
      return ResponseEntity.badRequest().body(result);
    } catch (IllegalArgumentException e) {
      //e.printStackTrace();
      result.add("Day not valid");
      return ResponseEntity.badRequest().body(result);
    }
    result = pharmacyDAO.isOpen(time, day);
    if(result.size()>0) {
      return ResponseEntity.ok().body(result);
    }
    result.add("No pharmacy found");
    return ResponseEntity.badRequest().body(result);
  }

  // List all pharmacies that have more than x mask products within a price range
  // ex : {url}/api/pharmacies/masks_more_than?amount=1&start=20&end=40
  @GetMapping("/masks_more_than")
  public ResponseEntity<ArrayList<String>> soldByMore(@RequestParam(required = true) String amount,
      @RequestParam(required = true) String start, @RequestParam(required = true) String end) {
    ArrayList<String> result = pharmacyDAO.masksAmount(start, end, amount, null);
    if(result.size()>0) {
      return ResponseEntity.ok().body(result);
    }
    result.add("No pharmacy found");
    return ResponseEntity.badRequest().body(result);
  }

  // List all pharmacies that have less than x mask products within a price range
  // ex : {url}/api/pharmacies/masks_less_than?amount=3&start=20&end=40
  @GetMapping("/masks_less_than")
  public ResponseEntity<ArrayList<String>> soldByLess(@RequestParam(required = true) String amount,
      @RequestParam(required = true) String start, @RequestParam(required = true) String end) {
    ArrayList<String> result = pharmacyDAO.masksAmount(start, end, null, amount);
    if (result.size() > 0) {
      return ResponseEntity.ok().body(result);
    }
    result.add("No pharmacy found");
    return ResponseEntity.badRequest().body(result);
  }

  // Search for pharmacies by name, ranked by relevance to search term
  // ex : {url}/api/pharmacies/search?key=Sav
  @GetMapping("/search")
  public ResponseEntity<ArrayList<String>> relevance(@RequestParam(required = true) String key) {
    ArrayList<String> result = pharmacyDAO.relevance(key);
    if (result.size() > 0) {
      return ResponseEntity.ok().body(result);
    }
    result.add("No pharmacy found");
    return ResponseEntity.badRequest().body(result);
  }
}
