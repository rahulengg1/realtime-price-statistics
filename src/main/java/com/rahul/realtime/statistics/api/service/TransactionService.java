package com.rahul.realtime.statistics.api.service;

import com.rahul.realtime.statistics.api.model.TransactionRequest;

public interface TransactionService {
	
	boolean addRecord(TransactionRequest request);

}
