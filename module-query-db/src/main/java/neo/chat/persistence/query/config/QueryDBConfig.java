package neo.chat.persistence.query.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableMongoRepositories(basePackages = QueryDBConfig.BASE_PACKAGE)
@EnableJpaRepositories(
        basePackages = QueryDBConfig.BASE_PACKAGE,
        transactionManagerRef = QueryDBConfig.TRANSACTION_MANAGER
)
public class QueryDBConfig {

    public static final String BASE_PACKAGE = "neo.chat.persistence.query";
    public static final String TRANSACTION_MANAGER = "queryTM";

    private final String connectionString;
    private final String database;

    @Autowired
    public QueryDBConfig(
            @Value("${spring.data.mongodb.uri}") String connectionString,
            @Value("${spring.data.mongodb.database}") String database
    ) {
        this.connectionString = connectionString;
        this.database = database;
    }

    @Bean
    public PlatformTransactionManager queryTM(MongoDatabaseFactory mdf) {
        return new MongoTransactionManager(mdf);
    }

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build());
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mdf, MongoMappingContext context) {
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mdf), context);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoClient(), database), converter);
    }

}
