package net.devstudy.resume.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationListener implements ServletContextListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationListener.class);

	@Value("${application.production}")
	private boolean production;

	@Value("${css.version}")
	private String cssVersion;

	@Value("${js.version}")
	private String jsVersion;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute("production", production);
		sce.getServletContext().setAttribute("cssVersion", cssVersion);
		sce.getServletContext().setAttribute("jsVersion", jsVersion);
		LOGGER.info("Application started");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LOGGER.info("Application destroyed");
	}
}
