package com.hackerrank.weather.entities;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "temperature")
@Table(name = "temperatures")
public class Temperature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @Column(name = "temperature")
    private Double temperature;

    @ManyToOne
    @JoinColumn(name = "weather_id", referencedColumnName = "id", nullable = false)
    private Weather weather;
}
