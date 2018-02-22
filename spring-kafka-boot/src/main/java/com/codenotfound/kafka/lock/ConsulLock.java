package com.codenotfound.kafka.lock;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codenotfound.kafka.model.Dto;

import consul.Consul;
import consul.KeyValue;

@Component
public class ConsulLock implements LockHandler<CustomConsulLock> {

	private final KeyValue keyValue;
	
	private final String sessionId;
	
	@Autowired
	public ConsulLock(Consul consul, String sessionId) {
		this.keyValue = consul.keyStore();
		this.sessionId = sessionId;
	}

	@Override
	public CustomConsulLock acquire(Dto dto) {
		return new CustomConsulLock(dto, keyValue, sessionId);
	}
	
	@Override
	public boolean release(CustomConsulLock lock) {
		return lock.expireAt(LocalDateTime.now().plusSeconds(15));
	}


}
