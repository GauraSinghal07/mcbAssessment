package com.assignment.mcb.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import com.assignment.mcb.controller.FileController;
import com.assignment.mcb.enums.DocumentType;
import com.assignment.mcb.model.FileDetails;
import com.assignment.mcb.repository.FileRepository;
import com.assignment.mcb.service.FileStorageService;
@SpringBootTest
@AutoConfigureMockMvc

public class FileStorageServiceImplTest {

	@Autowired
	private MockMvc mockMvc;

	@InjectMocks
	FileStorageServiceImpl storageService;

	@Mock
	FileController fileController;

	@Mock
	FileRepository fileRepository;
	
	@Value("${file.upload-dir}")
	private String filePath;

	@BeforeEach
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void storeFileTest() {
		FileDetails fileDetails = new FileDetails();
		fileDetails.setCustomerId(67687L);
		fileDetails.setData("This is the file content".getBytes());
		fileDetails.setFileName("sampleFile.txt");
		fileDetails.setStatus("C");
		MockMultipartFile file = new MockMultipartFile("file", "sampleFile.txt", "text/plain",
				"This is the file content".getBytes());
		try {
			Mockito.when(fileRepository.save(Mockito.any(FileDetails.class))).thenReturn(fileDetails);
			FileDetails fileDetail = storageService.store(file, DocumentType.PERSONAL, 11L);
			assertThat(fileDetail).isNotNull();
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	public void saveFileInFileSystem() 
	{
		storageService.setFilePath(filePath);
		List<FileDetails> list = new ArrayList<>();
		FileDetails fileDetails = new FileDetails();
		fileDetails.setCustomerId(67687L);
		fileDetails.setData("This is the file content".getBytes());
		fileDetails.setFileName("sampleFile.txt");
		fileDetails.setStatus("C");
		fileDetails.setTimestamp(new Timestamp(System.currentTimeMillis()));
		list.add(fileDetails);
		String fileDir = System.getProperty("user.home") + java.io.File.separator + "Uploads";
		storageService.saveFileInFileSystem(list);
		File file = new File(fileDir);
		Assertions.assertNotNull(file.exists());
		
		
	}
}
