package com.codenotfound.kafka.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class Dto {

	private Integer id;
	
	private String data;
	
	private int partition;
	
	private long timestamp;

	@JsonCreator
	public Dto(@JsonProperty("id") Integer id,@JsonProperty("data") String data) {
		this.id = id;
		this.data = data;
	}
	
	private Dto(Integer id, String data, int partition, long timestamp) {
		super();
		this.id = id;
		this.data = data;
		this.partition = partition;
		this.timestamp = timestamp;
	}
	
	public static Builder builder(){
		return new Builder();
	}



	public static class Builder {
		
		private Integer id;
		
		private String data;
		
		private int partition;
		
		private long timestamp;
		
		public Builder id(Integer id){
			this.id = id;
			return this;
		}
		
		public Builder data(String data){
			this.data = data;
			return this;
		}
		
		public Builder partition(int partition){
			this.partition = partition;
			return this;
		}
		
		public Builder timestamp(long timestamp){
			this.timestamp = timestamp;
			return this;
		}
		
		public Dto build(){
			return new Dto(id, data, partition, timestamp);
		}
	}
	
}
