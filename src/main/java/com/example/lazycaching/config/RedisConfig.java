package com.example.lazycaching.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import com.example.lazycaching.repositories.cache.UserCachedRepository;

@Configuration
@EnableRedisRepositories(basePackageClasses = UserCachedRepository.class)
public class RedisConfig {

}
