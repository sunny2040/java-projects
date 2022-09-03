package com.surge.vms.business;

import java.util.List;
import java.util.Optional;
import com.surge.vms.model.Vendor;

public interface VendorService {
	
	
	public List<Vendor> getAllVendors();
	
	public Optional<Vendor> getVendorByID(Long vendorId);
	
	public Vendor addVendor(Vendor vendor);
	
	public Optional<Vendor> updateVendor(Vendor updVendor,Long vendorId);
	
	public void deleteVendorById(Long vendorId);
	
	public List<Vendor> getVendorByStatus(String vendorStatus);
	
	public void updateVendorPID(Long vendorId, String vendorPID, String bussinessKey, String vendorUpdStatus);
	
	public void updateVendorStatus(Long vendorId, String vendorPID, String vendorUpdStatus);
	
	public void updateVendorApproverQuery(String vendorPID, String vendorUpdStatus, String approverQuery);
	
	public void updateVendorReply(Long vendorId, String vendorNewStatus, String vendorReply);
	
	public void updateVendorStatusByPID(String vendorPID, String vendorNewStatus);
	
	
	
	
}
