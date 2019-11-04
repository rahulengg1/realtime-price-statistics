# RealTime-Price-Statistics
API is to provide real-time price statistics from the last 60 seconds 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

> Prerequisite

* Maven
* Java 8 or higher

> Steps

1. Clone the repository.
2. Go inside the project directory using command line.
3. Install mvn and execute mvn clean spring-boot:run. Application port is 8080, you can change in application.yml file.
4. Context path for the application is /api/v1 which you can also change in application.yml file.
5. Test the endpoints using swagger http://localhost:8080/api/v1/swagger-ui.html#/

  * http://localhost:8080/api/v1/ticks 
  * http://localhost:8080/api/v1/statistics
  * http://localhost:8080/api/v1/statistics/{instrument_identifier}
  
  or you can use any REST API test tools such as Postman or SoapUI.
  
6). API payload as well as response is given below.  
  

## Task Description

There will be three APIs:

* The first one is called every time we receive a tick. It is also the sole input of this rest API.
* The second one returns the statistics based on the ticks of all instruments of the last 60 seconds (sliding time interval)
* The third one returns the statistics based on the ticks of one instrument of the last 60 seconds (sliding time interval).

#### POST /ticks

Every time a new tick arrives, this endpoint will be called. Body:

```
{
"instrument": "IBM.N",
"price": 143.82,
"timestamp": 1478192204000
}
```

where:

* instrument - a financial instrument identifier (string; list of instruments is not known to our service in advance so we add them dynamically)
* price - current trade price of a financial instrument (double)
* timestamp - tick timestamp in milliseconds (long; this is not current timestamp) Returns: Empty body with either 201 or 204:
* 201 - in case of success
* 204 - if tick is older than 60 seconds

#### GET /statistics

This is the endpoint with aggregated statistics for all ticks across all instruments, this endpoint has to execute in constant time and memory (O(1)).
It returns the following statistics based on the ticks which happened in the last 60 seconds (sliding time interval).

```
Returns:
{
"avg": 100,
"max": 200,
"min": 50,
"count": 10
}
```

where:
* avg is a double specifying the average amount of all tick prices in the last 60 seconds
* max is a double specifying single highest tick price in the last 60 seconds
* min is a double specifying single lowest tick price in the last 60 seconds
* count is a long specifying the total number of ticks happened in the last 60 seconds


#### GET /statistics/{instrument_identifier}

This is the endpoint with statistics for a given instrument.
It returns the statistic based on the ticks with a given instrument identifier happened in the last 60 seconds
(sliding time interval). The response is the same as for the previous endpoint but with instrument specific
statistics.

## Development and Architecture Aspects

> Technology used

* Java 8
* Spring boot
* Maven 
* Swagger for documentation (yaml file included for reference)

> Constant time and memory (O(1)) complexity

* To implement in-memory solution as well to achieve O(1) time and memory complexity two Map object one for all statistics result and other one for Instrument wise statistics along with timestamp. Combining already calculated statistics to provide statistics in O(1) complexity. <br /><br />
*Previously inserted entry is outdated then it will be overwritten by new incoming one(s).*

> Following Development principles are used

<I>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Clean Code, KISS, DRY, SOLID, Event Driven and Oops principle.</I>

## Test Cases

Unit Test cases and Integration test cases were written. Please use below command to run test cases


```
mvn clean test
```

Find the test coverage report under project working directory - **target/site/jacoco/index.html** 


## Comments

I Like the challenge :relaxed: and want to improve test coverage as now it is 42%. 
