package com.surge.vms.controller;

import com.surge.vms.business.VendorCompanyService;
import com.surge.vms.dto.UploadFileResponse;
import com.surge.vms.dto.VendorCompanyAddrDTO;
import com.surge.vms.dto.VendorCompanyBankAccountDTO;
import com.surge.vms.exception.BadDataException;
import com.surge.vms.exception.ResourceAlreadyExistException;
import com.surge.vms.exception.ResourceNotFoundException;
import com.surge.vms.model.Address;
import com.surge.vms.model.CompanyCompliance;
import com.surge.vms.model.CompanyLegal;
import com.surge.vms.model.Vendor;
import com.surge.vms.model.VendorCompany;
import com.surge.vms.repositories.VendorCompanyRepository;
import com.surge.vms.util.BuildVendorCompanyComponents;
import com.surge.vms.util.FileUtil;
import com.surge.vms.util.MailUtil;
import com.surge.vms.validator.VendorCompanyValidator;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.*;
import java.net.URLConnection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VendorCompanyController {

	@Autowired
	private VendorCompanyService vendorCompanyService;

	@Autowired
	private VendorCompanyValidator vendorCompanyValidator;

	@Autowired
	private FileUtil fileUtil;
	
	@Autowired
	private MailUtil mailUtil;

	// tbd
	@Autowired
	private VendorCompanyRepository vendorCompanyRepository;

	@Value("${dmsPath}")
	private String dmsBasePath;

	@Value("${activationKeyDelimiter}")
	private String activationKeyDelimiter;

	// Find
	@GetMapping("/vendorcompany")
	Iterable<VendorCompany> findAll() {
		return vendorCompanyService.getAllVendorCompany();
	}

	// Save
	@PostMapping("/vendorcompanyonly")
	VendorCompany newCompany(@Valid @RequestBody VendorCompany newCompany) {
		return vendorCompanyService.addVendorCompany(newCompany);
	}

	@PostMapping("/dummyvendor")
	String newCompany() {
		return "sucess";
	}

	@PostMapping("/vendorcompany")
	ResponseEntity<VendorCompany> newCompanyWithFiles(@RequestPart("vendor") VendorCompany newCompany,
			@RequestPart("resources") MultipartFile[] uploadfiles) {

		VendorCompany savedCompany = null;
		String compTaxId = newCompany.getTaxIdNumber();
		String filefolderName = dmsBasePath + compTaxId + "\\";
		newCompany.getVendor().stream().findFirst().get().setVendorStatus("NEW");
		try {

			List<MultipartFile> fileList = Arrays.asList(uploadfiles);
			List<UploadFileResponse> uploadFileListDetails = fileUtil.saveUploadedFiles(fileList, filefolderName,
					newCompany);
			savedCompany = vendorCompanyService.addVendorCompany(newCompany);
			savedCompany.setUploadFileListDetails(uploadFileListDetails);

		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<VendorCompany>(savedCompany, HttpStatus.OK);
	}

	@PostMapping("/vendorcompanyreg/basic")
	ResponseEntity<VendorCompany> newCompanyRegWithDocs(@RequestPart("action") VendorCompany newCompany,
			@RequestPart("resources") MultipartFile[] uploadfiles)
			throws ResourceAlreadyExistException, BadDataException {

		VendorCompany savedCompany = null;
		vendorCompanyValidator.validateVendorCompany(newCompany);
		if (null != uploadfiles && uploadfiles.length > 0) {
			vendorCompanyValidator.validateVendorCompanyUploadFiles(newCompany.getFileNameDomainTypeMap(), uploadfiles);
		}

		newCompany = BuildVendorCompanyComponents.getVendorCompanyBuildUp(newCompany);
		try {
			savedCompany = vendorCompanyService.addVendorCompany(newCompany);
			savedCompany = fileUtil.saveDocs(savedCompany, uploadfiles);
			savedCompany.setFileNameDomainTypeMap(newCompany.getFileNameDomainTypeMap());

		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<VendorCompany>(savedCompany, HttpStatus.OK);
	}

	@PutMapping("/vendorcompanyreg/basic")
	ResponseEntity<VendorCompany> updCompanyRegWithDocs(@RequestPart("action") VendorCompany updCompany,
			@RequestPart("resources") MultipartFile[] uploadfiles)
			throws ResourceAlreadyExistException, BadDataException {

		VendorCompany savedCompany = null;
		vendorCompanyValidator.validateVendorCompanyBasic(updCompany);
		if (null != uploadfiles && uploadfiles.length > 0) {
			vendorCompanyValidator.validateVendorCompanyUploadFiles(updCompany.getFileNameDomainTypeMap(), uploadfiles);
		}

		try {
			savedCompany = vendorCompanyService.updateVendorCompanyBasic(updCompany, updCompany.getCompanyId()).get();
			savedCompany = fileUtil.saveDocs(savedCompany, uploadfiles);
			savedCompany.setFileNameDomainTypeMap(updCompany.getFileNameDomainTypeMap());

		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<VendorCompany>(savedCompany, HttpStatus.OK);
	}

	// Save company bank acc

	@PostMapping("/vendorcompanyreg/bank/{vendorCompId}")
	public ResponseEntity<VendorCompany> updateVendorCompBankAccount(@PathVariable Long vendorCompId,
			@RequestPart("action") VendorCompanyBankAccountDTO vendorCompanyBankAccountDTO,
			@RequestPart("bankresources") MultipartFile[] uploadfiles) throws BadDataException {

		if (null != uploadfiles && uploadfiles.length > 0) {
			vendorCompanyValidator.validateVendorCompanyUploadFiles(
					vendorCompanyBankAccountDTO.getFileNameDomainTypeMap(), uploadfiles);
		}

		Optional<VendorCompany> optVendorCompany = vendorCompanyService.updateVendorCompanyBankAccount(
				vendorCompanyBankAccountDTO.getBankAccount(), vendorCompanyBankAccountDTO.getAddress(), vendorCompId);

		VendorCompany retrievedVendorCompany = optVendorCompany.get();
		
		try {
			retrievedVendorCompany.setFileNameDomainTypeMap(vendorCompanyBankAccountDTO.getFileNameDomainTypeMap());
			retrievedVendorCompany = fileUtil.saveDocs(retrievedVendorCompany, uploadfiles);

		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<VendorCompany>(retrievedVendorCompany, HttpStatus.OK);

	}

	// Save company compliance
	@RequestMapping(value = "/vendorcompanyreg/compliance/{vendorCompId}", method = RequestMethod.POST)
	public ResponseEntity<VendorCompany> updateVendorCompCompliance(@PathVariable Long vendorCompId,
			@RequestPart("action") CompanyCompliance companyCompliance,
			@RequestPart("complianceresources") MultipartFile[] uploadfiles) throws BadDataException {

		if (null != uploadfiles && uploadfiles.length > 0) {
			vendorCompanyValidator.validateVendorCompanyUploadFiles(companyCompliance.getFileNameDomainTypeMap(),
					uploadfiles);
		}

		Optional<VendorCompany> optVendorCompany = vendorCompanyService.updateVendorCompanyCompliance(companyCompliance,
				vendorCompId);

		VendorCompany retrievedVendorCompany = optVendorCompany.get();
		try {
			retrievedVendorCompany.setFileNameDomainTypeMap(companyCompliance.getFileNameDomainTypeMap());
			retrievedVendorCompany = fileUtil.saveDocs(retrievedVendorCompany, uploadfiles);

		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<VendorCompany>(retrievedVendorCompany, HttpStatus.OK);

	}

	// Save company compliance
	@RequestMapping(value = "/vendorcompanyreg/legal/{vendorCompId}", method = RequestMethod.POST)
	public ResponseEntity<VendorCompany> updateVendorCompLegal(@PathVariable Long vendorCompId,
			@RequestPart("action") CompanyLegal companyLegal,
			@RequestPart("legalresources") MultipartFile[] uploadfiles) throws BadDataException {

		if (null != uploadfiles && uploadfiles.length > 0) {
			vendorCompanyValidator.validateVendorCompanyUploadFiles(companyLegal.getFileNameDomainTypeMap(),uploadfiles);
		}

		Optional<VendorCompany> optVendorCompany = vendorCompanyService.updateVendorCompanyLegal(companyLegal,
				vendorCompId);

		VendorCompany retrievedVendorCompany = optVendorCompany.get();
	
		
		try {
			retrievedVendorCompany.setFileNameDomainTypeMap(companyLegal.getFileNameDomainTypeMap());
			retrievedVendorCompany = fileUtil.saveDocs(retrievedVendorCompany, uploadfiles);

		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<VendorCompany>(retrievedVendorCompany, HttpStatus.OK);

	}

	// Save company compliance
	@RequestMapping(value = "/vendorcompanyreg/addladdr/{vendorCompId}", method = RequestMethod.POST)
	public ResponseEntity<VendorCompany> updateVendorCompAddlAddr(@PathVariable Long vendorCompId,
			@RequestBody VendorCompanyAddrDTO vendorCompanyAddrDTO) {

		Map<String, Address> addladdrMap = vendorCompanyAddrDTO.getAddladdr();
		Optional<VendorCompany> optVendorCompany = Optional.empty();
		if (null != addladdrMap && addladdrMap.size() > 0) {

			optVendorCompany = vendorCompanyService.updateVendorCompanyAddlAddr(addladdrMap, vendorCompId);
			try {

				fileUtil.saveDocs(optVendorCompany.get(), null);

			} catch (IOException e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

		}

		return new ResponseEntity<VendorCompany>(optVendorCompany.get(), HttpStatus.OK);

	}

	// Find
	@GetMapping("/vendorcompany/{id}")
	ResponseEntity<VendorCompany> findVendorCompById(@PathVariable Long id) throws ResourceNotFoundException {

		VendorCompany fetchedVendorComp = null;

		boolean isVcPresent = vendorCompanyService.getVendorCompanyByID(id).isPresent();

		if (isVcPresent) {

			fetchedVendorComp = vendorCompanyService.getVendorCompanyByID(id).get();

		}

		if (null != fetchedVendorComp) {

			fetchedVendorComp = fileUtil.getDocListForVendorCompany(fetchedVendorComp);
			fetchedVendorComp.setFileNameDomainTypeMap(fileUtil.getFileNameDomainTypeMap(fetchedVendorComp));

			return new ResponseEntity<VendorCompany>(fetchedVendorComp, HttpStatus.OK);

		} else {

			throw new ResourceNotFoundException("VendorCompany not found for this id :: " + id);
		}

	}

	// Find
	@GetMapping("/vendorcompany/email/{email}")
	ResponseEntity<VendorCompany> findVendorCompByEmail(@PathVariable String email) throws ResourceNotFoundException {

		VendorCompany fetchedVendorComp = vendorCompanyService.getVendorCompanyByVendorRepEmail(email);

		if (null != fetchedVendorComp) {

			fetchedVendorComp = fileUtil.getDocListForVendorCompany(fetchedVendorComp);
			fetchedVendorComp.setFileNameDomainTypeMap(fileUtil.getFileNameDomainTypeMap(fetchedVendorComp));

			return new ResponseEntity<VendorCompany>(fetchedVendorComp, HttpStatus.OK);

		} else {

			throw new ResourceNotFoundException("VendorCompany not found for this email :: " + email);
		}

	}

	// validate login
	@GetMapping("/vendorcompany/validateUserIdPassword/{email}/{password}")
	ResponseEntity<String> findVendorCompByEmail(@PathVariable String email, @PathVariable String password)
			 {

		VendorCompany fetchedVendorComp = vendorCompanyService.getVendorCompanyByVendorRepEmail(email);

		if (null != fetchedVendorComp && null != fetchedVendorComp.getVendor().stream().findFirst().get()) {

			Vendor retVendor = fetchedVendorComp.getVendor().stream().findFirst().get();
			String retrievedPwd = retVendor.getPwd();
			if (retrievedPwd != null && retrievedPwd.equalsIgnoreCase(password)) {
				return new ResponseEntity<String>(fetchedVendorComp.getCompanyId().toString(), HttpStatus.OK);
			}else {
				return new ResponseEntity<String>("Password Mismatch...",HttpStatus.BAD_REQUEST);
			}
		} else {

			return new ResponseEntity<String>("Invalid Username...",HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/vendorcompany/validateApproval/{activationKey}")
	ResponseEntity<String> validateVendorCompanyApproval(@PathVariable String activationKey)
			throws ResourceNotFoundException {

		byte[] decodedBytes = Base64.getDecoder().decode(activationKey);
		String decodedActivationkey = new String(decodedBytes);
		System.out.println("decodedActivationkey: " + decodedActivationkey);
		String vendorRepEmail = "";
		if (null != decodedActivationkey && decodedActivationkey.contains(activationKeyDelimiter)) {
			String[] splitterArr = decodedActivationkey.split(activationKeyDelimiter);
			String item1 = splitterArr[0];
			String item2 = splitterArr[1];

			if (null != item2 && item2.length() > 0 && null != item1 && item1.length() > 0) {

				VendorCompany vendorCompany = vendorCompanyService.getVendorCompanyByVendorRepEmail(item2);
				if (null != vendorCompany && vendorCompany.getVendor().size() > 0) {

					Vendor vendorRep = vendorCompany.getVendor().stream().findFirst().get();
					if (item1.equalsIgnoreCase(vendorRep.getProcessInstanceId())
							&& item2.equalsIgnoreCase(vendorRep.getEmail())) {
						vendorRepEmail = vendorRep.getEmail();
					}
				}

			}
		}

		return new ResponseEntity<String>(vendorRepEmail, HttpStatus.OK);

	}

	// Save or update
	@RequestMapping(value = "/vendorcompany/{id}", method = RequestMethod.PUT)
	public ResponseEntity<VendorCompany> updateVendorComp(@PathVariable Long id,
			@RequestPart("action") VendorCompany updVendorComp, @RequestPart("resources") MultipartFile[] uploadfiles) {

		String compTaxId = updVendorComp.getTaxIdNumber();
		updVendorComp.getVendor().stream().findFirst().get().setVendorStatus("NEW");
		String filefolderName = dmsBasePath + compTaxId + "\\";
		List<UploadFileResponse> uploadFileListDetails = new ArrayList<UploadFileResponse>();

		try {

			List<MultipartFile> fileList = Arrays.asList(uploadfiles);
			uploadFileListDetails = fileUtil.saveUploadedFiles(fileList, filefolderName, updVendorComp);

		} catch (IOException e) {
			e.printStackTrace();
		}
		Optional<VendorCompany> retVendorComp = vendorCompanyService.updateVendorCompanyBasic(updVendorComp, id);

		List<UploadFileResponse> savedDocumentList = fileUtil.listFiles(filefolderName, compTaxId);

		retVendorComp.get().setUploadFileListDetails(uploadFileListDetails);
		retVendorComp.get().setVendorCompanyDocList(savedDocumentList);
		return new ResponseEntity<VendorCompany>(retVendorComp.get(), HttpStatus.OK);

	}

	@RequestMapping(value = "/updateVendorReply/{vendorcompid}", method = RequestMethod.POST)
	public ResponseEntity<VendorCompany> updateVendorComp(@RequestBody Vendor vendor, @PathVariable Long vendorcompid) {

		VendorCompany retVendorComp = vendorCompanyService.updateVendorCompanyVendor(vendorcompid, vendor).get();

		return new ResponseEntity<VendorCompany>(retVendorComp, HttpStatus.OK);

	}

	@RequestMapping(value = "/vendorcompany/updateVendorPwd/{email}/{pwd}", method = RequestMethod.POST)
	public ResponseEntity<VendorCompany> updateVendorRepPassword(@PathVariable String email, @PathVariable String pwd) {

		VendorCompany retVendorComp = null;
		if (null != email && email.length() > 0 && null != pwd && pwd.length() > 0) {

			retVendorComp = vendorCompanyService.updateVendorRepPwdByEmail(email, pwd);
		}

		return new ResponseEntity<VendorCompany>(retVendorComp, HttpStatus.OK);

	}

	@RequestMapping(value = "/vendorcompany/updateVendorReply/{taxId}/{reply}", method = RequestMethod.POST)
	public ResponseEntity<String> updateVendorReplyByVendorCompTaxId(@PathVariable String taxId,
			@PathVariable String reply) {

		if (null != taxId && taxId.length() > 0 && null != reply && reply.length() > 0) {

			vendorCompanyService.updateVendorReplyByVendorCompTaxId(taxId, "REPLIED", reply);
			return new ResponseEntity<String>("Success", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Error", HttpStatus.BAD_REQUEST);
		}

	}

	@DeleteMapping("/vendorcompany/{id}")
	void deleteVendorCompanyById(@PathVariable Long id) {
		vendorCompanyService.deleteVendorCompanyById(id);
	}

	// TBD method
	@DeleteMapping("/vendorcompany/all")
	void deleteVendorCompany() {
		vendorCompanyRepository.deleteAll();
	}

	@RequestMapping("/downloadFile/{compId}/{fileName:.+}")
	public void df(HttpServletRequest request, HttpServletResponse response, @PathVariable("compId") String compId,
			@PathVariable("fileName") String fileName) throws IOException {

		try {

			String filePath = dmsBasePath + compId + "\\" + fileName;
			System.out.println("File Full path name:" + filePath);
			File file = new File(filePath);

			InputStream inputStream = new FileInputStream(file);
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}

			response.setContentType(mimeType);

			/**
			 * Here we have mentioned it to show inline
			 */
			// response.setHeader("Content-Disposition", String.format("inline; filename=\""
			// + file.getName() + "\""));

			// Here we have mentioned it to show as attachment
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));

			response.setContentLength((int) file.length());

			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
			inputStream.close();

		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

	}

}