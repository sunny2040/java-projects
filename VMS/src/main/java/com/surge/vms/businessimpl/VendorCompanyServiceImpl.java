package com.surge.vms.businessimpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.surge.vms.business.VendorCompanyService;
import com.surge.vms.exception.ResourceNotFoundException;
import com.surge.vms.model.Address;
import com.surge.vms.model.BankAccount;
import com.surge.vms.model.CompanyCompliance;
import com.surge.vms.model.CompanyLegal;
import com.surge.vms.model.Vendor;
import com.surge.vms.model.VendorCompany;
import com.surge.vms.repositories.VendorCompanyRepository;
import com.surge.vms.repositories.VendorRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class VendorCompanyServiceImpl implements VendorCompanyService {

	@Autowired
	private VendorCompanyRepository vendorCompanyRepository;

	@Autowired
	private VendorRepository vendorRepository;

	@Override
	public List<VendorCompany> getAllVendorCompany() {
		List<VendorCompany> list = new ArrayList<>();
		vendorCompanyRepository.findAll().forEach(e -> list.add(e));
		return list;
	}

	@Override
	public List<VendorCompany> getAllVendorCompanyByStatus(String status) {

		List<VendorCompany> vcList = getAllVendorCompany();

		List<VendorCompany> filteredList = new ArrayList<VendorCompany>();

		for (VendorCompany vc : vcList) {

			Vendor vendor = vc.getVendor().stream().findFirst().get();
			if (null != vendor && null != vendor.getVendorStatus()
					&& vendor.getVendorStatus().equalsIgnoreCase(status)) {
				filteredList.add(vc);
			}
		}

		return filteredList;
	}

	@Override
	public Optional<VendorCompany> getVendorCompanyByID(Long vendorCompId) {

		Optional<VendorCompany> vendorCompany = vendorCompanyRepository.findById(vendorCompId);

		return vendorCompany;
	}

	@Override
	public VendorCompany getVendorCompanyByVendorRepEmail(String email) {

		VendorCompany vcByEmail = vendorCompanyRepository.getCompanyByVendorRepEmail(email);

		return vcByEmail;
	}

	@Override
	public VendorCompany getVendorCompanyByTaxIdNumber(String taxId) {

		VendorCompany vcByTaxId = vendorCompanyRepository.getCompanyByTaxIdNumber(taxId);

		return vcByTaxId;
	}

	@Override
	public VendorCompany addVendorCompany(VendorCompany newVendorCompany) {

		VendorCompany retVendorCompany = vendorCompanyRepository.save(newVendorCompany);
		return retVendorCompany;

	}

	@Override
	public Optional<VendorCompany> updateVendorCompanyBasic(VendorCompany updCompanyVendor, Long vendorCompId) {

		Optional<VendorCompany> vendorCompany = vendorCompanyRepository.findById(vendorCompId).map(x -> {

			x.setCompanyName(updCompanyVendor.getCompanyName());

			x.setCompanyWebsite(updCompanyVendor.getCompanyWebsite());

			x.setCompanyTurnOver(updCompanyVendor.getCompanyTurnOver());

			x.setCompanyPhone(updCompanyVendor.getCompanyPhone());

			x.setCompanyFax(updCompanyVendor.getCompanyFax());

			x.setCategory(updCompanyVendor.getCategory());

			x.setTaxIdNumber(updCompanyVendor.getTaxIdNumber());

			x.setVendor(updCompanyVendor.getVendor());

			Set<Address> addrSet = x.getAddress();

			List<Address> addrList = new ArrayList<Address>(addrSet);
			for (int i = 0; i < addrList.size(); i++) {

				Address itemAddr = addrList.get(i);

				if (itemAddr.getAddrType().equalsIgnoreCase("corpaddr")) {

					Address updCorpAddress = updCompanyVendor.getAddress().stream().findFirst().get();
					updCorpAddress.setAddressId(itemAddr.getAddressId());
					addrList.set(i, updCorpAddress);
				}

			}

			addrSet = new HashSet<Address>(addrList);

			x.setAddress(addrSet);

			return vendorCompanyRepository.save(x);
		});

		return vendorCompany;
	}

	@Override
	public Optional<VendorCompany> updateVendorCompanyBankAccount(BankAccount feedBankAccount, Address remitAddress,
			Long vendorCompId) {

		Optional<VendorCompany> vendorCompanyOpt = vendorCompanyRepository.findById(vendorCompId);

		if (vendorCompanyOpt.isPresent()) {

			VendorCompany vendrCompany = vendorCompanyOpt.get();

			Long bankAccountId = vendrCompany.getBankAccount().stream().findFirst().get().getAccountId();

			feedBankAccount.setAccountId(bankAccountId);

			Set<BankAccount> bankAccountSet = new HashSet<BankAccount>();
			bankAccountSet.add(feedBankAccount);
			vendrCompany.setBankAccount(bankAccountSet);

			Set<Address> addrSet = vendrCompany.getAddress();

			List<Address> addrList = new ArrayList<Address>(addrSet);
			for (int i = 0; i < addrList.size(); i++) {

				Address itemAddr = addrList.get(i);

				if (itemAddr.getAddrType().equalsIgnoreCase("remitaddr")) {
					remitAddress.setAddressId(itemAddr.getAddressId());
					addrList.set(i, remitAddress);
				}

			}

			addrSet = new HashSet<Address>(addrList);
			vendrCompany.setAddress(addrSet);
			vendrCompany = vendorCompanyRepository.save(vendrCompany);

		}

		return vendorCompanyOpt;
	}

	@Override
	public Optional<VendorCompany> updateVendorCompanyCompliance(CompanyCompliance feedCompanyCompliance,
			Long vendorCompId) {

		Optional<VendorCompany> vendorCompanyOpt = vendorCompanyRepository.findById(vendorCompId);

		if (vendorCompanyOpt.isPresent()) {

			VendorCompany vendrCompany = vendorCompanyOpt.get();

			Long compComplianceId = vendrCompany.getCompanyCompliance().stream().findFirst().get().getComplianceId();

			feedCompanyCompliance.setComplianceId(compComplianceId);

			Set<CompanyCompliance> companyComplianceSet = new HashSet<CompanyCompliance>();
			companyComplianceSet.add(feedCompanyCompliance);
			vendrCompany.setCompanyCompliance(companyComplianceSet);
			vendrCompany = vendorCompanyRepository.save(vendrCompany);
		}

		return vendorCompanyOpt;
	}

	@Override
	public Optional<VendorCompany> updateVendorCompanyLegal(CompanyLegal feedCompanyLegal, Long vendorCompId) {

		Optional<VendorCompany> vendorCompanyOpt = vendorCompanyRepository.findById(vendorCompId);

		if (vendorCompanyOpt.isPresent()) {

			VendorCompany vendrCompany = vendorCompanyOpt.get();

			Long compLegalId = vendrCompany.getCompanyLegal().stream().findFirst().get().getLegalId();

			feedCompanyLegal.setLegalId(compLegalId);

			Set<CompanyLegal> companyLegalSet = new HashSet<CompanyLegal>();
			companyLegalSet.add(feedCompanyLegal);
			vendrCompany.setCompanyLegal(companyLegalSet);
			vendrCompany.getVendor().stream().findFirst().get().setVendorStatus("NEW");
			vendrCompany = vendorCompanyRepository.save(vendrCompany);

		}

		return vendorCompanyOpt;
	}

	@Override
	public Optional<VendorCompany> updateVendorCompanyAddlAddr(Map<String, Address> addlAddrMap, Long vendorCompId) {

		Optional<VendorCompany> vendorCompanyOpt = vendorCompanyRepository.findById(vendorCompId);

		if (vendorCompanyOpt.isPresent()) {

			VendorCompany locVendrCompany = vendorCompanyOpt.get();

			Set<Address> addrSet = locVendrCompany.getAddress();

			List<Address> addrList = new ArrayList<Address>(addrSet);
			for (int i = 0; i < addrList.size(); i++) {

				Address itemAddr = addrList.get(i);

				if (itemAddr.getAddrType().equalsIgnoreCase("poaddr")) {

					if (addlAddrMap.containsKey("poaddr")) {
						Address poAddress = (Address) addlAddrMap.get("poaddr");
						poAddress.setAddressId(itemAddr.getAddressId());

						addrList.set(i, poAddress);

					}

				} else if (itemAddr.getAddrType().equalsIgnoreCase("claimaddr")) {
					if (addlAddrMap.containsKey("claimaddr")) {
						Address claimAddress = (Address) addlAddrMap.get("claimaddr");
						claimAddress.setAddressId(itemAddr.getAddressId());

						addrList.set(i, claimAddress);

					}

				} else if (itemAddr.getAddrType().equalsIgnoreCase("returnaddr")) {

					if (addlAddrMap.containsKey("returnaddr")) {
						Address returnAddress = (Address) addlAddrMap.get("returnaddr");
						returnAddress.setAddressId(itemAddr.getAddressId());

						addrList.set(i, returnAddress);
					}

				}

			}

			addrSet = new HashSet<Address>(addrList);
			locVendrCompany.setAddress(addrSet);
			locVendrCompany = vendorCompanyRepository.save(locVendrCompany);
		}

		return vendorCompanyOpt;
	}

	@Override

	public Optional<VendorCompany> updateVendorCompanyVendor(Long vendorCompId, Vendor vendor) {

		Optional<VendorCompany> vendorCompany = vendorCompanyRepository.findById(vendorCompId);

		vendorCompany.get().getVendor().stream().findFirst().get().setVendorReply(vendor.getVendorReply());
		vendorCompany.get().getVendor().stream().findFirst().get().setVendorStatus(vendor.getVendorStatus());
		vendorCompanyRepository.save(vendorCompany.get());

		return vendorCompany;

	}

	public VendorCompany updateVendorRepPwdByEmail(String email, String pwd) {

		VendorCompany vendorCompIns = null;
		Optional<Vendor> vendorRep = vendorRepository.findByEmail(email);

		if (vendorRep.isPresent()) {
			vendorRep.get().setPwd(pwd);
			vendorRep.get().setVendorStatus("REGISTERED");
			vendorRepository.save(vendorRep.get());
			vendorCompIns = vendorCompanyRepository.getCompanyByVendorRepEmail(email);
		}

		return vendorCompIns;
	}

	@Override
	public void updateVendorReplyByVendorCompTaxId(String vendorCompTaxId, String vendorNewStatus, String vendorReply) {

		Optional<Vendor> vendor = vendorCompanyRepository.getCompanyByTaxIdNumber(vendorCompTaxId).getVendor().stream()
				.findFirst().map(x -> {
					x.setVendorReply(vendorReply);
					x.setVendorStatus(vendorNewStatus);
					return vendorRepository.save(x);
				});

	}

	@Override
	public void deleteVendorCompanyById(Long vendorCompId) {

		vendorCompanyRepository.deleteById(vendorCompId);

	}

}
