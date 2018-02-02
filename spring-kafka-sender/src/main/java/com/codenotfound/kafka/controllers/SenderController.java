package com.codenotfound.kafka.controllers;

import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.codenotfound.kafka.producer.Sender;

@RestController
public class SenderController {

	@Autowired
	private Sender sender;
	
	private static String TOPIC = "myTopic";
	
	@PostMapping("/api/send/{messages}/")
	public ResponseEntity<String> send(@PathVariable int messages,@RequestBody String message){
		
		IntStream.range(0, messages).parallel().boxed()
		.map(Object::toString)
		.forEach(i -> sender.send(TOPIC, message.concat(" : ").concat(i)));
		
		return ResponseEntity.ok("OK");
	}
	
}
