package com.codenotfound.kafka.lock;

import com.codenotfound.kafka.model.Dto;

public interface LockHandler<T extends FutureLock> {

	 T acquire(Dto dto);
	
	boolean release(FutureLock lock);
	
}
