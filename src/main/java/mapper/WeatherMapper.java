package mapper;

import com.hackerrank.weather.entities.Weather;
import com.hackerrank.weather.model.WeatherInput;
import com.hackerrank.weather.output.WeatherJSON;
import com.hackerrank.weather.utils.DateUtil;

import java.math.BigDecimal;
import java.util.stream.Collectors;

public class WeatherMapper {

    public static Weather inputToEntity(WeatherInput weatherInput) {

        return Weather.builder()
                .date(DateUtil.stringToDate(weatherInput.getDate()))
                .city(weatherInput.getCity())
                .lat(weatherInput.getLat())
                .lon(weatherInput.getLon())
                .state(weatherInput.getState())
                .temperatures(
                        weatherInput.getTemperatures()
                                .stream()
                                .map(BigDecimal::valueOf)
                                .collect(Collectors.toList())
                )
                .build();
    }

    public static WeatherJSON entityToJSON(Weather weather) {

        return WeatherJSON.builder()
                .id(weather.getId())
                .date(DateUtil.format(weather.getDate(), "yyyy-MM-dd"))
                .city(weather.getCity())
                .lat(weather.getLat())
                .lon(weather.getLon())
                .state(weather.getState())
                .temperatures(
                        weather.getTemperatures()
                                .stream()
                                .map(temperature -> Double.valueOf(String.valueOf(temperature)))
                                .collect(Collectors.toList())
                )
                .build();
    }
}
