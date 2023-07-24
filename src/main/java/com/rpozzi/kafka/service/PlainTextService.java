package com.rpozzi.kafka.service;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.rpozzi.kafka.common.AKafkaConsumer;

@Service
public class PlainTextService extends AKafkaConsumer {
	@Value(value = "${kafka.topic.plaintextinput}")
	private String plainTextKafkaTopic;

	@Override
	protected void customizeConsumerConfigProps() {
		consumerConfigProps.put(ConsumerConfig.GROUP_ID_CONFIG, "streams-plaintext");
	}

	@Override
	protected void consumeMsg(String in) {
		logger.debug("===> running consumeMsg(String in) method ...");
		logger.info("Reading from '" + plainTextKafkaTopic + "' Kafka topic (using SpringBoot Kafka APIs) ...");
		logger.info("Message read : " + in);
	}

}