
package com.assignment.mcb.schedular;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.assignment.mcb.enums.Status;
import com.assignment.mcb.model.FileDetails;
import com.assignment.mcb.property.Constants;
import com.assignment.mcb.service.impl.FileStorageServiceImpl;
import com.assignment.mcb.service.impl.FtpFileUpload;

@Component
public class FileReadWriteSchedular {

	private static final Logger logger = LoggerFactory.getLogger(FileReadWriteSchedular.class);

	@Autowired
	private FtpFileUpload ftpUpload;

	@Autowired
	FileStorageServiceImpl FileStorageService;

	/*
	 * Scheduler to run every hour to fetch all the Files from database of status
	 * Completed uploading them to FTP server
	 */
	@Scheduled(initialDelayString = Constants.INITIAL_DELAY_STRING, fixedDelayString = Constants.FIXED_DElAY_STRING)
	public void fileReadWriteUpload() throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		Date now = new Date();
		String strDate = sdf.format(now);
		logger.info("Java cron job expression:: " + strDate);
		List<FileDetails> findAllByStatus = FileStorageService.getAllFilesByStatus(Status.COMPLETED.name());
		FileStorageService.saveFileInFileSystem(findAllByStatus);
		ftpUpload.setup();

	}

	/*
	 * Scheduler to delete all the files in the directory
	 */
	@Scheduled(initialDelayString = Constants.INITIAL_DELAY_STRING_DELETE, fixedDelayString = Constants.FIXED_DElAY_STRING)
	public void deleteFilesFromDirectory() throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		Date now = new Date();
		String strDate = sdf.format(now);
		logger.info("Java cron job for delete operation :: " + strDate);
		String filDir = System.getProperty("user.home") + java.io.File.separator + "uploads";
		Arrays.stream(new File(filDir).listFiles()).forEach(File::delete);
	}
}
