package com.hackerrank.weather.repository;

import com.hackerrank.weather.model.WeatherSearchCriteria;
import com.hackerrank.weather.output.WeatherJSON;

import java.util.List;

public interface WeatherRepositoryCustom {
    List<WeatherJSON> searchWeather(WeatherSearchCriteria weatherSearchCriteria);
}
