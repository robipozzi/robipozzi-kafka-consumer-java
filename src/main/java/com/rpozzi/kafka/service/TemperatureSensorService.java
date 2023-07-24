package com.rpozzi.kafka.service;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpozzi.kafka.common.AKafkaConsumer;
import com.rpozzi.kafka.dto.Sensor;

@Service
public class TemperatureSensorService extends AKafkaConsumer {
	@Value(value = "${kafka.topic.temperatures}")
	private String temperaturesKafkaTopic;

	@Override
	protected void consumeMsg(String in) {
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

	@Override
	protected void customizeConsumerConfigProps() {
		consumerConfigProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerConfigProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		consumerConfigProps.put(ConsumerConfig.GROUP_ID_CONFIG, "robi-temperatures");
	}
}