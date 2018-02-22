package com.codenotfound.kafka.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class LockFactory  {

	private static final String REDIS = "redis";

	private static final String CONSUL = "consul";

	private static final String LOCK_TOOL = "lockTool";

	private final LockHandler<CustomRedisLock> redisLock;
	
	private final LockHandler<CustomConsulLock> consulLock;
	
	private final Environment env;
	
	@Autowired
	public LockFactory(RedisLock redisLock, ConsulLock consulLock, Environment env) {
		super();
		this.redisLock = redisLock;
		this.consulLock = consulLock;
		this.env = env;
	}


	@SuppressWarnings("unchecked")
	public <T extends FutureLock> LockHandler<T> getLockHandler(){
		if ( env.getRequiredProperty(LOCK_TOOL).equalsIgnoreCase(CONSUL) ){
			return (LockHandler<T>) consulLock;
		} else if ( env.getRequiredProperty(LOCK_TOOL).equalsIgnoreCase(REDIS) ){
			return (LockHandler<T>) redisLock;
		} else {
			throw new IllegalStateException("Unable to get Lock Tool");
		}
	}
	
}
