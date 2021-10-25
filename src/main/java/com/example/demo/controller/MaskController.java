package com.example.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.model.Mask;
import com.example.demo.respository.MaskDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/masks")
public class MaskController {

  @Autowired
  private MaskDAO maskDAO;

  @GetMapping("/")
  public ResponseEntity<List<Mask>> getMasks() {
    List<Mask> masks = (List<Mask>) maskDAO.getAll();
    if (masks.size() > 0) {
      return ResponseEntity.ok().body(masks);
    }
    return ResponseEntity.badRequest().body(null);
  }

  // List all masks that are sold by a given pharmacy, sorted by mask name or mask price
  // ex : {url}/api/masks/sold_by?pharmacy=Cash Saver Pharmacy&sorted_by=name
  @GetMapping("/sold_by")
  public ResponseEntity<ArrayList<Mask>> soldBy(@RequestParam(required = true) String pharmacy,
      @RequestParam(required = false) String sorted_by) {
    ArrayList<Mask> masks = maskDAO.soldBy(pharmacy, sorted_by);
    if(masks.size()>0) {
      return ResponseEntity.ok().body(masks);
    }
    return ResponseEntity.badRequest().body(null);
  }

  // The total amount of masks and dollar value of transactions that happened within a date range
  // ex : {url}/api/masks/transaction?start=2021-01-01&end=2021-01-07
  @GetMapping("/transaction")
  public ResponseEntity<Map<String, Integer>> transaction(@RequestParam(required = true) String start,
      @RequestParam(required = false) String end) {
    Map<String, Integer> result = new HashMap<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    try {
      sdf.parse(start);
      sdf.parse(end);
    } catch (ParseException e) {
      //e.printStackTrace();
      result.put("Dates not valid", null);
      return ResponseEntity.badRequest().body(result);
    }
    result = maskDAO.transaction(start, end);
    if (result.size() > 0) {
      return ResponseEntity.ok().body(result);
    }
    result.put("No transaction between these dates", null);
    return ResponseEntity.badRequest().body(result);
  }

  // Search for masks by name, ranked by relevance to search term
  // ex : {url}/api/masks/search?key=green
  @GetMapping("/search")
  public ResponseEntity<ArrayList<String>> relevance(@RequestParam(required = true) String key) {
    ArrayList<String> masks = maskDAO.relevance(key);
    if(masks.size()>0) {
      return ResponseEntity.ok().body(masks);
    }
    masks.add("No masks found");
    return ResponseEntity.badRequest().body(masks);
  }
}
