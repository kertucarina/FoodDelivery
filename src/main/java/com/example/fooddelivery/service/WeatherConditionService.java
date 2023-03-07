package com.example.fooddelivery.service;

import com.example.fooddelivery.entity.WeatherCondition;
import com.example.fooddelivery.repository.WeatherConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class WeatherConditionService {

    @Autowired
    private WeatherConditionRepository weatherConditionRepository;

    public List<WeatherCondition> findAll() {
        return weatherConditionRepository.findAll();
    }

    public WeatherCondition findCurrentWeatherByCity(String city) {
        return weatherConditionRepository.findTopByCityOrderByTimestampDesc(city);
    }

    public WeatherCondition findById(Long id) {
        Optional<WeatherCondition> weatherCondition = weatherConditionRepository.findById(id);
        return weatherCondition.orElse(null);
    }

    public WeatherCondition save(WeatherCondition weatherCondition) {
        return weatherConditionRepository.save(weatherCondition);
    }

    public Boolean delete(Long id) {
        try {
            weatherConditionRepository.deleteById(id);
            return Boolean.TRUE;
        } catch (final Exception e) {
            return Boolean.FALSE;
        }
    }
}
