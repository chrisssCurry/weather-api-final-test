package com.hackerrank.weather.repository;

import com.hackerrank.weather.entities.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<Weather,Integer>, WeatherRepositoryCustom {
    Optional<Weather> findById(Integer id);

}
