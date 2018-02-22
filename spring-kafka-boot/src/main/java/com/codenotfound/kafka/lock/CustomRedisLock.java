package com.codenotfound.kafka.lock;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

import org.redisson.api.RLock;

public class CustomRedisLock implements FutureLock {

	private final RLock rLock;
	
	private static final ZoneOffset zoneOffsetUTC = ZoneOffset.UTC;
	
	public CustomRedisLock(RLock rLock) {
		this.rLock = rLock;
	}

	@Override
	public void lock() {
		rLock.lock();
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		rLock.lockInterruptibly();
	}

	@Override
	public boolean tryLock() {
		return rLock.tryLock();
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return rLock.tryLock(time, unit);
	}

	@Override
	public void unlock() {
		rLock.unlock();
		
	}

	@Override
	public Condition newCondition() {
		return rLock.newCondition();
	}

	@Override
	public boolean expireAt(LocalDateTime time) {
		return rLock.expireAt(Date.from(time.toInstant(zoneOffsetUTC)));
	}

}
