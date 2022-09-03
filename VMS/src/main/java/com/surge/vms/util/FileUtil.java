package com.surge.vms.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.surge.vms.business.DocumentService;
import com.surge.vms.dto.DocumentDTO;
import com.surge.vms.dto.UploadFileResponse;
import com.surge.vms.model.Document;
import com.surge.vms.model.VendorCompany;

@Component
public class FileUtil {

	@Value("${dmsPath}")
	private String dmsBasePath;
	@Autowired
	DocumentService documentService;

	public List<UploadFileResponse> listFiles(String fileDir, String compTaxId) {

		List<UploadFileResponse> fileList = new ArrayList<UploadFileResponse>();

		File fileFolder = new File(fileDir);
		File[] list = fileFolder.listFiles();

		if (null != list && list.length > 0) {

			for (File file : list) {
				if (!file.isDirectory()) {

					String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
							.path(compTaxId + "//").path(file.getName()).toUriString();

					UploadFileResponse uploadFileResponse = new UploadFileResponse(file.getName(), fileDownloadUri);
					fileList.add(uploadFileResponse);
				}
			}

		}

		return fileList;
	}

	public VendorCompany getDocListForVendorCompany(VendorCompany vendorCompany) {

		String compTaxId = vendorCompany.getTaxIdNumber();

		String filefolderName = dmsBasePath + compTaxId + "\\";

		List<UploadFileResponse> savedDocumentList = listFiles(filefolderName, compTaxId);

		vendorCompany.setVendorCompanyDocList(savedDocumentList);

		return vendorCompany;

	}

	public VendorCompany saveDocs(VendorCompany vendorCompany, MultipartFile[] uploadfiles) throws IOException {

		String compTaxId = vendorCompany.getTaxIdNumber();
		String filefolderName = dmsBasePath + compTaxId + "\\";
		try {

			if (null != uploadfiles && uploadfiles.length > 0) {

				List<MultipartFile> fileList = Arrays.asList(uploadfiles);
				List<UploadFileResponse> uploadFileListDetails = saveUploadedFiles(fileList, filefolderName,
						vendorCompany);
				vendorCompany.setUploadFileListDetails(uploadFileListDetails);

			}

			List<UploadFileResponse> savedDocumentList = listFiles(filefolderName, compTaxId);

			vendorCompany.setVendorCompanyDocList(savedDocumentList);

		} catch (IOException e) {
			throw e;
		}
		return vendorCompany;

	}

	public List<UploadFileResponse> saveUploadedFiles(List<MultipartFile> files, String dmsBasePath,
			VendorCompany vendorCompany) throws IOException {

		List<UploadFileResponse> uploadFileListDetails = new ArrayList<UploadFileResponse>();
		Map<String, String> fileDomainMap = vendorCompany.getFileNameDomainTypeMap();
		String fileOriginalName = "";
		String fileDomainType = "";
		String compTaxId = vendorCompany.getTaxIdNumber();
		Long docId = 0l;

		for (MultipartFile file : files) {

			if (file.isEmpty()) {
				continue;
			}

			Path dirPath = Paths.get(dmsBasePath);

			byte[] bytes = file.getBytes();

			if (!Files.exists(dirPath)) {

				Files.createDirectories(dirPath);
			}

			fileOriginalName = file.getOriginalFilename().trim();

			Path dirPathFile = Paths.get(dmsBasePath + fileOriginalName);

			if (null != fileDomainMap && fileDomainMap.size() > 0) {
				//fileDomainType = fileDomainMap.get(fileOriginalName);
				
				for (Map.Entry<String, String> entry : fileDomainMap.entrySet()) {
				    if(fileOriginalName.equalsIgnoreCase(entry.getValue())) {
				    	fileDomainType = entry.getKey();
				    }
				}
			}

			Document doc = new Document();
			doc.setDocName(fileOriginalName);
			doc.setDocOwnerId(compTaxId);
			doc.setDocPath(dirPathFile.toString());
			doc.setDocDomainType(fileDomainType);

			List<Document> retDocList = documentService.getDocumentByOwnerIdAndDocType(compTaxId, fileDomainType);
			if (null != retDocList && retDocList.size() > 0) {
				docId = retDocList.get(0).getDocId();
				doc.setDocId(docId);
			}
			documentService.addDocument(doc);

			Files.write(dirPathFile, bytes);

			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
					.path(compTaxId + "//").path(fileOriginalName).toUriString();
			System.out.println("fileDownloadUri:" + fileDownloadUri);
			UploadFileResponse uploadFileResponse = new UploadFileResponse(fileOriginalName, fileDownloadUri);

			uploadFileListDetails.add(uploadFileResponse);

		}

		return uploadFileListDetails;

	}

	public Map<String, String> getFileNameDomainTypeMap(VendorCompany vendorCompany) {

		String compTaxId = vendorCompany.getTaxIdNumber();
		List<Document> docList = documentService.getDocumentByOwnerId(compTaxId);

		Map<String, String> fileDomainMap = new HashMap<String, String>();
		if (null != docList && docList.size() > 0) {

			for (Document doc : docList) {
				String fileName = doc.getDocName();
				String fileDomainType = doc.getDocDomainType();
				fileDomainMap.put(fileDomainType, fileName);

			}
		}
		return fileDomainMap;

	}

	public DocumentDTO encodeFileFromPath(String filePath) throws IOException {

		DocumentDTO documentDTO = new DocumentDTO();

		Path path = Paths.get(filePath);

		byte[] fileContent = Files.readAllBytes(path);

		byte[] encodedBytes = Base64.getEncoder().encode(fileContent);

		String mimeType = Files.probeContentType(path);

		System.out.println(mimeType);
		System.out.println(fileContent);

		String encodedString = new String(encodedBytes);
		System.out.println("***************************");
		System.out.println(encodedString);

		documentDTO.setDocEncodedContent(encodedString);
		documentDTO.setDocName(path.getFileName().toString());
		documentDTO.setDocMimeType(mimeType);

		return documentDTO;

	}

}
