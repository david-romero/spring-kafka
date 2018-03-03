package com.davromalc.kafka.lock;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.davromalc.kafka.model.Dto;

import consul.Consul;
import consul.KeyValue;

@Component
class ConsulLockHandler implements LockHandler<CustomConsulLock> {

	private final KeyValue keyValue;
	
	private final String sessionId;
	
	private final ScheduledExecutorService scheduledExecutorService;
	
	@Autowired
	public ConsulLockHandler(Consul consul, String sessionId) {
		this.keyValue = consul.keyStore();
		this.sessionId = sessionId;
		this.scheduledExecutorService = Executors.newScheduledThreadPool(10);
	}

	@Override
	public CustomConsulLock acquire(Dto dto) {
		return new CustomConsulLock(dto, keyValue, sessionId,scheduledExecutorService);
	}
	
	@Override
	public boolean release(FutureLock lock) {
		return lock.expireAt(LocalDateTime.now().plusSeconds(15));
	}


}
