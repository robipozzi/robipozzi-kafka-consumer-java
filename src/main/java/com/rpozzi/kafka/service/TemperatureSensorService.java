package com.rpozzi.kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpozzi.kafka.dto.Sensor;

@Service
public class TemperatureSensorService {
	private static final Logger logger = LoggerFactory.getLogger(TemperatureSensorService.class);
	@Value(value = "${kafka.topic.temperatures}")
	private String temperaturesKafkaTopic;

	public void consume(String in) {
		logger.debug("===> running consumeMsg(String in) method ...");
		logger.info("Reading from '" + temperaturesKafkaTopic + "' Kafka topic (using SpringBoot Kafka APIs) ...");
		ObjectMapper mapper = new ObjectMapper();
		try {
			logger.debug("Message read : " + in);
			Sensor sensor = mapper.readValue(in, Sensor.class);
			logger.info("Temperature = " + sensor.getTemperature() + " - Humidity = " + sensor.getHumidity());
		} catch (JsonMappingException e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

}