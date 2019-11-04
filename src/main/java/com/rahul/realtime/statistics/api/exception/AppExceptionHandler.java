package com.rahul.realtime.statistics.api.exception;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.rahul.realtime.statistics.api.model.ErrorMessage;




/**
 * @author Rahul
 *
 */
@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(value = {Exception.class})
	public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request)
	{
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex);
	}

	 @Override
	 protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
	                                                                  HttpHeaders headers,
	                                                                  HttpStatus status, WebRequest request) {

	        ErrorMessage error= new ErrorMessage();
	        error.setTimeStamp(new Date());
	        error.setStatus(status.value());

	        //Get all errors
	        List<String> errors = ex.getBindingResult().getAllErrors().stream()
	                .map(loop -> loop.getDefaultMessage())
	                .collect(Collectors.toList());

	        error.setError(errors);

	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(error);

	    }
	
}
