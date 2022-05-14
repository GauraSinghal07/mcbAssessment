package com.assignment.mcb.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.assignment.mcb.dto.CustomerUser;
import com.assignment.mcb.dto.ResponseMessage;
import com.assignment.mcb.dto.UploadFileResponse;
import com.assignment.mcb.enums.DocumentType;
import com.assignment.mcb.exception.CustomerNotFoundException;
import com.assignment.mcb.exception.MyFileNotFoundException;
import com.assignment.mcb.model.FileDetails;
import com.assignment.mcb.service.impl.FileStorageServiceImpl;

@RestController
@CrossOrigin
public class FileController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private FileStorageServiceImpl storageService;

	/*
	 * uploads the file to DB
	 */
	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam MultipartFile file,
			@RequestParam("documentType") DocumentType documentType) {
		String message = "";
		try {

			CustomerUser user = (CustomerUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			storageService.validateFields(file, user);
			storageService.store(file, documentType, user.getCustomerId());
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			logger.info("Uploaded the file successfully: " + file.getOriginalFilename());
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (IOException e) {
			logger.error("Could not upload the file: " + file.getOriginalFilename() + "! and stack trace is "
					+ e.getStackTrace());
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		} catch (CustomerNotFoundException | MyFileNotFoundException e) {
			message = "Please attach the file to upload";
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));

		}

	}

	@GetMapping("/files")
	public ResponseEntity<List<UploadFileResponse>> getListFiles() {
		List<UploadFileResponse> files = storageService.getAllFiles().map(dbFile -> {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/")
					.path(dbFile.getFileId()).toUriString();
			return new UploadFileResponse(dbFile.getFileName(), fileDownloadUri, dbFile.getData().length);
		}).collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(files);
	}

	@GetMapping("/files/{id}")
	public ResponseEntity<byte[]> getFile(@PathVariable String id) {
		FileDetails fileDB = storageService.getFile(id);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getFileName() + "\"")
				.body(fileDB.getData());

	}
}
