package com.codenotfound.kafka.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.codenotfound.kafka.model.Event;

@Repository
public interface EventRepository extends MongoRepository<Event, Long> {

	Event findFirst1ByOrderByDateDesc();

	Event findFirst1ByOrderByDateAsc();

	Event findFirst1ByOrderByInsertDateDesc();

	Event findFirst1ByOrderByInsertDateAsc();

}
