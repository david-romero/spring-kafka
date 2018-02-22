package com.codenotfound.kafka.config;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import consul.Consul;
import consul.ConsulException;

@Configuration
public class ConsulConfig {

	private final String hostname;

	private final Random random = new Random();

	public ConsulConfig() throws UnknownHostException {
		this.hostname = Inet4Address.getLocalHost().getHostName() + random.nextInt(80000);
	}

	@Bean
	public Consul consul() {
		return new Consul("http://localhost", 8500);
	}

	@Bean
	public String sessionId(final Consul consul) throws ConsulException {
		return consul.session().create(hostname);
	}

}
