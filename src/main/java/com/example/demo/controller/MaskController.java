package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.demo.model.Mask;
import com.example.demo.respository.MaskDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/masks")
public class MaskController {

  @Autowired private MaskDAO maskDAO;

  @GetMapping("/")
  public List<Mask> getMasks() {
    List<Mask> masks = (List<Mask>) maskDAO.getAll();
    return masks;
  }

  // List all masks that are sold by a given pharmacy, sorted by mask name or mask price
  // ex : {url}/api/masks/sold_by?pharmacy=Cash Saver Pharmacy&sorted_by=name
  @GetMapping("/sold_by")
  public ArrayList<Mask> soldBy(@RequestParam(required = true) String pharmacy, @RequestParam(required = false) String sorted_by) {
    ArrayList<Mask> masks = maskDAO.soldBy(pharmacy, sorted_by);
    return masks;
  }

  // The total amount of masks and dollar value of transactions that happened within a date range
  // ex : {url}/api/masks/transaction?start=2021-01-01&end=2021-01-07
  @GetMapping("/transaction")
  public Map<String, Integer> transaction(@RequestParam(required = true) String start,
      @RequestParam(required = false) String end) {
    Map<String, Integer> result = maskDAO.transaction(start, end);
    return result;
  }

  // Search for masks by name, ranked by relevance to search term
  // ex : {url}/api/masks/search?key=green
  @GetMapping("/search")
  public ArrayList<String> relevance(@RequestParam(required = true) String key) {
    ArrayList<String> masks = maskDAO.relevance(key);
    return masks;
  }
}
