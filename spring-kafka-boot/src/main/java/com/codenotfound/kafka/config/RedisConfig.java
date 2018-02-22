package com.codenotfound.kafka.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

	@Bean
	public RedissonClient redisson() {
		Config config = new Config();
		config.useSingleServer().setAddress("redis://redis:6379");
		return Redisson.create(config);
	}

}
