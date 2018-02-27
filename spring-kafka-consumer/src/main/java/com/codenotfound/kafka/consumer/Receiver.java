package com.codenotfound.kafka.consumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.codenotfound.kafka.model.Dto;
import com.codenotfound.kafka.model.Event;
import com.codenotfound.kafka.repositories.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import consul.ConsulException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Receiver {

	private final EventRepository eventRepository;
	
	private final Function<Dto, Optional<Event>> mapper;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	public Receiver(final Function<Dto, Optional<Event>> mapper,final EventRepository eventRepository) {
		this.mapper = mapper;
		this.eventRepository = eventRepository;
	}

	@KafkaListener(topics = "myTopic", containerFactory = "kafkaListenerContainerFactory", errorHandler = "kafkaListenerErrorHandler")
	public void receive(@Payload List<String> messages, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
			@Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topic, @Header("kafka_receivedTimestamp") long ts,
			Acknowledgment ack) throws ConsulException, IOException {
		log.info("received from partition:[{}] [{}] elements with payload=[{}] , topic:[{}] , timestamp:[{}]",
				partition, messages.size(), StringUtils.collectionToCommaDelimitedString(messages),
				StringUtils.collectionToCommaDelimitedString(topic), ts);
		
		final Iterator<String> messagesIterator = messages.iterator();
		final List<Event> eventsToPersist = new ArrayList<>(messages.size());
		while ( messagesIterator.hasNext() ){
			final Dto dtoParsed = deserialize(messagesIterator.next());
			final Dto dto =  Dto.builder().id(dtoParsed.getId()).timestamp(ts).partition(partition).data(dtoParsed.getData()).build();
			final Optional<Event> event = mapper.apply(dto);
			if (event.isPresent()){
				eventsToPersist.add(event.get());
			}
		}		
		
		if (!eventsToPersist.isEmpty()) {
			log.info("Persisting [{}] objects", eventsToPersist.size());
			eventRepository.save(eventsToPersist);
			ack.acknowledge();
		}
	}
	
	private Dto deserialize(final String json){
		try {
			return objectMapper.readValue(json, Dto.class);
		} catch (IOException e) {
			log.error(String.format("Cannot deserialize json: [%s]", json),e);
			return null;
		}
	}
	
}
