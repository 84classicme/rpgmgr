package io.festerson.rpgvault;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories
public class MongoReactiveConfiguration extends AbstractReactiveMongoConfiguration {

    @Value(value = "${spring.data.mongodb.uri}")
    private String uri;

    @Value(value = "${spring.data.mongodb.database}")
    private String database;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(uri);
    }

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public com.mongodb.reactivestreams.client.MongoClient reactiveMongoClient() {
        return com.mongodb.reactivestreams.client.MongoClients.create(uri);
    }
}
