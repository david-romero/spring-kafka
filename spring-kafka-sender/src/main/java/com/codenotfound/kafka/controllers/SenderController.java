package com.codenotfound.kafka.controllers;

import java.util.stream.IntStream;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.codenotfound.kafka.producer.Sender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
public class SenderController {

	@Autowired
	private Sender sender;
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	private static final String TOPIC = "myTopic";
	
	@PostMapping("/api/send/{messages}/")
	public ResponseEntity<String> send(@PathVariable int messages,@RequestBody String message){
		
		IntStream.range(0, messages).parallel().boxed()
		.map(i ->  new Dto(i, message.concat(RandomStringUtils.randomAlphanumeric(80))))
		.map(this::serialize)
		.forEach(s -> sender.send(TOPIC, s));
		
		return ResponseEntity.ok("OK");
	}
	
	private String serialize(final Dto dto){
		try {
			return objectMapper.writeValueAsString(dto);
		} catch (JsonProcessingException e) {
			return org.apache.commons.lang3.StringUtils.EMPTY;
		}
	}
	
	@Data
	@AllArgsConstructor
	private static class Dto {
		
		private Integer id;
		
		private String data;
		
	}
	
}
