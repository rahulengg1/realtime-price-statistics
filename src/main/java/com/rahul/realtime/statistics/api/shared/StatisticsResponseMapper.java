package com.rahul.realtime.statistics.api.shared;

import lombok.Data;

@Data
public class StatisticsResponseMapper   {
 
 
  private Double sum;
	
  private Double avg;

  private Double max;

  private Double min;

  private Long count;
  

  
  
  public StatisticsResponseMapper(StatisticsDto dto) {
      this.sum = dto.getSum();
      this.max = dto.getMax();
      this.min = dto.getMin();
      this.count = dto.getCount();
  }

  public StatisticsResponseMapper() {
      this.sum = 0.00;
      this.max = Double.MIN_VALUE;
      this.min = Double.MAX_VALUE;
      this.count = 0L;
      this.avg = 0.00;
  }
}
