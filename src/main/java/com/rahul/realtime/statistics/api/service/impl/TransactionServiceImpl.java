package com.rahul.realtime.statistics.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.rahul.realtime.statistics.api.model.TransactionRequest;
import com.rahul.realtime.statistics.api.service.TransactionService;
import com.rahul.realtime.statistics.api.shared.AppConstant;

@Service
public class TransactionServiceImpl implements TransactionService{

	
	 private final ApplicationEventPublisher applicationEventPublisher;

	    @Autowired
	    public TransactionServiceImpl(ApplicationEventPublisher applicationEventPublisher) {
	        this.applicationEventPublisher = applicationEventPublisher;
	    }

	    @Override
	    public boolean addRecord(TransactionRequest request) {
       
	        if (isTransactionTimestampUnderInterestInterval(request.getTimestamp())) {
	            applicationEventPublisher.publishEvent(request);
	            return Boolean.TRUE;
	        } else {
	             return Boolean.FALSE;
	        }
	    }

	    private Boolean isTransactionTimestampUnderInterestInterval(Long timestamp) {
	        return timestamp > (System.currentTimeMillis() - AppConstant.INTERVAL_IN_MILLISECONDS);
	    }

	

}
