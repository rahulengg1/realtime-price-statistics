package com.rahul.realtime.statistics.api.service;

import com.rahul.realtime.statistics.api.model.StatisticsResponse;
import com.rahul.realtime.statistics.api.model.TransactionRequest;

public interface StatisticsService {
	
	StatisticsResponse getAllStatistics();
	
	StatisticsResponse getInstrumentStatistics(String instrumentId);
	
	void insertUpdateStatistics(TransactionRequest request, Long currentTimestamp);

}
