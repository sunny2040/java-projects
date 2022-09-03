package com.surge.vms.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class SpringMailConfig {



/*	@Bean(name = "textTemplateEngine")
	public TemplateEngine textTemplateEngine() {
		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.addTemplateResolver(textTemplateResolver());
		return templateEngine;
	}

	private ITemplateResolver textTemplateResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("/templates/");
		templateResolver.setSuffix(".txt");
		templateResolver.setTemplateMode(TemplateMode.TEXT);
		templateResolver.setCharacterEncoding("UTF8");
		templateResolver.setCheckExistence(true);
		templateResolver.setCacheable(false);
		return templateResolver;
	}*/

	private ITemplateResolver htmlTemplateResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding("UTF8");
		templateResolver.setCheckExistence(true);
		templateResolver.setCacheable(false);
		return templateResolver;
	}

	@Bean(name = "htmlTemplateEngine")
	public TemplateEngine htmlTemplateEngine() {
		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.addTemplateResolver(htmlTemplateResolver());
		return templateEngine;
	}

	/*private ITemplateResolver fileTemplateResolver() {
		FileTemplateResolver templateResolver = new FileTemplateResolver();
		templateResolver.setPrefix("/resources/templates/"); //Change based on your environment
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding("UTF8");
		templateResolver.setCheckExistence(true);
		templateResolver.setCacheable(false);
		return templateResolver;
	}

	@Bean(name = "fileTemplateEngine")
	public TemplateEngine fileTemplateEngine() {
		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.addTemplateResolver(fileTemplateResolver());
		return templateEngine;
	}*/

}
