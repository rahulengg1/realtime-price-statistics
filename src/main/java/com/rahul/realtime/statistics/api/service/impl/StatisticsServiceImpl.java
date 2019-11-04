package com.rahul.realtime.statistics.api.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.rahul.realtime.statistics.api.caching.StatisticsCacheObject;
import com.rahul.realtime.statistics.api.model.StatisticsResponse;
import com.rahul.realtime.statistics.api.model.TransactionRequest;
import com.rahul.realtime.statistics.api.service.StatisticsService;
import com.rahul.realtime.statistics.api.shared.AppConstant;
import com.rahul.realtime.statistics.api.shared.StatisticsDto;
import com.rahul.realtime.statistics.api.shared.StatisticsResponseMapper;

@Service
public class StatisticsServiceImpl implements StatisticsService {

	private final StatisticsCacheObject cachedObject;
	
	public StatisticsServiceImpl(StatisticsCacheObject cachedObject) {
		this.cachedObject = cachedObject;
	}

	@Override
	public StatisticsResponse getAllStatistics() {
		long currentTime = System.currentTimeMillis();
				
		StatisticsResponseMapper responseMapper = cachedObject.getAllStatisticsData().values().stream()
				.filter(s -> (currentTime - s.getTimestamp()) / 1000 < AppConstant.INTERVAL_IN_SECONDS)
				.map(StatisticsResponseMapper::new).reduce(new StatisticsResponseMapper(), (s1, s2) -> {
					s1.setSum(s1.getSum() + s2.getSum());
					s1.setCount(s1.getCount() + s2.getCount());
					s1.setMax(Double.compare(s1.getMax(), s2.getMax()) > 0 ? s1.getMax() : s2.getMax());
					s1.setMin(Double.compare(s1.getMin(), s2.getMin()) < 0 ? s1.getMin() : s2.getMin());
					return s1;
				});
		updateStatisticsData(responseMapper);
		ModelMapper modelMapper = new ModelMapper();
		StatisticsResponse response = modelMapper.map(responseMapper, StatisticsResponse.class);
		return response;
	}

	@Override
	public StatisticsResponse getInstrumentStatistics(String instrumentId) {
		long currentTime = System.currentTimeMillis();
	
		StatisticsResponseMapper responseMapper = cachedObject.getInstrumentStatisticData().getOrDefault(instrumentId, new ConcurrentHashMap<Integer, StatisticsDto>())
				.values()
				.stream()
				.filter(s -> (currentTime - s.getTimestamp()) / 1000 < AppConstant.INTERVAL_IN_SECONDS)
				.map(StatisticsResponseMapper::new).reduce(new StatisticsResponseMapper(), (s1, s2) -> {
					s1.setSum(s1.getSum() + s2.getSum());
					s1.setCount(s1.getCount() + s2.getCount());
					s1.setMax(Double.compare(s1.getMax(), s2.getMax()) > 0 ? s1.getMax() : s2.getMax());
					s1.setMin(Double.compare(s1.getMin(), s2.getMin()) < 0 ? s1.getMin() : s2.getMin());
					return s1;
				});
		
		
		updateStatisticsData(responseMapper);
		ModelMapper modelMapper = new ModelMapper();
		StatisticsResponse response = modelMapper.map(responseMapper, StatisticsResponse.class);
		return response;
	}

	@Override
	public void insertUpdateStatistics(TransactionRequest request, Long currentTimestamp) {

		int second = LocalDateTime.ofInstant(Instant.ofEpochMilli(request.getTimestamp()), ZoneId.systemDefault())
				.getSecond();

		cachedObject.getAllStatisticsData().compute(second, (key, existingStatistic) -> {
			if (Objects.isNull(existingStatistic)
					|| isStatisticsUnderInterval(currentTimestamp, existingStatistic)) {
				return createStatistics(request);
			}
			return updateStatistics(request, existingStatistic);
		});
		
		
			
		cachedObject.getInstrumentStatisticData().
					compute(request.getInstrument(),(key, existingStatistic) -> {
						if(Objects.isNull(existingStatistic))
							{ return new ConcurrentHashMap<>();}
							return existingStatistic;
						})
					.compute(second, (key, existingStatistic) -> {
							if (Objects.isNull(existingStatistic)
									|| isStatisticsUnderInterval(currentTimestamp, existingStatistic)) {
								return createStatistics(request);
							}
							return updateStatistics(request, existingStatistic);
					});
		
		
	}

	private boolean isStatisticsUnderInterval(Long currentTimestamp, StatisticsDto existingStatistic) {
		return (currentTimestamp - existingStatistic.getTimestamp()) / 1000 >= AppConstant.INTERVAL_IN_SECONDS;
	}

	private StatisticsDto updateStatistics(TransactionRequest request, StatisticsDto dto) {
		dto.setCount(dto.getCount() + 1);
		dto.setSum(dto.getSum() + request.getPrice());
		if (Double.compare(request.getPrice(), dto.getMax()) > 0) {
			dto.setMax(request.getPrice());
		}
		if (Double.compare(request.getPrice(), dto.getMin()) < 0) {
			dto.setMin(request.getPrice());
		}
		return dto;
	}

	private StatisticsDto createStatistics(TransactionRequest request) {
		StatisticsDto statistics = new StatisticsDto();
		statistics.setTimestamp(request.getTimestamp());
		statistics.setSum(request.getPrice());
		statistics.setMax(request.getPrice());
		statistics.setMin(request.getPrice());
		statistics.setCount(1L);
		return statistics;
	}

	private void updateStatisticsData(StatisticsResponseMapper responseObject) {
		responseObject.setAvg(calculateAverage(responseObject));
		responseObject.setMin(
				Double.compare(responseObject.getMin(), Double.MAX_VALUE) == 0 ? 0.00 : responseObject.getMin());
		responseObject.setMax(
				Double.compare(responseObject.getMax(), Double.MIN_VALUE) == 0 ? 0.00 : responseObject.getMax());
	}

	private Double calculateAverage(StatisticsResponseMapper responseObject) {
		Double average = responseObject.getCount() > 0L ? (responseObject.getSum() / responseObject.getCount()) : 0.00;
		return Double.valueOf(AppConstant.DECIMAL_FORMAT.format(average));
	}

}
