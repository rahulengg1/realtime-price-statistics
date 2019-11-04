package com.rahul.realtime.statistics;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.realtime.statistics.api.caching.StatisticsCacheObject;
import com.rahul.realtime.statistics.api.controller.StatisticsApiController;
import com.rahul.realtime.statistics.api.controller.TicksApiController;
import com.rahul.realtime.statistics.api.listeners.TransactionEventListener;
import com.rahul.realtime.statistics.api.model.TransactionRequest;
import com.rahul.realtime.statistics.api.service.impl.StatisticsServiceImpl;
import com.rahul.realtime.statistics.api.service.impl.TransactionServiceImpl;


@AutoConfigureMockMvc
@ContextConfiguration(classes = { StatisticsApiController.class, TicksApiController.class, StatisticsServiceImpl.class, TransactionServiceImpl.class, StatisticsCacheObject.class, TransactionEventListener.class})
@WebMvcTest
@TestMethodOrder(OrderAnnotation.class)
class RealtimePriceStatisticsApplicationTest {

	
	private MockMvc mockMvc;
	


	private static final String TRANSACTION_API_ENDPOINT = "/ticks";
	
	private static final String STATISTICS_API_ENDPOINT = "/statistics";
	
	private static final String INSTRUMENT_STATISTICS_API_ENDPOINT = "/statistics/{instrument_id}";

	


	@Autowired
	private ObjectMapper objectMapper;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	
	}

	@BeforeEach
	void setUp(@Autowired WebApplicationContext context) throws Exception {
		MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		
	}

	@Test
	@Order(1)  
	void whenValidInput_thenReturns201() throws Exception {
		TransactionRequest model = new TransactionRequest();
		model.setInstrument("IBM.N");
		model.setPrice(new Double("12.80"));
		model.setTimestamp(new Date().getTime());
		

		mockMvc.perform(post(TRANSACTION_API_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(model)))
				.andExpect(status().isCreated());
	}
	
	
	@Test
	@Order(2)  
	void whenValidInputTimeGreaterThan60_thenReturns204() throws Exception {
		TransactionRequest model = new TransactionRequest();
		model.setInstrument("IBM.N");
		model.setPrice(new Double("12.80"));
		
		LocalDateTime date = LocalDateTime.now().minusSeconds(65);		
		model.setTimestamp(date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		

		mockMvc.perform(post(TRANSACTION_API_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(model)))
				.andExpect(status().isNoContent());
	}

	@Test
	@Order(3)  
	void whenInvalidIntsrument_thenReturns400() throws Exception {
		TransactionRequest model = new TransactionRequest();
		model.setInstrument("");
		model.setPrice(new Double("12.80"));
		model.setTimestamp(new Date().getTime());
		
		mockMvc.perform(post(TRANSACTION_API_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(model)))
				.andExpect(status().isBadRequest());
	}

	

	@Test
	@Order(4)  
	void whenInvalidPrice_thenReturns400() throws Exception {
		
		TransactionRequest model = new TransactionRequest();
		model.setInstrument("IBM.N");
		model.setTimestamp(new Date().getTime());
		
		mockMvc.perform(post(TRANSACTION_API_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(model)))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isBadRequest());
	}

	@Test
	@Order(5)  
	void whenInvalidTimeStamp_thenReturns400() throws Exception {
		TransactionRequest model = new TransactionRequest();
		model.setInstrument("IBM.N");
		model.setPrice(new Double("12.80"));
		model.setTimestamp(null);

		mockMvc.perform(post(TRANSACTION_API_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(model)))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isBadRequest());
	}


	@Test
	@Order(6)  
	void whenValidAllStatistics_thenReturns200() throws Exception {
		
		
	mockMvc.perform(get(STATISTICS_API_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk());
	}
	
	@Test
	@Order(7)  
	void whenValidInstrumentStatistics_thenReturns200() throws Exception {
		
		
	mockMvc.perform(get(INSTRUMENT_STATISTICS_API_ENDPOINT,"TEST")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk());
	}
	
	@Test
	@Order(8)  
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
	void testStatisticsData_isOk() throws Exception {
		TransactionRequest model = new TransactionRequest();
		model.setInstrument("IBM.N");
		model.setPrice(new Double("12.80"));
		model.setTimestamp(new Date().getTime());

		
		mockMvc.perform(post(TRANSACTION_API_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
			    .accept(MediaType.APPLICATION_JSON_VALUE)
		        .content(objectMapper.writeValueAsString(model)))
				.andDo(MockMvcResultHandlers.print())
		        .andExpect(status().isCreated());
		
		mockMvc.perform(get(STATISTICS_API_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
			    .accept(MediaType.APPLICATION_JSON_VALUE))
		        .andDo(MockMvcResultHandlers.print())
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("avg").value(12.80))
		        .andExpect(jsonPath("max").value(12.80))
		        .andExpect(jsonPath("min").value(12.80))
		        .andExpect(jsonPath("count").value(1));
		
		mockMvc.perform(get(INSTRUMENT_STATISTICS_API_ENDPOINT,"IBM.N")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
			    .accept(MediaType.APPLICATION_JSON_VALUE))
		        .andDo(MockMvcResultHandlers.print())
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("avg").value(12.80))
		        .andExpect(jsonPath("max").value(12.80))
		        .andExpect(jsonPath("min").value(12.80))
		        .andExpect(jsonPath("count").value(1));
		
		
		model.setInstrument("IBM.N");
		model.setPrice(new Double("10.00"));
		model.setTimestamp(new Date().getTime());
				
		mockMvc.perform(post(TRANSACTION_API_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
			    .accept(MediaType.APPLICATION_JSON_VALUE)
		        .content(objectMapper.writeValueAsString(model)))
				.andDo(MockMvcResultHandlers.print())
		        .andExpect(status().isCreated());
		
		mockMvc.perform(get(STATISTICS_API_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
			    .accept(MediaType.APPLICATION_JSON_VALUE))
		        .andDo(MockMvcResultHandlers.print())
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("avg").value(11.40))
		        .andExpect(jsonPath("max").value(12.80))
		        .andExpect(jsonPath("min").value(10.00))
		        .andExpect(jsonPath("count").value(2));
		
		mockMvc.perform(get(INSTRUMENT_STATISTICS_API_ENDPOINT,"IBM.N")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
			    .accept(MediaType.APPLICATION_JSON_VALUE))
		        .andDo(MockMvcResultHandlers.print())
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("avg").value(11.40))
		        .andExpect(jsonPath("max").value(12.80))
		        .andExpect(jsonPath("min").value(10.00))
		        .andExpect(jsonPath("count").value(2));
		
		
		model.setInstrument("HP.X");
		model.setPrice(new Double("15.00"));
		model.setTimestamp(new Date().getTime());
				
		mockMvc.perform(post(TRANSACTION_API_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
			    .accept(MediaType.APPLICATION_JSON_VALUE)
		        .content(objectMapper.writeValueAsString(model)))
				.andDo(MockMvcResultHandlers.print())
		        .andExpect(status().isCreated());
		
		mockMvc.perform(get(STATISTICS_API_ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
			    .accept(MediaType.APPLICATION_JSON_VALUE))
		        .andDo(MockMvcResultHandlers.print())
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("avg").value(12.60))
		        .andExpect(jsonPath("max").value(15.00))
		        .andExpect(jsonPath("min").value(10.00))
		        .andExpect(jsonPath("count").value(3));
		
		mockMvc.perform(get(INSTRUMENT_STATISTICS_API_ENDPOINT,"IBM.N")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
			    .accept(MediaType.APPLICATION_JSON_VALUE))
		        .andDo(MockMvcResultHandlers.print())
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("avg").value(11.40))
		        .andExpect(jsonPath("max").value(12.80))
		        .andExpect(jsonPath("min").value(10.00))
		        .andExpect(jsonPath("count").value(2));
		
		mockMvc.perform(get(INSTRUMENT_STATISTICS_API_ENDPOINT,"HP.X")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
			    .accept(MediaType.APPLICATION_JSON_VALUE))
		        .andDo(MockMvcResultHandlers.print())
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("avg").value(15.00))
		        .andExpect(jsonPath("max").value(15.00))
		        .andExpect(jsonPath("min").value(15.00))
		        .andExpect(jsonPath("count").value(1));
		
	}
	
}
