package com.hackerrank.weather.repository;

import com.hackerrank.weather.entities.Temperature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemperatureRepository extends JpaRepository<Temperature,Integer>{

    List<Temperature> findAllByWeather_Id(int id);
}
