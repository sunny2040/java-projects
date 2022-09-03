package com.surge.vms.controller;

import com.surge.vms.business.DocumentService;
import com.surge.vms.business.VendorService;
import com.surge.vms.model.Document;
import com.surge.vms.model.Vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@RestController
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")

//https://mkyong.com/spring-boot/spring-boot-file-upload-example-ajax-and-rest/
public class DMSController {

	@Value("${dmsPath}")
	private String dmsBasePath;

	@Autowired
	private DocumentService documentService;

	@PostMapping("/upload")
	// public ResponseEntity uploadToLocalFileSystem(@RequestParam("file")
	// MultipartFile file) {
	public ResponseEntity uploadToLocalFileSystem(@RequestPart("vendor") Vendor vendor,
			@RequestParam("files") MultipartFile[] uploadfiles) {

		System.out.println("done");

		try {

			List<MultipartFile> fileList = Arrays.asList(uploadfiles);
			saveUploadedFiles(fileList, dmsBasePath);

		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

//		 
//		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//		Path path = Paths.get("D:\\VMS\\DMS\\" + fileName);
//		System.out.println("dmsBasePath...:"+dmsBasePath);
//		
//		
//		Document doc = new Document();
//		doc.setDocName(fileName);
//		doc.setDocPath(dmsBasePath);
//		doc.setDocOwnerId(123456L);
//		
//		
//		
//		try {
//			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//			Document savedDoc = documentService.addDocument(doc);
//			
//			System.out.println("File saved Path:"+savedDoc.getDocPath());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/download/")
				// .path(fileName)
				.path("").toUriString();
		return ResponseEntity.ok(HttpStatus.OK);
	}

	// @PostMapping("/uploadMulti")

	@RequestMapping(value = "/uploadMulti", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public ResponseEntity uploadToLocalFileSystemOL(@RequestPart("action") Vendor vendor,
			@RequestPart("resources") MultipartFile[] uploadfiles) {
		// public ResponseEntity uploadToLocalFileSystem(@RequestParam("file")
		// MultipartFile file) {
		// public ResponseEntity uploadToLocalFileSystemOL(@RequestParam("file")
		// MultipartFile[] uploadfiles) {

		System.out.println("done");

		return ResponseEntity.ok(HttpStatus.OK);
	}

	@RequestMapping(value = "/filelist", method = RequestMethod.GET, produces = { "application/json" })

	public List<String> listFiles() {

		List<String> listFiles = new ArrayList<>();
		File fileFolder = new File(dmsBasePath);
		File[] list = fileFolder.listFiles();

		for (File f : list) {
			if (!f.isDirectory()) {
				listFiles.add(f.getName());
			}
		}
		return listFiles;
	}

	@RequestMapping(value = "/doclist/{ownerId}", method = RequestMethod.GET, produces = { "application/json" })

	public List<Document> getDocsByOwnerId(@PathVariable String ownerId) {

		List<Document> docList = documentService.getDocumentByOwnerId(ownerId);
		return docList;
	}

	@PostMapping("/uploadtbd")
	public ResponseEntity uploadFileDemo(
			@RequestParam("murali") MultipartFile[] uploadfiles) {

		System.out.println("done");

			List<MultipartFile> fileList = Arrays.asList(uploadfiles);
//
//		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/download/")
//				// .path(fileName)
//				.path("").toUriString();
		return ResponseEntity.ok(HttpStatus.OK);
	}


	private void saveUploadedFiles(List<MultipartFile> files, String dmsBasePath) throws IOException {

		for (MultipartFile file : files) {

			if (file.isEmpty()) {
				continue; // next pls
			}

			byte[] bytes = file.getBytes();

			byte[] encodedBytes = Base64.getEncoder().encode(bytes);
			String encodedString = new String(encodedBytes);
			
			
			byte[] decodedBytes = Base64.getDecoder().decode(encodedString.getBytes());

			System.out.println(encodedBytes.toString());
			System.out.println("***************************");
			System.out.println(encodedString);
			System.out.println("***************************");
			System.out.println(decodedBytes.toString());
			Path path = Paths.get(dmsBasePath + file.getOriginalFilename());
			// Files.write(path, bytes);
			Files.write(path, decodedBytes);

		}

	}
}
