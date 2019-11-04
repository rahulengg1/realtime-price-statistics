package com.rahul.realtime.statistics.api.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rahul.realtime.statistics.api.model.StatisticsResponse;
import com.rahul.realtime.statistics.api.service.StatisticsService;

import io.swagger.annotations.Api;


@Api(tags = "Statistics")
@RestController
@RequestMapping(value = "/statistics")
public class StatisticsApiController {

	private final StatisticsService statisticsService;


	public StatisticsApiController(StatisticsService statisticsService) {	
		this.statisticsService = statisticsService;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StatisticsResponse> getAllStatistics() {
		StatisticsResponse response = statisticsService.getAllStatistics();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(value = "/{instrument_identifier}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StatisticsResponse> getStatisticsByIntrument(@Valid @PathVariable("instrument_identifier") String instrumentId) {
		return ResponseEntity.status(HttpStatus.OK).body(statisticsService.getInstrumentStatistics(instrumentId));
	}

}
