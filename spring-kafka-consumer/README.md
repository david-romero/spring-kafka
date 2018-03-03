# spring-boot-kafka-lisener

A spring boot service which is able to listen from a kafka topic.

It receives messages from Kafka and store them in mongo. To avoid duplicates with uses a locking mechanism with two strategies: redis and consul. You need to choose a straregy through environment variables. If you put lockTool=consul, the strategy of locking is handled by consul, however, if you put lockTool=redis, the strategy of locking is handled by redis.

Furthemore, you need to set up kafka group by through environment variables too.

Example: groupId=boot1


