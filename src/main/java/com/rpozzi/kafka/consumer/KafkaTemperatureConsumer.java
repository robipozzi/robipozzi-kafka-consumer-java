package com.rpozzi.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaTemperatureConsumer {

	@KafkaListener(groupId = "robi-temperatures", topics = "temperatures", containerFactory = "concurrentKafkaListenerContainerFactory")
	public void consume(String message) {
		System.out.println("message = " + message);
	}
}