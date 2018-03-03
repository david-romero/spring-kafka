package com.davromalc.kafka.lock;

import com.davromalc.kafka.model.Dto;

public interface LockHandler<T extends FutureLock> {

	 T acquire(Dto dto);
	
	boolean release(FutureLock lock);
	
}
