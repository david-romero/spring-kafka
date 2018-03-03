package com.davromalc.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Sender {


  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  private ListenableFutureCallback<? super SendResult<String, String>> callback;

  public void send(String topic, String payload) {
    log.info("sending payload='{}' to topic='{}'", payload, topic);
    ListenableFuture<SendResult<String, String>> future =  kafkaTemplate.send(topic, payload);
    future.addCallback(callback);
  }
}
