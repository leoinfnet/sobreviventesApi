package br.com.infnet.sobreviventesapi.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
@Configuration
public class RedisCacheConfig {
//    @Bean
//    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
//
//        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofSeconds(30))
//                .serializeValuesWith(
//                        RedisSerializationContext.SerializationPair
//                                .fromSerializer(RedisSerializer.json())
//                );
//
//        return RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(config)
//                .build();
//    }
@Bean
public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {

    RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
          //  .entryTtl(Duration.ofSeconds(30))
            .serializeValuesWith(
                    RedisSerializationContext.SerializationPair
                            .fromSerializer(RedisSerializer.java())
            );

    return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config)
            .build();
}
}
