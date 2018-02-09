package com.moekr.autoindex;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
class FileInfo {
	private String name;
	private boolean directory;
	private String size;
	private String hash;
	private String date;
}
