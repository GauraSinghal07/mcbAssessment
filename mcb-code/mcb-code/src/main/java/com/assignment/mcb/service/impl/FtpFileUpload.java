package com.assignment.mcb.service.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FtpFileUpload {

	private static final Logger logger = LoggerFactory.getLogger(FtpFileUpload.class);

	private org.mockftpserver.fake.FakeFtpServer fakeFtpServer;

	private FtpClient ftpClient;

	@Value("${file.upload-dir}")
	private String filePath;

	public void setup() throws IOException {
		fakeFtpServer = new FakeFtpServer();
		fakeFtpServer.addUserAccount(new UserAccount("user", "password", "/data"));

		FileSystem fileSystem = new UnixFakeFileSystem();
		fileSystem.add(new DirectoryEntry("/data"));
		fakeFtpServer.setFileSystem(fileSystem);
		fakeFtpServer.setServerControlPort(0);

		fakeFtpServer.start();

		ftpClient = new FtpClient("localhost", fakeFtpServer.getServerControlPort(), "user", "password");
		ftpClient.open();
		try {
			uploadingFilesToFtpServer();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	public void uploadingFilesToFtpServer() throws URISyntaxException, IOException {
		Path newFilePath = Paths.get(System.getProperty("user.home") + java.io.File.separator + "uploads");
		File file = new File(newFilePath.toUri());
		File[] allFiles = file.listFiles();
		Arrays.asList(allFiles).forEach(f -> {
			try {
				ftpClient.putFileToPath(f, "/" + f.getName());
			} catch (IOException e) {
				logger.error("Could not upload the file ! and stack trace is "
						+ e.getStackTrace());
			}

			boolean value = fakeFtpServer.getFileSystem().exists("/" + f.getName());
			assertTrue(value, "File is not uploaded");
		});

		ftpClient.close();

	}

}
