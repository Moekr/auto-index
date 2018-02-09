package com.moekr.autoindex;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CommonsLog
class AutoIndex {
	private final Configuration configuration;
	private final TemplateProcessor templateProcessor;

	AutoIndex(String[] args) {
		configuration = Configuration.parse(args);
		log.debug(configuration);
		templateProcessor = new TemplateProcessor(configuration.getTemplate(), configuration.getCharset());
	}

	void run() {
		File sourceRoot = new File(configuration.getSource());
		Assert.check(sourceRoot.exists() && sourceRoot.isDirectory(), "Source not exists or is not a directory");
		File destinationRoot;
		if (configuration.getDestination() != null) {
			destinationRoot = new File(configuration.getDestination());
		} else {
			destinationRoot = sourceRoot;
		}
		run(sourceRoot, destinationRoot);
	}

	private void run(File source, File destination) {
		if (destination.exists()) {
			if (!destination.isDirectory()) {
				log.error("[" + destination.getPath() + "] exists but is not a directory");
				return;
			}
		} else if (!destination.mkdir()) {
			log.error("Failed to create directory [" + destination.getPath() + "]");
			return;
		}
		File[] fileArray = source.listFiles((dir, name) -> !StringUtils.equals(name, configuration.getFileName()));
		if (fileArray == null) {
			log.error("Failed to read children file list of [" + source.getPath() + "]");
			return;
		}
		String destinationFile = destination.getPath() + File.separator + configuration.getFileName();
		try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(destinationFile), configuration.getCharset())) {
			List<FileInfo> fileList = Arrays.stream(fileArray).map(file -> {
				FileInfo fileInfo = new FileInfo();
				fileInfo.setName(file.getName());
				fileInfo.setDirectory(file.isDirectory());
				fileInfo.setSize(fileInfo.isDirectory() ? "-" : ToolKit.convertSize(file.length()));
				fileInfo.setHash(ToolKit.md5Hash(file));
				fileInfo.setDate(ToolKit.convertTimeStamp(file.lastModified()));
				return fileInfo;
			}).sorted((f1, f2) -> {
				if (f1.isDirectory() != f2.isDirectory()) {
					return BooleanUtils.compare(f2.isDirectory(), f1.isDirectory());
				} else {
					return StringUtils.compareIgnoreCase(f1.getName(), f2.getName());
				}
			}).collect(Collectors.toList());
			Context context = new Context();
			context.setVariable("fileList", fileList);
			templateProcessor.process(context, writer);
			if (configuration.isVerbose()) {
				log.info("Write [" + destinationFile + "]");
			}
		} catch (IOException e) {
			log.error("Failed to write [" + destinationFile + "] caused by [" + e.getClass().getSimpleName() + "]" + (e.getMessage() == null ? "" : (": " + e.getMessage())));
		}
		Arrays.stream(fileArray).filter(File::isDirectory).forEach(file -> run(file, new File(destination.getPath() + File.separator + file.getName())));
	}
}
