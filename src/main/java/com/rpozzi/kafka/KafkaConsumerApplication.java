package com.rpozzi.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.KafkaListener;
import com.rpozzi.kafka.service.TemperatureSensorService;

@SpringBootApplication
@ComponentScan(basePackages = { "com.rpozzi.kafka" })
public class KafkaConsumerApplication {
	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerApplication.class);
	@Autowired
	private TemperatureSensorService temperatureSensorSrv;
	   
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(KafkaConsumerApplication.class, args);
	    logger.info("Application " + ctx.getId() + " started !!!");
	}
	
	/*****************************************************/
	/****** Kafka Listeners methods - Section START ******/
	/*****************************************************/
	
	// Kafka Listener for temperature and humidity sensor - using SpringBoot Kafka APIs
	@KafkaListener(groupId = "robi-temperatures", topics = "temperatures")
	public void consumeTemperature(String in) {
		logger.debug("===> running consumeTemperature(String in) method ...");
		temperatureSensorSrv.consume(in);
	}
	
	/***************************************************/
	/****** Kafka Listeners methods - Section END ******/
	/***************************************************/

}