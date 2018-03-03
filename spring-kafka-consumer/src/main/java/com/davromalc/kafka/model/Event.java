package com.davromalc.kafka.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Event implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3287109340691454065L;

	@Id
	private String id;
	
	private LocalDateTime date;
	
	private LocalDateTime insertDate;
	
	private String payload;
	
	private int partition;
	
}
