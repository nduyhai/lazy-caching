package com.example.lazycaching.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.example.lazycaching.repositories.pg.UserRepository;

@Configuration
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class JpaConfig {

}
