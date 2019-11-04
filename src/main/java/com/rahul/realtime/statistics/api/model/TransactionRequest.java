package com.rahul.realtime.statistics.api.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionRequest   {
	
  @NotNull(message ="Price cannot be blank")
  private Double price;
 
  @NotNull(message ="Financial instrument identifier cannot be blank")
  @NotEmpty(message ="Financial instrument identifier cannot be blank")
  private String instrument;
 
  @NotNull(message ="Timestamp cannot be blank")
  private Long timestamp;

 

}
