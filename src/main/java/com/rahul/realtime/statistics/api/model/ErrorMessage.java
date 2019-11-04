package com.rahul.realtime.statistics.api.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data

public class ErrorMessage   {

	private Date timeStamp;
	private int status;
	private List<String> error;

}
