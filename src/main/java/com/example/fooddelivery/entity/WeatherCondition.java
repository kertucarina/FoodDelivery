package com.example.fooddelivery.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "weatherconditions")
public class WeatherCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String station;
    private String city;
    private int WMO;
    private double temperature;
    private double windSpeed;
    private String phenomenon;
    private LocalDateTime timestamp;

    public Long getId() {
        return id;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getWMO() {
        return WMO;
    }

    public void setWMO(int WMO) {
        this.WMO = WMO;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getPhenomenon() {
        return phenomenon;
    }

    public void setPhenomenon(String phenomenon) {
        this.phenomenon = phenomenon;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "WeatherCondition{" +
                "id=" + id +
                ", station='" + station + '\'' +
                ", city='" + city + '\'' +
                ", WMO=" + WMO +
                ", temperature=" + temperature +
                ", windSpeed=" + windSpeed +
                ", phenomenon='" + phenomenon + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
