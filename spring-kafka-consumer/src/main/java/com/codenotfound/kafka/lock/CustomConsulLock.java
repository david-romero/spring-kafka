package com.codenotfound.kafka.lock;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

import com.codenotfound.kafka.model.Dto;

import consul.ConsulException;
import consul.KeyValue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class CustomConsulLock implements FutureLock {

	private final String id;
	private final String sessionId;
	private final KeyValue keyValue;
	private String hostname;
	private final ScheduledExecutorService scheduler;

	public CustomConsulLock(final Dto dto, final KeyValue keyValue, final String sessionId, final ScheduledExecutorService scheduler) {
		this.id = dto.getId().toString();
		this.sessionId = sessionId;
		this.keyValue = keyValue;
		this.scheduler = scheduler;
		try {
			hostname = Inet4Address.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			hostname = "Unkown";
		}
	}

	@Override
	public void lock() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean tryLock() {
		try {
			return keyValue.acquireLock(id, id, sessionId);
		} catch (ConsulException e) {
			log.warn(String.format("Cannot get lock for id: %s", id), e);
			return false;
		}
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		if (unit == TimeUnit.SECONDS) {
			boolean locked = tryLock();
			if (time > 15) {
				return locked && expireAt(LocalDateTime.now().plusSeconds(time));
			} else {
				if (locked) {
					unlock();
				}
				return locked;
			}
		} else {
			throw new IllegalArgumentException("Only seconds is allowed");
		}
	}

	@Override
	public void unlock() {
		expireAt(LocalDateTime.now().plusSeconds(15));
	}

	@Override
	public Condition newCondition() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean expireAt(LocalDateTime time) {
		final Runnable releaseLock = () -> {
			try {
				final boolean released = keyValue.releaseLock(id, id, sessionId);
				log.info("Released? {} lock for id [{}] and host [{}] and sessionID [{}]", released, id, hostname,
						sessionId);
			} catch (ConsulException e) {
				log.error(String.format("Cannot release lock [%s]", id), e);
			}
		};
		final ScheduledFuture<?> schedulerFuture = scheduler.schedule(releaseLock,
				Duration.between(LocalDateTime.now(), time).getSeconds(), TimeUnit.SECONDS);
		return schedulerFuture.isCancelled();
	}

}
