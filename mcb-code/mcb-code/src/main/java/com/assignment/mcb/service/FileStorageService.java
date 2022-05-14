package com.assignment.mcb.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import com.assignment.mcb.enums.DocumentType;
import com.assignment.mcb.model.FileDetails;

public interface FileStorageService {

	FileDetails getFile(String id);

	void saveFileInFileSystem(List<FileDetails> findAllByStatus);

	Stream<FileDetails> getAllFiles();

	FileDetails store(MultipartFile file, DocumentType documentType, Long customerId) throws IOException;

	List<FileDetails> getAllFilesByStatus(String status);

}
