package com.rahul.realtime.statistics.api.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.rahul.realtime.statistics.api.model.TransactionRequest;


@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {
	
	
	@InjectMocks
	TransactionServiceImpl transactionServiceImplTest;
	
	@Mock
	ApplicationEventPublisher applicationEventPublisher;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		
	}

	@Test
	void testAddRecord() {

		TransactionRequest model = new TransactionRequest();
		LocalDateTime date = LocalDateTime.now().minusSeconds(65);	
		
		model.setInstrument("IBM.N");
		model.setPrice(new Double("12.80"));
		model.setTimestamp(date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		
		
		assertEquals(transactionServiceImplTest.addRecord(model),false);
		
		model.setInstrument("IBM.N");
		model.setPrice(new Double("12.80"));
		model.setTimestamp(new Date().getTime());
		
		assertEquals(transactionServiceImplTest.addRecord(model),true);
		
		
		

	}

}
