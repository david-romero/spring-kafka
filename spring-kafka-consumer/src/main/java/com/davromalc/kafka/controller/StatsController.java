package com.davromalc.kafka.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davromalc.kafka.model.Event;
import com.davromalc.kafka.repositories.EventRepository;

@RestController
public class StatsController {

	@Autowired
	private EventRepository eventRepository;
	
	@GetMapping("/api/purge")
	public String purge(){
		eventRepository.deleteAll();
		return "OK";
	}
	
	@GetMapping("/api/time")
	public String getTime(){
		
		List<Event> events = eventRepository.findAll();
		
		List<Event> eventsFiltered = events.parallelStream().filter(e -> e.getDate() != null && e.getInsertDate() != null).collect(Collectors.toList());
		
		long count = events.size();
		
		Optional<Event> eventOlder = eventsFiltered.parallelStream().sorted(Comparator.comparing(Event::getDate)).limit(1).findFirst();
		Optional<Event> eventLatest = eventsFiltered.parallelStream().sorted(Comparator.comparing(Event::getDate).reversed()).limit(1).findFirst();
		
		Optional<Event> eventInsertedOlder = eventsFiltered.parallelStream().sorted(Comparator.comparing(Event::getInsertDate)).limit(1).findFirst();
		Optional<Event> eventInsertedLatest = eventsFiltered.parallelStream().sorted(Comparator.comparing(Event::getInsertDate).reversed()).limit(1).findFirst();
		
		
		Set<Long> duration = eventsFiltered.parallelStream().map(e -> Duration.between(e.getDate() , e.getInsertDate()).getSeconds()).collect(Collectors.toSet());
		
		Optional<Long> max =  duration.parallelStream().max(Comparator.naturalOrder());
		Optional<Long> min =  duration.parallelStream().min(Comparator.naturalOrder());
		OptionalDouble average = duration.parallelStream().mapToDouble(a -> a).average();
		
		
		final LocalDateTime latest = eventLatest.get().getDate();
		
		final LocalDateTime older = eventOlder.get().getDate();
		
		Duration durationKafka = Duration.between(older, latest);
		
		
		final LocalDateTime latestInsert = eventInsertedLatest.get().getInsertDate();
		
		final LocalDateTime olderInsert = eventInsertedOlder.get().getInsertDate();
		
		Duration durationInsert = Duration.between(olderInsert, latestInsert);
		
		Duration durationBetweenOlderKafkaAndLatestInsert = Duration.between(older, latestInsert);
		
		return "Total: " + count  + "\n\n  kafka: \n\n" + humanReadableFormat(durationKafka) + " \n\n insert: \n\n " + humanReadableFormat(durationInsert) + " \n\n kafka + insert:"
				+ " \n\n " + humanReadableFormat(durationBetweenOlderKafkaAndLatestInsert) + " \n\n max: " + max.get() + " \n\n min:  " + min.get() + 
				" \n\n average: " + average.getAsDouble();
		
	}
	
	public String humanReadableFormat(Duration duration) {
	    return duration.toString()
	            .substring(2)
	            .replaceAll("(\\d[HMS])(?!$)", "$1 ")
	            .toLowerCase();
	}
	
}
