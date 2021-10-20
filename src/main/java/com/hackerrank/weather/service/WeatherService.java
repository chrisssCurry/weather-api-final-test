package com.hackerrank.weather.service;

import com.hackerrank.weather.entities.Weather;
import com.hackerrank.weather.model.WeatherInput;
import com.hackerrank.weather.output.WeatherJSON;
import com.hackerrank.weather.repository.WeatherRepository;
import mapper.WeatherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }


    public ResponseEntity<WeatherJSON> submitWeather(WeatherInput weatherInput) {
        final Weather weather = WeatherMapper.inputToEntity(weatherInput);
        weatherRepository.save(weather);
        
        return new ResponseEntity<WeatherJSON>(WeatherMapper.entityToJSON(weather), HttpStatus.CREATED);
    }
}
