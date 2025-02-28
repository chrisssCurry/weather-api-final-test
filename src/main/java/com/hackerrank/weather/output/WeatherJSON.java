package com.hackerrank.weather.output;
import com.hackerrank.weather.model.WeatherInput;
import com.hackerrank.weather.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherJSON implements Serializable {
    private Integer id;

    @Schema(name = "date", description = "The date for weather", example = "2021-09-27")
    @Valid
    private String date;

    private Float lat;
    private Float lon;
    private String city;
    private String state;

    @Schema(description = "List of temperatures", required = true, example = "[24.0, 21.5, 24.0, 19.5, 25.5, 25.5, 24.0, 25.0, 23.0, 22.0, 18.0, 18.0, 23.5, 23.0, 23.0, 25.5, 21.0, 20.5, 20.0, 18.5, 20.5, 21.0, 25.0, 20.5]")
    private List<Double> temperatures;


    public static WeatherJSON from(WeatherInput weather) {
        return WeatherJSON.builder()
                .id(weather.getId())
                .lat(weather.getLat())
                .lon(weather.getLon())
                .city(weather.getCity())
                .state(weather.getState())
                .date(weather.getDate())
                .temperatures(weather.getTemperatures())
                .build();
    }

    public WeatherJSON(Integer id, LocalDate date, String city, Float lat, Float lon, String state) {
        this.id = id;
        this.city = city;
        this.lat = lat;
        this.lon = lon;
        this.state = state;
        this.date = DateUtil.format(date, "yyyy-MM-dd");
//        this.temperatures = temperatures.stream()
//                .map(temperature -> Double.valueOf(String.valueOf(temperature)))
//                .collect(Collectors.toList());
    }
}
