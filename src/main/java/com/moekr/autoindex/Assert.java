package com.moekr.autoindex;

import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
abstract class Assert {
	static void check(Object object, String message) {
		boolean assertion;
		if (object instanceof Boolean) {
			assertion = (Boolean) object;
		} else {
			assertion = object != null;
		}
		if (!assertion) {
			log.error(message);
			throw new AutoIndexException(message);
		}
	}
}
