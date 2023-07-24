package com.rpozzi.kafka.common;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;

public abstract class AKafkaConsumer implements IKafkaConsumer {
	protected final Logger logger;
	@Autowired
	protected KafkaProperties kafkaProperties;
	protected Map<String, Object> consumerConfigProps = null;

	protected AKafkaConsumer() {
		super();
		logger = LoggerFactory.getLogger(this.getClass());
	}
	
	protected abstract void customizeConsumerConfigProps();

	@Bean
	protected Map<String, Object> consumerConfigs() {
		logger.debug("===> running consumerConfigs() method ...");
		if (consumerConfigProps == null) {
			logger.debug("##### instantiating consumerConfigProps Map ...");
			consumerConfigProps = new HashMap<>(kafkaProperties.buildConsumerProperties());
			logger.debug("##### customizing consumerConfigProps Map ...");
			customizeConsumerConfigProps();
		}
		consumerConfigProps.forEach((key, value) -> logger.debug("KEY = " + key + " - VALUE = " + value));
		return consumerConfigProps;
	}

	@Override
	public void consume(String in) {
		logger.debug("===> running consume(String in) method ...");
		consumerConfigs();
		consumeMsg(in);
	}

	protected abstract void consumeMsg(String in);

}