package com.surge.vms.controller;

import com.surge.vms.business.AddressService;
import com.surge.vms.model.City;
import com.surge.vms.model.Country;
import com.surge.vms.model.State;
import com.surge.vms.model.ZipCode;
import com.surge.vms.util.MailUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import javax.mail.MessagingException;

@RestController
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AddressController {

    @Autowired
    private AddressService addressService;
    
    @Autowired

    // Find
    @GetMapping("/country")
    Iterable<Country> findAll() {
    	
    	Iterable<Country> countryList = addressService.getCountryList();
        return countryList;
    }
    

    
    @GetMapping("/countryonly/")
    Country findOneCountry() {
    	
    	Country country = new Country();
    	country.setCountryCode("FRN");
    	country.setCountryId(3l);
    	country.setCountryName("FRANCE");
        return country;
    }
    
    
    @GetMapping("/state/{countryCode}")
    List<State> findStateByCountry(@PathVariable String countryCode) {
    	
    	List<State> stateList = addressService.getStateByCountry(countryCode);
        return stateList;
    }
    
    @GetMapping("/city/{stateCode}")
    List<City> findCityByState(@PathVariable String stateCode) {
    	
    	List<City> cityList = addressService.getCityByState(stateCode);
        return cityList;
    }
    
 
    @GetMapping("/zip/{cityCode}")
    List<ZipCode> findZipBycity(@PathVariable String cityCode) {
    	
    	List<ZipCode> zipCodeList = addressService.getZipCodeListByCityCode(cityCode);
        return zipCodeList;
    }
    
   
  

    
}