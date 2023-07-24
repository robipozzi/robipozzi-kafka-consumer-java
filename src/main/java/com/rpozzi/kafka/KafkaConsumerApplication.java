package com.rpozzi.kafka;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.KafkaListener;
import com.rpozzi.kafka.service.PlainTextService;
import com.rpozzi.kafka.service.QuickstartService;
import com.rpozzi.kafka.service.TemperatureSensorService;
import com.rpozzi.kafka.service.WordCountService;

@SpringBootApplication
@ComponentScan(basePackages = { "com.rpozzi.kafka" })
public class KafkaConsumerApplication {
	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerApplication.class);
	@Value(value = "${spring.kafka.bootstrap-servers}")
	private String kafkaBootstrapServers;
	@Autowired
	private TemperatureSensorService temperatureSensorSrv;
	@Autowired
	private QuickstartService quickstartSrv;
	@Autowired
	private PlainTextService plainTextSrv;
	@Autowired
	private WordCountService wordCountSrv;
	   
	public static void main(String[] args) {
		SpringApplication.run(KafkaConsumerApplication.class, args);
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
		
	// Kafka Listener for Quickstart Events (see Apache Kafka Get started https://kafka.apache.org/quickstart) - using SpringBoot Kafka APIs
	@KafkaListener(groupId = "quickstart", topics = "quickstart-events")
	public void consumeQuickstartEvents(String in) {
		logger.debug("===> running consumeQuickstartEvents(String in) method ...");
		quickstartSrv.consume(in);
	}
	
	// Kafka Listener for plain text input
	@KafkaListener(groupId = "streams-plaintext", topics = "streams-plaintext-input")
	public void consumePlainText(String in) {
		logger.debug("===> running consumePlainText(String in) method ...");
		plainTextSrv.consume(in);
	}
	
	// [NOT USED] Kafka Listener for plain text input - using Kafka client APIs
	/*public void consumePlainText() {
	    logger.debug("===> running consumePlainText() method ...");
		Properties props = new Properties();
        props.put("bootstrap.servers", kafkaBootstrapServers);
        props.put("group.id", "streams-plaintext");
        props.put("enable.auto.commit", "false");
        props.put("session.timeout.ms", "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest");
        
		logger.info("Reading from '" + plainTextKafkaTopic + "' Kafka topic (using Kafka client APIs) ...");
		final KafkaConsumer<Integer, String> consumer = new KafkaConsumer<>(props);
		
        consumer.subscribe(Collections.singletonList(plainTextKafkaTopic));
        ConsumerRecords<Integer, String> records = consumer.poll(Duration.ofMillis(10000));

        System.out.println("size of records polled is "+ records.count());
        for (ConsumerRecord<Integer, String> record : records) {
        	logger.info("----- Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset());
        }

        consumer.commitSync();
        consumer.close();
	}*/
	
	// Kafka Listener for plain text input - using SpringBoot Kafka APIs
	@KafkaListener(groupId = "streams-plaintext", topics = "streams-wordcount-output")
	public void consumeWordCount(String in) {
		logger.debug("===> running consumeWordCount(String in) method ...");
		wordCountSrv.consume(in);
	}
	
	/*****************************************************/
	/****** Kafka Listeners methods - Section END ******/
	/*****************************************************/
	
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
			
			logger.debug("Print application configuration parameters");
			logger.debug("************** Application configuration parameters - START **************");
			logger.debug("Kafka Bootstrap Servers :  " + kafkaBootstrapServers);
			logger.debug("************** Application configuration parameters - END **************");
			
			logger.info("Application " + ctx.getId() + " started !!!");
		};
	}

}