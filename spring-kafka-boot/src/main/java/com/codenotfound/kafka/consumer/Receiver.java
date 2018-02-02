package com.codenotfound.kafka.consumer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.codenotfound.kafka.model.Event;
import com.codenotfound.kafka.repositories.EventRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Receiver {

	@Autowired
	private EventRepository eventRepository;
	
	@KafkaListener(topics = "myTopic", containerFactory = "kafkaListenerContainerFactory" , errorHandler = "kafkaListenerErrorHandler")
	public void receive(@Payload String message,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
			@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Header("kafka_receivedTimestamp") long ts,
			Acknowledgment ack) {
		log.info("received payload=[{}] , from partition:[{}] topic:[{}] , timestamp:[{}]", message, partition, topic, ts);
		final Event event = new Event();
		final LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(ts), ZoneId.systemDefault());
		event.setDate(date);
		event.setInsertDate(LocalDateTime.now());
		event.setPayload(message);
		final Event persistedEvent = eventRepository.save(event);
		log.info("persisted event: {}", persistedEvent);
		ack.acknowledge();
		log.info("ack sent payload=[{}] , from partition:[{}] topic:[{}] , timestamp:[{}]", message, partition, topic, ts);
	}
}
