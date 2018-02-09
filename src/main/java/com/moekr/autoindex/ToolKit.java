package com.moekr.autoindex;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

abstract class ToolKit {
	static String convertSize(long size) {
		if (size < 0) {
			return "0B";
		} else if (size < 1024) {
			return size + "B";
		} else if (size < 1024 * 1024) {
			return (int) (size / 1024.0 * 100) / 100.0 + "KB";
		} else if (size < 1024 * 1024 * 1024) {
			return (int) (size / (1024.0 * 1024.0) * 100) / 100.0 + "MB";
		} else {
			return (int) (size / (1024.0 * 1024.0 * 1024.0) * 100) / 100.0 + "GB";
		}
	}

	static String md5Hash(File file) {
		String result;
		if (file.isFile()) {
			try (InputStream inputStream = new FileInputStream(file)) {
				result = DigestUtils.md5Hex(inputStream);
			} catch (IOException e) {
				result = "-";
			}
		} else {
			result = "-";
		}
		return result;
	}

	public static String convertTimeStamp(long timeStamp) {
		LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), ZoneId.systemDefault());
		dateTime = dateTime.withNano(0);
		return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE) + " " + dateTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
	}
}
