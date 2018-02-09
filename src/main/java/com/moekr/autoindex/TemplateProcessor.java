package com.moekr.autoindex;

import lombok.extern.apachecommons.CommonsLog;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.io.*;
import java.nio.charset.Charset;

@CommonsLog
class TemplateProcessor {
	private final String template;
	private final TemplateEngine templateEngine;

	TemplateProcessor(String template, Charset charset) {
		StringBuilder stringBuilder = new StringBuilder();
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(template), charset))) {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line).append('\n');
			}
		} catch (IOException e) {
			log.error("Failed to read template file, caused by [" + e.getClass().getSimpleName() + "]" + (e.getMessage() == null ? "" : (": " + e.getMessage())));
			throw new AutoIndexException();
		}
		this.template = stringBuilder.toString();
		StringTemplateResolver templateResolver = new StringTemplateResolver();
		templateResolver.setCacheable(true);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
	}

	void process(IContext context, Writer writer) {
		templateEngine.process(template, context, writer);
	}
}
