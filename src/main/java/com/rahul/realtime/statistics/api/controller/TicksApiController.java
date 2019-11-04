package com.rahul.realtime.statistics.api.controller;


import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rahul.realtime.statistics.api.model.TransactionRequest;
import com.rahul.realtime.statistics.api.service.TransactionService;

import io.swagger.annotations.Api;






@Api(tags = "Transaction")
@RestController
@RequestMapping(value = "ticks")
public class TicksApiController{

	
	private final TransactionService transactionService;

	
	public TicksApiController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}
   
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE
            ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addTick(@Valid @RequestBody TransactionRequest body) {
       
		HttpStatus httpStatus=transactionService.addRecord(body)?HttpStatus.CREATED:HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(httpStatus);
    }

}
