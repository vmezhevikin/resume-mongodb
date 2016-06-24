package net.devstudy.resume.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {
	
	@Autowired
	private ConfigurableEnvironment environment;

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(environment.getRequiredProperty("email.smtp.server"));
		if (environment.containsProperty("email.smtp.username")) {
			javaMailSender.setUsername(
					environment.resolveRequiredPlaceholders(environment.getRequiredProperty("email.smtp.username")));
			javaMailSender.setPassword(
					environment.resolveRequiredPlaceholders(environment.getRequiredProperty("email.smtp.password")));
			javaMailSender.setPort(Integer.parseInt(environment.getRequiredProperty("email.smtp.port")));
			javaMailSender.setDefaultEncoding("UTF-8");
			javaMailSender.setJavaMailProperties(javaMailProperties());
		}
		return javaMailSender;
	}

	private Properties javaMailProperties() {
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		return properties;
	}
}