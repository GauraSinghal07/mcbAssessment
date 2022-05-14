
package com.assignment.mcb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.assignment.mcb.dto.JwtRequest;
import com.assignment.mcb.enums.DocumentType;
import com.assignment.mcb.model.FileDetails;
import com.assignment.mcb.service.FileStorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {

	private String accessToken = "Bearer ";

	@Autowired
	private MockMvc mockMvc;

	@Mock
	FileStorageService storageService;

	@Mock
	FileController fileController;

	@BeforeEach
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		accessToken = accessToken + getToken();
	}

	public String getToken() throws Exception {
		String uri = "/authenticate";
		JwtRequest request = new JwtRequest();
		request.setUsername("gaura_1");
		request.setPassword("gaura123");
		String inputJson = mapToJson(request);
		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		JSONObject jsonObj = new JSONObject(content);

		return jsonObj.getString("token");
	}

	private String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		return objectMapper.writeValueAsString(obj);
	}

	@Test
	public void uploadFileTest() {
		MockMultipartFile file = new MockMultipartFile("file", "sampleFile.txt", "text/plain",
				"This is the file content".getBytes());
		FileDetails fileDetails = new FileDetails();
		fileDetails.setCustomerId(67687L);
		fileDetails.setData("This is the file content".getBytes());
		fileDetails.setFileName("sampleFile.txt");
		fileDetails.setStatus("C");
		try {
			RequestBuilder requestBuilder = MockMvcRequestBuilders.multipart("/upload").file(file)
					.param("documentType", DocumentType.PERSONAL.name()).header("Authorization", accessToken);

			MvcResult result = mockMvc.perform(requestBuilder).andReturn();

			MockHttpServletResponse response = result.getResponse();

			assertEquals(HttpStatus.OK.value(), response.getStatus());

		} catch (Exception e) {
			Assertions.fail();
		}

	}

	@Test
	public void getListFilesTest() {
		String uri = "/files";
		MvcResult mvcResult;
		try {
			mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)
					.header("Authorization", accessToken)).andReturn();
			int status = mvcResult.getResponse().getStatus();
			assertEquals(200, status);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
