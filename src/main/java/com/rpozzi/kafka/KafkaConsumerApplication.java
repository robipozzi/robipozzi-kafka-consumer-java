package com.rpozzi.kafka;

import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@ComponentScan(basePackages = {"com.rpozzi.kafka"})
public class KafkaConsumerApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(KafkaConsumerApplication.class, args);
		System.out.println("Application " + ctx.getApplicationName() + " started");
	}
	
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println("****************************************");
			System.out.println("Let's inspect the beans provided by Spring Boot:");
			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}
			System.out.println("****************************************");
		};
    }
	
	/*@Bean
    public NewTopic topic() {
        return TopicBuilder.name("temperatures")
                .partitions(10)
                .replicas(1)
                .build();
    }*/

    @KafkaListener(id = "robi-temperatures", topics = "temperatures")
    public void consume(String in) {
        System.out.println(in);
    }

}