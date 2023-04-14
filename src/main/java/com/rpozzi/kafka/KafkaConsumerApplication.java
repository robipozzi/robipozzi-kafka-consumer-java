package com.rpozzi.kafka;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@ComponentScan(basePackages = { "com.rpozzi.kafka" })
public class KafkaConsumerApplication {
	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerApplication.class);
	   
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(KafkaConsumerApplication.class, args);
		logger.info("Application " + ctx.getApplicationName() + " started !!!");
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			logger.debug("Let's inspect the beans provided by Spring Boot:");
			logger.debug("************** Spring Boot beans - START **************");
			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				logger.debug(beanName);
			}
			logger.debug("************** Spring Boot beans - END **************");
		};
	}
	
	@KafkaListener(groupId = "robi-temperatures", topics = "temperatures")
	public void consumeTemperature(String in) {
		logger.info(in);
	}

	@KafkaListener(groupId = "quickstart", topics = "quickstart-events")
	public void consumeQuickstartEvents(String in) {
		logger.info(in);
	}

}