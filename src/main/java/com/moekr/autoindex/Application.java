package com.moekr.autoindex;

import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public abstract class Application {
	public static void main(String[] args) {
		try {
			new AutoIndex(args).run();
		} catch (AutoIndexException e) {
			log.error("Exit with error");
			System.exit(1);
		} catch (Throwable e) {
			log.error("Exit with error caused by [" + e.getClass().getSimpleName() + "]" + (e.getMessage() == null ? "" : (": " + e.getMessage())));
			System.exit(2);
		}
	}
}
