# Kafka Java Consumer
- [Introduction](#introduction)
- [Setup and run Kafka](#setup-and-run-kafka)
    - [Run Kafka cluster on local environment](#run-Kafka-cluster-on-local-environment)
    - [Run Kafka cluster on Confluent](#run-Kafka-cluster-on-confluent)
    - [Create, delete and describe Kafka topics](#create-delete-and-describe-kafka-topics)
    - [Producers and consumers using Kafka command line tools](#producers-and-consumers-using-Kafka-command-line-tools)
- [How the application works](#how-the-application-works)
    
## Introduction
This repository provides code and scripts to experiment with Kafka consumer Java client technology.

The code is organized in a Maven based project, it uses Spring Boot 3.1.2 and Kafka libraries, injected as Spring dependencies.

To access the code, open a Terminal and start by cloning this repository with the following commands:

```
mkdir $HOME/dev
cd $HOME/dev
git clone https://github.com/robipozzi/robipozzi-kafka-consumer-java
```

## Setup and run Kafka
To see how a Kafka consumer works, you will need to setup a few things in advance, such as a Kafka cluster to interact with and Kafka topic to produce and consume 
messages; also it could be useful to have some Kafka producer to test the messages have been correctly produced by our Kafka consumer: everything is 
already described in details in this GitHub repository https://github.com/robipozzi/robipozzi-kafka, you will find pointers to the appropriate content in the 
paragraphs below.

### Run Kafka cluster on local environment
One option to run a Kafka cluster is obviously installing and running locally, please refer to 
https://github.com/robipozzi/robipozzi-kafka#run-Kafka-cluster-on-local-environment for all the details.

### Run Kafka cluster on Confluent
Another option to setup a Kafka cluster is to use a Cloud solution, for instance Confluent (https://www.confluent.io/), you can refer to https://github.com/robipozzi/robipozzi-kafka#run-Kafka-cluster-on-confluent 
for details regarding this option.

### Create, delete and describe Kafka topics
Once the Kafka cluster has been setup, you can find details on how to manage topics (i.e.: create, delete, ...) at https://github.com/robipozzi/robipozzi-kafka#create-delete-and-describe-kafka-topics

### Producers and consumers using Kafka command line tools
The code in this repository is focused on Kafka consumers exclusively, so you won't find any example of usable Kafka producer

* **Temperature consumer**: a producer that publishes messages that can then be consumed by this consumer can be find at this other repository
https://github.com/robipozzi/robipozzi-kafka-producer-java
* **Quickstart events consumer**: Kafka provides a very convenient way to start producers via command line, refer to 
https://github.com/robipozzi/robipozzi-kafka#producers-and-consumers-using-Kafka-command-line-tools for details and examples
* **Plaintext consumer**: refer to  https://github.com/robipozzi/robipozzi-kafka#producers-and-consumers-using-Kafka-command-line-tools for details and examples
on how to start a producer based on Kafka command line tools
* **Word count consumer**: refer to  https://github.com/robipozzi/robipozzi-kafka#producers-and-consumers-using-Kafka-command-line-tools for details and examples
on how to start a producer based on Kafka command line tools

## How the application works
The application implements several Kafka Consumers that consume messages from several Kafka topics  
(configurable in the **[application.properties](src/main/resources/application.properties)** configuration file:

* **Temperature consumer**: this consumer consumes messages from a *temperatures* topic 
(configurable in the **[application.properties](src/main/resources/application.properties)** configuration file)
* **Quickstart events consumer**: this consumer consumes messages from a *quickstart-events* topic, as published following Kafka Quickstart example
in Kafka official website https://kafka.apache.org/quickstart
* **Plaintext consumer**: this consumer consumes messages from a *streams-plaintext-input* topic, which is the input topic of the sample Kafka Stream 
application example in Kafka official website https://kafka.apache.org/35/documentation/streams/quickstart
* **Word count consumer**: this consumer consumes messages from a *streams-wordcount-output* topic, as published by Kafka Stream application example
in Kafka official website https://kafka.apache.org/35/documentation/streams/quickstart
 
As said in the introduction, the code for this application is based on:
- **Maven**: here is the **[POM](pom.xml)** that defines project configuration; the library dependencies section is reported here below
```
<dependencies>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-json</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.kafka</groupId>
		<artifactId>spring-kafka</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-test</artifactId>
		<scope>test</scope>
	</dependency>
	<dependency>
		<groupId>org.springframework.kafka</groupId>
		<artifactId>spring-kafka-test</artifactId>
		<scope>test</scope>
	</dependency>
</dependencies>
```
	
- **Spring Boot 3.1.2**: the usage of Spring Boot framework v3.1.2, with all its implicit dependencies, is declared in the same **[POM](pom.xml)**; 
as any Spring Boot application, it has a specific configuration file called **[application.properties](src/main/resources/application.properties)**
```
<parent>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-parent</artifactId>
	<version>3.1.2</version>
	<relativePath/> <!-- lookup parent from repository -->
</parent>
```

- **Kafka libraries**: they are injected as Spring dependencies, as it can be seen in the **[POM](pom.xml)** dependencies section.

Every Spring Boot application needs to have a main class annotated as **@SpringBootApplication**; our application main class is 
**[KafkaConsumerApplication](src/main/java/com/rpozzi/kafka/KafkaConsumerApplication.java)**, whose code is reported here below for reference
```
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
```
The application is started via *main()* method and, as you can see, **[KafkaConsumerApplication](src/main/java/com/rpozzi/kafka/KafkaConsumerApplication.java)**
class has several methods, each marked with a **@KafkaListener** annotation: this simple piece of code instructs Spring Boot framework to treat these methods 
as Kafka listener to the topic described in the annotation, calling the method whenever a message is published to the linked topic.


[TODO]