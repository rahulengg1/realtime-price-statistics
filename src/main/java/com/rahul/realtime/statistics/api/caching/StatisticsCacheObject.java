package com.rahul.realtime.statistics.api.caching;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.rahul.realtime.statistics.api.shared.StatisticsDto;

import lombok.Getter;
@Component
public class StatisticsCacheObject {
	@Getter
    private final Map<String, Map<Integer, StatisticsDto>> instrumentStatisticData = new ConcurrentHashMap<>();
	
	@Getter
    private final Map<Integer, StatisticsDto> allStatisticsData = new ConcurrentHashMap<>();

}
