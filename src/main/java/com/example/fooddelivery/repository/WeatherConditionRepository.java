package com.example.fooddelivery.repository;

import com.example.fooddelivery.entity.WeatherCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherConditionRepository extends JpaRepository<WeatherCondition, Long> {

    /**
     * This method retrieves the most recent weather observation in the given city from the database.
     * @param city location of the observation.
     * @return WeatherCondition object that contains data regarding the weather conditions in the given city.
     */
    WeatherCondition findTopByCityOrderByTimestampDesc(String city);

}
