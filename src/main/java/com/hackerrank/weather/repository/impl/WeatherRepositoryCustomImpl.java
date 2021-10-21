package com.hackerrank.weather.repository.impl;

import com.hackerrank.weather.entities.Temperature;
import com.hackerrank.weather.entities.Weather;
import com.hackerrank.weather.model.WeatherSearchCriteria;
import com.hackerrank.weather.output.WeatherJSON;
import com.hackerrank.weather.repository.WeatherRepositoryCustom;
import com.hackerrank.weather.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeatherRepositoryCustomImpl implements WeatherRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<WeatherJSON> searchWeather(WeatherSearchCriteria weatherSearchCriteria) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<WeatherJSON> criteriaQuery = criteriaBuilder.createQuery(WeatherJSON.class);
        final Root<Weather> weatherRoot = criteriaQuery.from(Weather.class);


        criteriaQuery.select(criteriaBuilder.construct(
                        WeatherJSON.class,
                        weatherRoot.get("id"),
                        weatherRoot.get("date"),
                        weatherRoot.get("city"),
                        weatherRoot.get("lat"),
                        weatherRoot.get("lon"),
                        weatherRoot.get("state")
                        //generateSubqueryForTemperatures(criteriaBuilder, criteriaQuery, weatherRoot).getSelection()
        ));//SELECT FROM



        List<Predicate> predicates = getFilterPredicates(weatherSearchCriteria, criteriaBuilder, weatherRoot);
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[]{}));//WHERE
        }
        criteriaQuery.orderBy(getOrder(weatherSearchCriteria, criteriaBuilder, weatherRoot), criteriaBuilder.asc(weatherRoot.get("id"))); //ORDER BY

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    private Subquery<Double> generateSubqueryForTemperatures(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery, Root<Weather> weatherRoot) {

        Subquery<Double> subquery = criteriaQuery.subquery(Double.class);

        final Root<Temperature> temperatureRoot = subquery.from(Temperature.class);

        Predicate idsEqual = criteriaBuilder.equal(weatherRoot.get("id"), temperatureRoot.get("weather").get("id"));

        //subquery.select(temperatureRoot.get("temperature")).where(idsEqual);

        Join<Temperature, Weather> temperatureWeatherJoin = temperatureRoot.join("weather", JoinType.INNER);
        temperatureWeatherJoin.on(criteriaBuilder.equal(temperatureRoot.get("weather").get("id"), weatherRoot.get("id")));

        subquery.select(temperatureWeatherJoin.get("temperatures"));

        return subquery;
    }

    private Order getOrder(WeatherSearchCriteria weatherSearchCriteria, CriteriaBuilder builder, Root<Weather> weatherRoot){
        if (StringUtils.isNotEmpty(weatherSearchCriteria.getSort())) {
            if ("-date".equalsIgnoreCase(weatherSearchCriteria.getSort()))
                return builder.desc(weatherRoot.get("date"));
            else if ("date".equalsIgnoreCase(weatherSearchCriteria.getSort()))
                return builder.asc(weatherRoot.get("date"));
        }
        return builder.asc(weatherRoot.get("date"));
    }

    private List<Predicate> getFilterPredicates(WeatherSearchCriteria searchCriteria, CriteriaBuilder builder, Root<Weather> weatherRoot) {

        Predicate dateCondition = null;
        Predicate cityCondition = null;

        //Search by date
        if(searchCriteria.getDate() != null) {
            dateCondition = builder.equal(weatherRoot.get("date"), DateUtil.stringToDate(searchCriteria.getDate()));
        }

        //Search by city
        if (searchCriteria.getCity() != null) {
            List<String> cities = Arrays.asList(searchCriteria.getCity().toLowerCase().split(","));
            Expression<String> citiesExpression = builder.lower(weatherRoot.get("city"));

            cityCondition = citiesExpression.in(cities);
        }

        return Stream.of(dateCondition, cityCondition)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
