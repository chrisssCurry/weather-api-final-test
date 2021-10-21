package com.hackerrank.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hackerrank.weather.utils.Patterns;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeatherSearchCriteria {

    @Schema(example = "2021-09-20")
    @Pattern(regexp = Patterns.YYYY_MM_DD_REGEXP,
            message = "Date field must match the following pattern: 'yyy-mm-dd'")
    private String date;

    private String city;

    @Schema(example = "-date")
    @Pattern(regexp = Patterns.DATE_SORTING_ORDER_REGEXP,
    message = "Sorting order must have one of the following values: 'date/-date'")
    private String sort;

    @JsonIgnore
    public boolean isEmpty() {
        return  date == null
                && city == null
                && sort == null;
    }

}
