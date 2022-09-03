package com.surge.vms.controller;


import com.surge.vms.business.VendorService;
import com.surge.vms.model.Vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VendorController {

	@Autowired
	private VendorService vendorService;

	// Find
	@GetMapping("/vendor/{id}")
	Optional<Vendor> findOne(@PathVariable Long id) {
		return vendorService.getVendorByID(id);

	}

	@GetMapping(value = "/vendors")
	public List<Vendor> getAllVendors() {
		List<Vendor> vendorList = vendorService.getAllVendors();
		return vendorList;
	}

	@PostMapping("/vendor")
	public Vendor addVendor(@RequestBody Vendor newVendor) {
		System.out.println("addVendor called...");
		newVendor.setVendorStatus("ACTIVE");
		//newVendor.setCategory("SMALLSCALE");
		
		Vendor retVendor = vendorService.addVendor(newVendor);
		return retVendor;
	}

	 // Save or update
    @PutMapping("/vendor/{id}")
    public Optional<Vendor> updateVendor(@RequestBody Vendor updVendor, @PathVariable Long id) {

    	Optional<Vendor> retVendor = vendorService.updateVendor(updVendor, id);
		return retVendor;
    }	
    
    
    @PutMapping("/vendor/query/{pId}/{qry}")
    public void updateApproverQuery(@PathVariable String pId, @PathVariable String qry) {
    	vendorService.updateVendorApproverQuery(pId, "PENDING", qry);
    }
    
    @PutMapping("/vendor/status/{pId}/{status}")
    public void updateVendorStatusByPID(@PathVariable String pId, @PathVariable String status) {
    	vendorService.updateVendorStatusByPID(pId, status);
    }
	
	
	@DeleteMapping("/vendor/{id}")
	void deleteVendor(@PathVariable Long id) {
		vendorService.deleteVendorById(id);
	}
	


}