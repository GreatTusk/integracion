package com.f776.vientosdelsur.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public CacheStore<String, Integer> userCache() {
        // 15 minutes
        return new CacheStore<>(900, TimeUnit.SECONDS);
    }
}
