package com.guan.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.mongodb.Mongo;
import com.mongodb.MongoClientOptions;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;

@Configuration
public class TestMongoConfig {
   @Autowired
   private MongoProperties properties;

   @Autowired(required = false)
   private MongoClientOptions options;
   
   @Autowired
   private Environment environment;

   @Bean(destroyMethod = "close")
   public Mongo mongo(MongodProcess mongodProcess) throws IOException {
       Net net = mongodProcess.getConfig().net();
       properties.setHost(net.getServerAddress().getHostName());
       properties.setPort(net.getPort());
       return properties.createMongoClient(this.options, this.environment);
   }

   @Bean(destroyMethod = "stop")
   public MongodProcess mongodProcess(MongodExecutable mongodExecutable) throws IOException {
       return mongodExecutable.start();
   }

   @Bean(destroyMethod = "stop")
   public MongodExecutable mongodExecutable(MongodStarter mongodStarter, IMongodConfig iMongodConfig) throws IOException {
       return mongodStarter.prepare(iMongodConfig);
   }

   @Bean
   public IMongodConfig mongodConfig() throws IOException {
       return new MongodConfigBuilder().version(Version.Main.PRODUCTION).build();
   }

   @Bean
   public MongodStarter mongodStarter() {
       return MongodStarter.getDefaultInstance();
   }

}