package com.rahul.realtime.statistics.api.shared;


import lombok.Data;

@Data
public class StatisticsDto {

	
	  private Double sum;

	  private Double max;

	  private Double min;

	  private Double avg;
	  
	  private Long count;
	  
	  private Long timestamp;
}
