# spring-kafka

This repository contains the source code for the spring-kafka examples posted on [https://david-romero.github.io/articles/2018-02/consul-vs-redis-lock](https://david-romero.github.io//articles/2018-02/consul-vs-redis-lock)

This repository contains two implementarions of the spring-kafka integration:
	* A producer
	* A consumer

The producer has a endpoint in charge of sending messages to kafka.
The consumer pull the messages from kafka and store them in mongo in case it can obtain the relevant lock. There is implemented two strategies: Redis and Consul. You can choose between any of the two by setting the environment variable "lockTool"

In case of questions or remarks please leave a comment in the respective blog post. Thanks!
