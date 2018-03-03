package com.davromalc.kafka.consumer;

import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaListenerErrorHandler implements org.springframework.kafka.listener.KafkaListenerErrorHandler {

	@Override
	public Object handleError(Message<?> message, ListenerExecutionFailedException exception) throws Exception {
		final String msgError = "Unable to receiver message [%s] with payload [%s]";
		log.error(String.format(msgError, message.getHeaders().toString(), message.getPayload()), exception);
		return null;
	}

}
