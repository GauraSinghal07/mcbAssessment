package com.assignment.mcb.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.assignment.mcb.dto.CustomerUser;
import com.assignment.mcb.enums.DocumentType;
import com.assignment.mcb.enums.Status;
import com.assignment.mcb.exception.CustomerNotFoundException;
import com.assignment.mcb.exception.MyFileNotFoundException;
import com.assignment.mcb.model.FileDetails;
import com.assignment.mcb.property.Constants;
import com.assignment.mcb.repository.FileRepository;
import com.assignment.mcb.service.FileStorageService;

import mcb.assignmentmcb.utils.CommonUtils;

@Service
public class FileStorageServiceImpl implements FileStorageService {

	private static final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);

	@Value("${file.upload-dir}")
	private String filePath;

	@Autowired
	private FileRepository fileRepository;

	@Override
	public FileDetails getFile(String id) {
		return fileRepository.findById(id).get();
	}

	@Override
	public Stream<FileDetails> getAllFiles() {
		return fileRepository.findAll().stream();
	}

	@Override
	public List<FileDetails> getAllFilesByStatus(String status) {
		return fileRepository.findAllByStatus(Status.COMPLETED.name());
	}

	@Override
	public FileDetails store(MultipartFile file, DocumentType documentType, Long customerId) throws IOException {
		Long datetime = System.currentTimeMillis();
		Timestamp timestamp = new Timestamp(datetime);
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String uniqueFleName = CommonUtils.createUniqueFileName(datetime, fileName);
		FileDetails FileDB = new FileDetails(uniqueFleName, customerId, file.getBytes(), documentType.toString(),
				file.getSize(), Status.COMPLETED.name(), timestamp);
		return fileRepository.save(FileDB);
	}

	public void validateFields(MultipartFile file, CustomerUser customer) {
		if (file.getOriginalFilename().isEmpty()) {
			throw new MyFileNotFoundException("Please send the file");
		}
		if(customer == null) {
			throw new CustomerNotFoundException("Customer not found");
		}
	}

	@Override
	public void saveFileInFileSystem(List<FileDetails> findAllByStatus) {
		String fileDir = System.getProperty("user.home") + java.io.File.separator + filePath;

		for (FileDetails filedata : findAllByStatus) {
			Path newFilePath = Paths.get(fileDir + java.io.File.separator + filedata.getFileName());

			Path newIndexFilePath = Paths.get(fileDir + java.io.File.separator + Constants.FILENAME_STARTWITHDS
					+ CommonUtils.convertTimeStampToString(filedata.getTimestamp()) + ".index");
			Path createFile;
			try {
				createFile = Files.createFile(newFilePath);
				createFile = Files.createFile(newIndexFilePath);
				String finalData = createFileContent(filedata);
				Files.write(newFilePath, filedata.getData());
				Files.write(newIndexFilePath, finalData.getBytes());

			} catch (IOException e) {
				logger.error("exception occur while writing the file "+filedata.getFileName()+": " + e.getMessage());
			}
		}

	}

	public String createFileContent(FileDetails fileData) {
		String comment = Constants.COMMENT;
		String groupFieldName = Constants.GROUP_FIELD_NAME;
		String groupFieldValue = Constants.GROUP_FIELD_VALUE + CommonUtils.convertTimeStampToFormat(fileData.getTimestamp());
		String groupFieldNameForCus = Constants.GROUP_FIELD_NAME_FOR_CUSTOMER;
		String groupFieldValueForCus = Constants.GROUP_FIELD_VALUE_FOR_CUSTOMER + fileData.getCustomerId();
		String groupFieldNameForDt = Constants.GROUP_FIELD_NAME_FOR_DT;
		String groupFieldValueForDt = Constants.GROUP_FIELD_VALUE + fileData.getDocumentType();
		String groupFieldNameForFn = Constants.GROUP_FILE_NAME + Constants.FILENAME_STARTWITHDS
				+ CommonUtils.convertTimeStampToString(fileData.getTimestamp());

		String finalData = String.join(Constants.NEW_LINE, comment, groupFieldName, groupFieldValue, groupFieldNameForCus,
				groupFieldValueForCus, groupFieldNameForDt, groupFieldValueForDt, groupFieldNameForFn);

		return finalData;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
