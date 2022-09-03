package com.surge.vms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

/**
 * @author ${user}
 * @desc Mail configuration to tell where the template need to consumed. Set
 *       this as Primary because in Service there is another Mail Configuration.
 */
@Configuration
public class MailConfig {
	/*
	 * This is the bean tell where the template need to be consumed Set this as
	 * Primary because in Service there is another mail cofig
	 */
	@Primary
	@Bean
	public FreeMarkerConfigurationFactoryBean factoryBean() {
		FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
		bean.setTemplateLoaderPath("classpath:/templates");
		return bean;
	}
}
