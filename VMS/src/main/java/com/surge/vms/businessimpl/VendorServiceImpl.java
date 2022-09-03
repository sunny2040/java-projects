package com.surge.vms.businessimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.surge.vms.business.VendorService;
import com.surge.vms.exception.BookNotFoundException;
import com.surge.vms.exception.BookUnSupportedFieldPatchException;
import com.surge.vms.model.Vendor;
import com.surge.vms.repositories.VendorRepository;
import java.util.Optional;

@Service
public class VendorServiceImpl implements VendorService {

	@Autowired
	private VendorRepository vendorRepository;

	@Override
	public List<Vendor> getAllVendors() {
		List<Vendor> list = new ArrayList<>();
		vendorRepository.findAll().forEach(e -> list.add(e));
		return list;
	}

	@Override
	public Optional<Vendor> getVendorByID(Long vendorId) {
		Optional<Vendor> vendor = vendorRepository.findById(vendorId);

		return vendor;
	}

	@Override
	public Vendor addVendor(Vendor vendor) {

		Vendor retVendor = vendorRepository.save(vendor);
		return retVendor;

	}
	
	
	@Override
	public Optional<Vendor> updateVendor(Vendor updVendor,Long vendorId) {
		
		Optional<Vendor> vendor = vendorRepository.findById(vendorId).map(x -> {
           x.setVendorReply(updVendor.getVendorReply());
           x.setEmail(updVendor.getEmail());
          // x.setServiceArea(updVendor.getServiceArea());
           x.setVendorStatus(updVendor.getVendorStatus());
           
            return vendorRepository.save(x);
        });
		
		return vendor;
	}

	@Override
	public void deleteVendorById(Long vendorId) {

		vendorRepository.deleteById(vendorId);

	}
	
	@Override
	public List<Vendor> getVendorByStatus(String vendorStatus) {
		List<Vendor> list = new ArrayList<>();
		list = vendorRepository.findByVendorStatusEquals(vendorStatus);
		return list;
	}
	

	@Override
	public void updateVendorPID(Long vendorId, String vendorPID, String businessKey, String vendorNewStatus) {
		
		Optional<Vendor> vendor = vendorRepository.findById(vendorId).map(x -> {
            x.setProcessInstanceId(vendorPID);
            x.setBussinessKey(businessKey);
            x.setVendorStatus(vendorNewStatus);
            return vendorRepository.save(x);
        });
	}
		
	@Override
	public void updateVendorApproverQuery(String vendorPID, String vendorNewStatus, String approverQuery) {
		
		Optional<Vendor> vendor = vendorRepository.findByProcessInstanceIdEquals(vendorPID).map(x -> {
            x.setApproverQuery(approverQuery);
            x.setVendorStatus(vendorNewStatus);
            return vendorRepository.save(x);
        });
				
	} 
		
	@Override
	public void updateVendorReply(Long vendorId, String vendorNewStatus, String vendorReply){
		
		Optional<Vendor> vendor = vendorRepository.findById(vendorId).map(x -> {
	        x.setVendorReply(vendorReply);
	        x.setVendorStatus(vendorNewStatus);
	        return vendorRepository.save(x);
	    });	
		
	}
	
	@Override
	public void updateVendorStatus(Long vendorId, String vendorPID, String vendorNewStatus){
		
		Optional<Vendor> vendor = vendorRepository.findByIdAndProcessInstanceId(vendorId, vendorPID).map(x -> {
	        x.setVendorStatus(vendorNewStatus);
	        return vendorRepository.save(x);
	    });	
		
	}
	
	@Override
	public void updateVendorStatusByPID(String vendorPID, String vendorNewStatus){
		
		Optional<Vendor> vendor = vendorRepository.findByProcessInstanceId(vendorPID).map(x -> {
	        x.setVendorStatus(vendorNewStatus);
	        return vendorRepository.save(x);
	    });	
		
	}
	
	
	
	


}
