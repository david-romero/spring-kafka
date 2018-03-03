package com.davromalc.kafka.lock;

import java.time.LocalDateTime;
import java.util.concurrent.locks.Lock;

public interface FutureLock extends Lock {

	boolean expireAt(LocalDateTime time);
	
}
