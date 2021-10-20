package com.wellsfargo.rarconsumer;

//import com.mongodb.client.MongoCollection;
//import org.bson.Document;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.data.mongodb.MongoDbFactory;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
//import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
//import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
//import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.annotation.EnableScheduling;

//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientURI;
//import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Component;
//@Configuration
//@PropertySource("classpath:application.properties")
//@EnableAsync
//@EnableMongoRepositories(basePackages = { "com.wellsfargo.rarconsumer" } )
@Component
public class SpringConfig {
//	private static final Logger LOGGER = LoggerFactory.getLogger(SpringConfig.class);
//
//	@Value("${mongodb_url}")
//	private String mongoDBURI;
//
//	@Value("${mongodb_username}")
//	private String mongoDBUserName;
//
//	@Value("${mongodb_password}")
//	private String mongoDBPassword;
//
//	@Value("${mongodb_database}")
//	private String mongoDBDatabase;
//	
//	@Bean
//	public MongoDbFactory createMongoDBFactory(MongoClient mongoClient) {
//		return new SimpleMongoDbFactory(mongoClient, mongoDBDatabase);
//
//	}
//
//	@Bean
//	public MongoClient createMongoClient() {
//		LOGGER.info("Creating MongoClient");
//		this.mongoDBURI = mongoDBURI.replace("$username$", mongoDBUserName);
//		this.mongoDBURI = mongoDBURI.replace("$password$", mongoDBPassword);
//		this.mongoDBURI = mongoDBURI.replace("$db$", mongoDBDatabase);
//		LOGGER.info("Mongo URI: {}", mongoDBURI);
//		MongoClientURI uri = new MongoClientURI(this.mongoDBURI);
//		MongoClient mongoClient = null;
//		try {
//			mongoClient = new MongoClient(uri);
//		} catch (Exception ex) {
//			LOGGER.error("Mongo Connection Error: ", ex);
//		}
//
//		return mongoClient;
//	}
//
//	@Bean
//	public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
//		MappingMongoConverter converter = new MappingMongoConverter(mongoDbFactory, new MongoMappingContext());
//		converter.setTypeMapper(new DefaultMongoTypeMapper(null));
//		converter.afterPropertiesSet();
//		return new MongoTemplate(mongoDbFactory, converter);
//	}
//
//	public MongoCollection<Document> getCollectionData(String collectionName) {
//		MongoDatabase database = createMongoClient().getDatabase(mongoDBDatabase);
//		MongoCollection<Document> collection = database.getCollection(collectionName);
//		return collection;
//	}
	
	public MongoCollection<Document> getCollectionData(String collectionName) {
		 MongoClient mongoClient = MongoClients.create();
		 MongoDatabase database = mongoClient.getDatabase("rar");
		 MongoCollection<Document> collection = database.getCollection(collectionName);
		 return collection;
	}
}
