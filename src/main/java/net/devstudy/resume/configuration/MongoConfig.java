package net.devstudy.resume.configuration;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;

@Configuration
@PropertySource("classpath:application.properties")
@EnableMongoRepositories("net.devstudy.resume.repository.storage")
public class MongoConfig {
	
	@Autowired
	private Environment environment;
	
	@Bean(/*destroyMethod = "close"*/)
	public MongoClient mongoClient() throws NumberFormatException, UnknownHostException, IllegalStateException {
		return new MongoClient(
				environment.getRequiredProperty("mongo.host"), 
				Integer.parseInt(environment.getRequiredProperty("mongo.port")));
	}
	
	public @Bean MongoTemplate mongoTemplate() throws NumberFormatException, UnknownHostException, IllegalStateException {
		MongoTemplate mongoTemplate = new MongoTemplate(
				mongoClient(),
				environment.getRequiredProperty("mongo.db"));
		return mongoTemplate;
	}
}