package com.rpozzi.kafka.consumer;

import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//@EnableKafka
@Component
public class KafkaConsumer {

	//@KafkaListener(groupId = "group_id", topics = "temperatures", containerFactory = "concurrentKafkaListenerContainerFactory")
	@KafkaListener(topics = "temperatures")
	public void consume(String message) {
		// Print statement
		System.out.println("message = " + message);
	}
}