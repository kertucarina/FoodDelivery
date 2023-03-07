package com.example.fooddelivery.controller;

import com.example.fooddelivery.entity.WeatherCondition;
import com.example.fooddelivery.service.WeatherConditionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class with endpoints for GET, POST and DELETE methods.
 */
@Controller
@AllArgsConstructor
@RequestMapping(value = "/weatherconditions", consumes = MediaType.APPLICATION_JSON_VALUE)
public class WeatherConditionController {

    @Autowired
    private WeatherConditionService weatherConditionService;

    /**
     * GET /weatherconditions/{id}
     *
     * @param id The ID of the WeatherCondition to retrieve.
     * @return A specific WeatherCondition by ID.
     */
    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<WeatherCondition> get(@PathVariable("id") Long id) {
        return ResponseEntity.ok(weatherConditionService.findById(id));
    }

    /**
     * GET /weatherconditions/{city}
     *
     * @param city The location of the retrievable WeatherCondition.
     * @return The latest weather conditions in the city as a WeatherCondition object.
     */
    @GetMapping(value = "getCurrentWeather/{city}")
    public @ResponseBody ResponseEntity<WeatherCondition> getCurrentWeather(@PathVariable("city") String city) {
        return ResponseEntity.ok(weatherConditionService.findCurrentWeatherByCity(city));
    }

    /**
     * GET /weatherconditions
     *
     * @return All weather conditions in the database.
     */
    @GetMapping
    public @ResponseBody ResponseEntity<List<WeatherCondition>> getAll() {
        return ResponseEntity.ok(weatherConditionService.findAll());
    }

    /**
     * POST /weatherconditions/{id}
     *
     * @param weatherCondition The WeatherCondition object to be inserted into the database.
     * @return The inserted WeatherCondition object.
     */
    @PostMapping("/{id}")
    public @ResponseBody ResponseEntity<WeatherCondition> post(@RequestBody WeatherCondition weatherCondition) {
        return ResponseEntity.ok(weatherConditionService.save(weatherCondition));
    }

    /**
     * DELETE /weatherconditions/{id}
     *
     * @param id The ID of the WeatherCondition object to be deleted.
     * @return Boolean according to whether the object was deleted or not.
     */
    @DeleteMapping("/{id}")
    public @ResponseBody ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok(weatherConditionService.delete(id));
    }


}
