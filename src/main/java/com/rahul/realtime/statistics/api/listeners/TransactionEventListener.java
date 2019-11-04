package com.rahul.realtime.statistics.api.listeners;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.rahul.realtime.statistics.api.model.TransactionRequest;
import com.rahul.realtime.statistics.api.service.StatisticsService;

@Component
public class TransactionEventListener {
	

    private final StatisticsService statisticsService;

    @Autowired
    public TransactionEventListener(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Async
    @EventListener
    public void handleEvent(TransactionRequest request) {
        statisticsService.insertUpdateStatistics(request, System.currentTimeMillis());
    }

}
