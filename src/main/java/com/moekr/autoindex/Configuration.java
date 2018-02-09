package com.moekr.autoindex;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.apachecommons.CommonsLog;

import java.nio.charset.Charset;

@Getter
@ToString
@CommonsLog
class Configuration {
	private String source;
	private String destination;
	private String template;
	private Charset charset;
	private String fileName;
	private boolean verbose;

	private Configuration() {
		verbose = false;
	}

	private void validate() {
		Assert.check(source, "Source directory is not presented");
		Assert.check(template, "Template is not presented");
		if (charset == null) {
			charset = Charset.forName("UTF-8");
		}
		if (fileName == null) {
			fileName = "index.html";
		}
	}

	static Configuration parse(String[] args) {
		Configuration configuration = new Configuration();
		for (int index = 0; index < args.length; index++) {
			switch (args[index]) {
				case "-s":
				case "--source":
					Assert.check(configuration.source == null, "Option -s/--source is duplicate");
					Assert.check(args.length > ++index, "Source directory is not specified after -s/--source");
					configuration.source = args[index];
					break;
				case "-d":
				case "--destination":
					Assert.check(configuration.destination == null, "Option -d/--destination is duplicate");
					Assert.check(args.length > ++index, "Destination directory is not specified after -d/--destination");
					configuration.destination = args[index];
					break;
				case "-t":
				case "--template":
					Assert.check(configuration.template == null, "Option -t/--template is duplicate");
					Assert.check(args.length > ++index, "Template is not specified after -t/--template");
					configuration.template = args[index];
					break;
				case "-c":
				case "--charset":
					Assert.check(configuration.charset == null, "Option -c/--charset is duplicate");
					Assert.check(args.length > ++index, "Charset is not specified after -c/--charset");
					configuration.charset = Charset.forName(args[index]);
					break;
				case "-f":
				case "--file-name":
					Assert.check(configuration.fileName == null, "Option -f/--file-name is duplicate");
					Assert.check(args.length > ++index, "File name is not specified after -f/--file-name");
					configuration.fileName = args[index];
					break;
				case "-v":
				case "--verbose":
					configuration.verbose = true;
					break;
				default:
					Assert.check(false, "Unknown option [" + args[index] + "]");
			}
		}
		configuration.validate();
		return configuration;
	}
}
