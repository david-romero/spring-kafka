package com.codenotfound.kafka.lock;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codenotfound.kafka.model.Dto;
import com.codenotfound.kafka.model.Event;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DtoToEventMapper implements Function<Dto, Optional<Event>> {

	private final LockFactory lockFactory;
	
	private final String hostname;
	
	private final ZoneId zoneId = ZoneId.systemDefault();
	
	@Autowired
	public DtoToEventMapper(LockFactory lockFactory) throws UnknownHostException {
		this.lockFactory = lockFactory;
		this.hostname = Inet4Address.getLocalHost().getHostName();
	}


	@Override
	public Optional<Event> apply(Dto dto) {
		final FutureLock lock = lockFactory.getLockHandler().acquire(dto);
		boolean acquired = lock.tryLock();
		if (acquired) {
			log.info("Adquired lock for id [{}] and host [{}]", dto.getId(), hostname);
			final Event event = new Event();
			final LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(dto.getTimestamp()), zoneId);
			event.setDate(date);
			event.setInsertDate(LocalDateTime.now());
			event.setPayload(dto.getData());
			event.setPartition(dto.getPartition());
			lock.expireAt(LocalDateTime.now().plusSeconds(15));
			return Optional.of(event);
		} else {
			log.info("hostname [{}] did not adquire the lock for id [{}]", hostname, dto.getId());
			return Optional.empty();
		}
	}
	
}
