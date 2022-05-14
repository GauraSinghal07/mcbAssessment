package mcb.assignmentmcb.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CommonUtils {

	public static String convertTimeStampToString(Timestamp timestamp) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String timestp = dateFormat.format(timestamp);
		return timestp;
	}

	public static String convertTimeStampToFormat(Timestamp timestamp) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String timestp = dateFormat.format(timestamp);
		return timestp;
	}
	
	public static String createUniqueFileName(Long datetime, String fileName) {
		String[] split = fileName.split("\\.");
		String uniqueFleName = split[0].concat(datetime.toString()).concat(".").concat(split[1]);
		return uniqueFleName;
	}

}
