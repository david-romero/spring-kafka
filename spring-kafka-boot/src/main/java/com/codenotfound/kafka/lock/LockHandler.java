package com.codenotfound.kafka.lock;

import com.codenotfound.kafka.model.Dto;

public interface LockHandler<T extends java.util.concurrent.locks.Lock> {

	 T acquire(Dto dto);
	
	boolean release(T lock);
	
}
