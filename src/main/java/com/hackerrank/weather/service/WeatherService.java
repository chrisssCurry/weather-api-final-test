package com.hackerrank.weather.service;

import com.hackerrank.weather.entities.Temperature;
import com.hackerrank.weather.entities.Weather;
import com.hackerrank.weather.model.WeatherInput;
import com.hackerrank.weather.model.WeatherSearchCriteria;
import com.hackerrank.weather.output.WeatherJSON;
import com.hackerrank.weather.repository.TemperatureRepository;
import com.hackerrank.weather.repository.WeatherRepository;
import mapper.WeatherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final TemperatureRepository temperatureRepository;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository, TemperatureRepository temperatureRepository) {
        this.weatherRepository = weatherRepository;
        this.temperatureRepository = temperatureRepository;
    }


    public ResponseEntity<WeatherJSON> submitWeather(WeatherInput weatherInput) {
        final Weather weather = WeatherMapper.inputToEntity(weatherInput);
        weatherRepository.save(weather);

        return new ResponseEntity<WeatherJSON>(WeatherMapper.entityToJSON(weather), HttpStatus.CREATED);
    }

    public ResponseEntity<WeatherJSON> getWeatherById(Integer id) {
        Weather weather = weatherRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            return ResponseEntity.ok(WeatherMapper.entityToJSON(weather));
    }

    public List<WeatherJSON> searchWeather(WeatherSearchCriteria weatherSearchCriteria) {
        List<WeatherJSON> weatherList = new ArrayList<>();
        List<WeatherJSON> finalWeatherList = new ArrayList<>();

        if(weatherSearchCriteria.isEmpty()) {
            weatherList = weatherRepository.findAll().stream()
                    .sorted(Comparator.comparingInt(Weather::getId))
                    .map(WeatherMapper::entityToJSON)
                    .collect(Collectors.toList());
            return weatherList;
        } else {
            if (weatherSearchCriteria.getSort() != null) {
                weatherList = weatherRepository.searchWeather(weatherSearchCriteria);
                finalWeatherList = setTemperaturesList(weatherList, true);

                return finalWeatherList;
            }
            weatherList = weatherRepository.searchWeather(weatherSearchCriteria);
            finalWeatherList = setTemperaturesList(weatherList, false);
        }
        return finalWeatherList;
    }

    private List<WeatherJSON> setTemperaturesList(List<WeatherJSON> weatherList, boolean sorted) {
        List<WeatherJSON> finalWeatherList = new ArrayList<>();

        if (sorted) {
            finalWeatherList = weatherList.stream().peek(this::setTemperatures).collect(Collectors.toList());
        } else {
            finalWeatherList = weatherList.stream().sorted(Comparator.comparingInt(WeatherJSON::getId)).peek(this::setTemperatures).collect(Collectors.toList());
        }
        return finalWeatherList;
    }

    private void setTemperatures(WeatherJSON weatherJSON) {
        List<Temperature> temperatures = temperatureRepository.findAllByWeather_Id(weatherJSON.getId());
        List<Double> temperaturesDouble = temperatures.stream().map((Temperature::getTemperature)).collect(Collectors.toList());
        weatherJSON.setTemperatures(temperaturesDouble);
    }
}
