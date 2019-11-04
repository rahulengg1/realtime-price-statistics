package com.rahul.realtime.statistics.api.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.rahul.realtime.statistics.api.caching.StatisticsCacheObject;
import com.rahul.realtime.statistics.api.model.StatisticsResponse;
import com.rahul.realtime.statistics.api.model.TransactionRequest;

@TestMethodOrder(OrderAnnotation.class)
class StatisticsServiceImplTest {

	private StatisticsServiceImpl serviceImpl;

	private StatisticsCacheObject cacheObject;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

	}

	@BeforeEach
	void setUp() throws Exception {
		cacheObject = new StatisticsCacheObject();
		serviceImpl = new StatisticsServiceImpl(cacheObject);
	}

	@Test
	@Order(1)
	void testGetAllStatistics() {

		StatisticsResponse expectedResponse = new StatisticsResponse();
		expectedResponse.setAvg(12.80);
		expectedResponse.setCount(1L);
		expectedResponse.setMax(12.80);
		expectedResponse.setMin(12.80);

		TransactionRequest model = new TransactionRequest();
		model.setInstrument("IBM.N");
		model.setPrice(new Double("12.80"));
		model.setTimestamp(new Date().getTime());

		serviceImpl.insertUpdateStatistics(model, System.currentTimeMillis());

		StatisticsResponse actualResponse = serviceImpl.getAllStatistics();
		assertEquals(expectedResponse, actualResponse);

	}

	
	@Test
	@Order(2)
	void testGetInstrumentStatistics() {
		StatisticsResponse expectedResponse = new StatisticsResponse();
		expectedResponse.setAvg(10.00);
		expectedResponse.setCount(1L);
		expectedResponse.setMax(10.00);
		expectedResponse.setMin(10.00);

		TransactionRequest model = new TransactionRequest();
		model.setInstrument("IBM.N");
		model.setPrice(new Double("10.00"));
		model.setTimestamp(new Date().getTime());

		serviceImpl.insertUpdateStatistics(model, System.currentTimeMillis());
		StatisticsResponse actualResponse = serviceImpl.getInstrumentStatistics("IBM.N");
		assertEquals(expectedResponse, actualResponse);
	}

	

}
