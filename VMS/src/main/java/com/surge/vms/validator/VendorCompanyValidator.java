package com.surge.vms.validator;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.surge.vms.business.VendorCompanyService;
import com.surge.vms.exception.BadDataException;
import com.surge.vms.exception.ResourceAlreadyExistException;
import com.surge.vms.model.VendorCompany;

@Component
public class VendorCompanyValidator {

	@Autowired
	private VendorCompanyService vendorCompanyService;

	public void validateVendorCompany(VendorCompany newVendorCompany) throws ResourceAlreadyExistException {

		String vendorEmail = newVendorCompany.getVendor().stream().findFirst().get().getEmail();

		String compTaxId = newVendorCompany.getTaxIdNumber();

		StringBuffer exceptionMsg = new StringBuffer();

		VendorCompany fetchedVendorComp = vendorCompanyService.getVendorCompanyByVendorRepEmail(vendorEmail);

		if (null != fetchedVendorComp) {

			exceptionMsg.append(" Email Id :" + vendorEmail + " Already Exist \n");
			fetchedVendorComp = null;
		}

		fetchedVendorComp = vendorCompanyService.getVendorCompanyByTaxIdNumber(compTaxId);
		if (null != fetchedVendorComp) {

			exceptionMsg.append(" Tax Id  :" + compTaxId + " Already Exist \n");
			fetchedVendorComp = null;
		}
		if (exceptionMsg.length() > 0) {
			throw new ResourceAlreadyExistException(exceptionMsg.toString());
		}

	}

	public void validateVendorCompanyBasic(VendorCompany updVendorComapny) throws BadDataException {

		Long vendorCompId = updVendorComapny.getCompanyId();
		Long vendorId = updVendorComapny.getVendor().stream().findFirst().get().getId();

		StringBuffer exceptionMsg = new StringBuffer();

		if (null == vendorCompId || vendorCompId <= 0) {

			exceptionMsg.append(" VendorCompany Id cannot be NULL \n");

		}
		if (null == vendorId) {

			exceptionMsg.append(" VendorRep Id cannot be NULL \n");

		}

		if (exceptionMsg.length() > 0) {
			throw new BadDataException(exceptionMsg.toString());
		}

	}

	public void validateVendorCompanyUploadFiles(Map<String, String> fileNameDomainTypeMap, MultipartFile[] uploadfiles)
			throws BadDataException {

		StringBuffer exceptionMsg = new StringBuffer();

		if (null != fileNameDomainTypeMap) {

			if (uploadfiles == null || uploadfiles.length != fileNameDomainTypeMap.size()) {
				exceptionMsg.append(" File Name and File Type are mismatching...Please check... \n");
			}

		}

		if (exceptionMsg.length() > 0) {
			throw new BadDataException(exceptionMsg.toString());
		}

	}

}
