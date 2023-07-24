package com.rpozzi.kafka.service;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.rpozzi.kafka.common.AKafkaConsumer;

@Service
public class QuickstartService extends AKafkaConsumer {
	@Value(value = "${kafka.topic.quickstartevents}")
	private String quickstartEventsKafkaTopic;
	
	@Override
	protected void customizeConsumerConfigProps() {
		consumerConfigProps.put(ConsumerConfig.GROUP_ID_CONFIG, "quickstart");
	}
	
	protected void consumeMsg(String in) {
		logger.debug("===> running consumeMsg(String in) method ...");
		logger.info("====> Reading from '" + quickstartEventsKafkaTopic + "' Kafka topic (using SpringBoot Kafka APIs) ...");
		logger.info("====> Message read : " + in);
	}

}