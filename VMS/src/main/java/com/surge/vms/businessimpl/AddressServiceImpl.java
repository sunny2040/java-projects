package com.surge.vms.businessimpl;




import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.surge.vms.business.AddressService;
import com.surge.vms.model.City;
import com.surge.vms.model.Country;
import com.surge.vms.model.State;
import com.surge.vms.model.ZipCode;
import com.surge.vms.repositories.CityRepository;
import com.surge.vms.repositories.CountryRepository;
import com.surge.vms.repositories.StateRepository;
import com.surge.vms.repositories.ZipCodeRepository;

@Service
public class AddressServiceImpl implements AddressService{
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private StateRepository stateRepository;
	
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private ZipCodeRepository zipcodeRepository;

	@Override
	public Iterable<Country> getCountryList(){
		
		return countryRepository.findAll();
		
	}
	
	@Override
	public Optional<Country> getCountryByCode(String countryCode){
		
		return countryRepository.findByCountryCode(countryCode);
		
	}
	
	@Override
	public List<ZipCode> getZipCodeListByCityCode(String cityCode){
		
		return zipcodeRepository.findByCityCodeEquals(cityCode);
		
	}
	 
	@Override
	public List<City> getCityByState(String stateCode){
		
		return cityRepository.findByStateCodeEquals(stateCode);
		
	}
	
	@Override
	public List<State> getStateByCountry(String countryCode){
		
		return stateRepository.findByCountryCodeEquals(countryCode);
		
	}

}
