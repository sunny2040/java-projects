package com.example.vms.vmssurge.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.vms.vmssurge.model.PurchaseList;
import com.example.vms.vmssurge.repositories.PurchaseRepository;

@RestController

public class PurchaseController {


    @Autowired
    private PurchaseRepository repository;

    @PostMapping("/createorders")
    public PurchaseList register(@RequestBody PurchaseList order) {
    	  return  repository.save(order);
      
    }
    
    @GetMapping("/getAllorders")
    public List<PurchaseList> findAllUsers() {
        return repository.findAll();
    }

    @GetMapping("/findorder/{id}")
    public Optional<PurchaseList> findorder(@PathVariable long id) {
    	return repository.findById(id);
        
    }
    
    @DeleteMapping("/deleteorder/{id}")
    public List<PurchaseList> cancelRegistration(@PathVariable long id) {
        repository.deleteById(id);
        return repository.findAll();

        
    }
}

