package com.surge.vms.business;


import java.util.List;
import java.util.Optional;

import com.surge.vms.model.Country;
import com.surge.vms.model.State;
import com.surge.vms.model.City;
import com.surge.vms.model.ZipCode;


public interface AddressService {
	
	
	
	public List<ZipCode> getZipCodeListByCityCode(String cityCode);
	
	public List<City> getCityByState(String stateCode);
	
	public List<State> getStateByCountry(String countryCode);
	
	public Iterable<Country> getCountryList();
	
	public Optional<Country> getCountryByCode(String countryCode);
	
	

}
