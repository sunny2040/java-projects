package com.example.demo;


import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ApiController {

	
	@Autowired
	private RestTemplate restTemplate;
	
	
	private static String url="http://localhost:8082/getAllorders";
	
	
	@GetMapping("/getall")
	public List<Object> getList(){
		Object[] details=restTemplate.getForObject(url, Object[].class);
		return Arrays.asList(details);	
	} 	
	

    @GetMapping("/{id}")
	public String getbyid(@PathVariable long id){
    	String url="http://localhost:8082/findorder/{id}";
		String details=restTemplate.getForObject(url, String.class, id);
		return  details;		
	} 	
	
		
	 @DeleteMapping("/delete/{id}")
	    public void cancelRegistration(@PathVariable long id) {
		 	String url="http://localhost:8082/deleteorder/{id}";
		 	restTemplate.delete(url,id);
	     
	    }
	 
	 
	 @PostMapping("/createorders")
	 	public String register(@RequestBody String postt  ) {
		 String url="http://localhost:8082/createorders";
		 String details =  restTemplate.postForObject(url, postt,String.class);
		return details;
	 }
	
//
//	 @PutMapping("/createorders")
//	 	public void update(@RequestBody String postt  ) {
//		 String url="http://localhost:8082/update";
//		   restTemplate.put(url, postt);
//	
	 
}