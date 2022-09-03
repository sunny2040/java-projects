package com.surge.vms.util;

import java.net.URI;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.surge.vms.model.Vendor;

public class RestClientUtil {

	public void addVendorDemo() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8081/vendors";
		Vendor objVendor = new Vendor();
		objVendor.setFirstName("Muralibabu");
		//objVendor.setLastName("Muthukrishnan");
		//objVendor.setEmail("muthumur@gmail.com");


		HttpEntity<Vendor> requestEntity = new HttpEntity<Vendor>(objVendor, headers);
		URI uri = restTemplate.postForLocation(url, requestEntity);
		//System.out.println(uri.());
	}

	
	 public void getAllVendorsDemo() {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
		        RestTemplate restTemplate = new RestTemplate();
		        String url = "http://localhost:8081/vendors";
		        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		        ResponseEntity<Vendor[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Vendor[].class);
		        Vendor[] vendors = responseEntity.getBody();
		        for(Vendor vendor : vendors) {
//		              System.out.println("Email:"+vendor.getEmail()+", FN:"+vendor.getFirstName()
//		                      +", Id: "+vendor.getId());
		        }
		    }
	
	public static void main(String args[]) {
    	RestClientUtil util = new RestClientUtil();
      
    	//util.addVendorDemo();  
    	util.getAllVendorsDemo();
    }   
}
