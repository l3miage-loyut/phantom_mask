package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

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
  @GetMapping("/sold_by")
  public ArrayList<Mask> soldBy(@RequestParam(required = true) String pharmacy, @RequestParam(required = false) String sorted_by) {
    ArrayList<Mask> masks = maskDAO.soldBy(pharmacy, sorted_by);
    return masks;
  }
}
