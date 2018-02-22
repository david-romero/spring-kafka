package com.codenotfound.kafka.lock;

import java.time.LocalDateTime;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codenotfound.kafka.model.Dto;

@Component
public class RedisLock implements LockHandler<CustomRedisLock> {

	private final RedissonClient redisson;
	
	@Autowired
	public RedisLock(RedissonClient redisson) {
		this.redisson = redisson;
	}

	@Override
	public CustomRedisLock acquire(Dto dto) {
		return new CustomRedisLock(redisson.getFairLock(dto.getId().toString()));
	}
	
	@Override
	public boolean release(CustomRedisLock lock) {
		return lock.expireAt(LocalDateTime.now().plusSeconds(15)); 
	}


}
