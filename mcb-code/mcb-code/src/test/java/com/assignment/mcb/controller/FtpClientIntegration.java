package com.assignment.mcb.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.springframework.beans.factory.annotation.Value;

import com.assignment.mcb.service.impl.FtpClient;

public class FtpClientIntegration {

	private org.mockftpserver.fake.FakeFtpServer fakeFtpServer;

	private FtpClient ftpClient;

	@Value("${file.upload-dir}")
	private String filePath;

	@BeforeEach
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
	}

	@Test
	public void givenLocalFile_whenUploadingIt_thenItExistsOnRemoteLocation() throws URISyntaxException, IOException {
		Path newFilePath = Paths.get(System.getProperty("user.home") + java.io.File.separator + "uploads");
		File file = new File(newFilePath.toUri());
		File[] allFiles = file.listFiles();
		for (File f : allFiles) {
			ftpClient.putFileToPath(f, "/" + f.getName());
			boolean value = fakeFtpServer.getFileSystem().exists("/" + f.getName());
			System.out.println(value);
		}

	}

	public void listFilesForFolder(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				System.out.println(fileEntry.getName());
			}
		}
	}
	/*
	 * @AfterEach public void teardown() throws IOException { ftpClient.close();
	 * fakeFtpServer.stop(); }
	 */

}
