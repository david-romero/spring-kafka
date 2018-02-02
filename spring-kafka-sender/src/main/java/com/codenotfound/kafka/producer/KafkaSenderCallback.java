package com.codenotfound.kafka.producer;

import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaSenderCallback implements ListenableFutureCallback<SendResult<String, String>> {

	@Override
	public void onSuccess(SendResult<String, String> result) {
		log.info("Message sent to kafka. Topic: {}, timestamp: {}", result.getRecordMetadata().topic(), result.getRecordMetadata().timestamp());		
	}

	@Override
	public void onFailure(Throwable oops) {
		log.error("Cannot send message to kafka", oops);
	}

}
