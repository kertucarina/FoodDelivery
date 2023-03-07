package com.example.fooddelivery.controller;

import com.example.fooddelivery.service.DeliveryFeeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller class with API endpoint which enables other parts of the application
 * to request delivery fees according to city (Tallinn/Tartu/Pärnu) and vehicle type (Car/Scooter/Bike).
 *
 * <p>Example: <a href="http://localhost:8080/deliveryFee?city=Tartu&vehicleType=Bike">http://localhost:8080/deliveryFee?city=Tartu&vehicleType=Bike</a></p>
 */
@Controller
@AllArgsConstructor
@RequestMapping(value = "/deliveryFee")
public class DeliveryFeeController {
    @Autowired
    private DeliveryFeeService deliveryFeeService;

    /**
     * GET /deliveryFee?city={city}&vehicleType={vehicle}
     * <p>REST endpoint that enables to request delivery fees according to city and vehicle type.</p>
     * @param city    The city: Tallinn / Tartu / Pärnu
     * @param vehicle The type of the vehicle: Car / Scooter / Bike
     * @return The delivery fee calculated according to input parameters and business rules, or an error message.
     */
    @GetMapping
    public @ResponseBody ResponseEntity<String> calculateDeliveryFee(@RequestParam("city") String city, @RequestParam("vehicleType") String vehicle) {
        return ResponseEntity.ok(deliveryFeeService.calculateFee(city, vehicle));
    }

}
