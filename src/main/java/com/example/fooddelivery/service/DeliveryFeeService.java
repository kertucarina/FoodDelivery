package com.example.fooddelivery.service;

import com.example.fooddelivery.entity.WeatherCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeliveryFeeService {

    @Autowired
    private WeatherConditionService weatherConditionService;

    /**
     * Calculating the total delivery fee according to city and vehicle type.
     *
     * @param city    The city: Tallinn / Tartu / Pärnu
     * @param vehicle The type of the vehicle: Car / Scooter / Bike
     * @return The total delivery fee calculated according to input parameters and business rules, or an error message.
     */
    public String calculateFee(String city, String vehicle) {
        List<String> allowedCities = new ArrayList<>(List.of("Tallinn", "Tartu", "Pärnu"));
        List<String> allowedVehicles = new ArrayList<>(List.of("Car", "Scooter", "Bike"));

        try {
            if (allowedCities.contains(city) && allowedVehicles.contains(vehicle)) {
                WeatherCondition weather = weatherConditionService.findCurrentWeatherByCity(city);
                double deliveryFee = RBF(city, vehicle) + ATEF(weather.getTemperature(), vehicle) + WSEF(weather.getWindSpeed(), vehicle) + WPEF(weather.getPhenomenon(), vehicle);
                return String.valueOf(deliveryFee);
            } else return "Invalid request parameters!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    /**
     * Calculating the regional base fee according to city and vehicle type.
     *
     * @param city    The city: Tallinn / Tartu / Pärnu
     * @param vehicle The type of the vehicle: Car / Scooter / Bike
     * @return The regional base fee calculated according to input parameters and business rules.
     */
    public double RBF(String city, String vehicle) {
        return switch (city) {
            case "Tallinn" -> switch (vehicle) {
                case "Car" -> 4;
                case "Scooter" -> 3.5;
                default -> 3;
            };
            case "Tartu" -> switch (vehicle) {
                case "Car" -> 3.5;
                case "Scooter" -> 3;
                default -> 2.5;
            };
            default -> switch (vehicle) {
                case "Car" -> 3;
                case "Scooter" -> 2.5;
                default -> 2;
            };
        };
    }


    /**
     * Calculating the extra fee based on air temperature in a specific city in case the selected vehicle is Scooter or Bike.
     *
     * @param temperature Air temperature (°C) in the specific city.
     * @param vehicle     Vehicle type: Car / Scooter / Bike
     * @return Air temperature extra fee calculated according to input parameters and business rules.
     */
    public double ATEF(double temperature, String vehicle) {
        double airTemperatureExtraFee = 0;
        if (vehicle.equals("Scooter") || vehicle.equals("Bike")) {
            if (temperature <= 0) {
                if (temperature < -10) airTemperatureExtraFee = 1;
                else airTemperatureExtraFee = 0.5;
            }
        }
        return airTemperatureExtraFee;
    }


    /**
     * Calculating the extra fee based on wind speed in a specific city in case the vehicle = Bike.
     *
     * @param windSpeed Wind speed (m/s) in the specific city.
     * @param vehicle   Vehicle type: Car / Scooter / Bike
     * @return Wind speed extra fee calculated according to input parameters and business rules.
     * @throws Exception is thrown when the wind is too strong for the selected vehicle (bike).
     */
    public double WSEF(double windSpeed, String vehicle) throws Exception {
        double windSpeedExtraFee = 0;
        if (vehicle.equals("Bike")) {
            if (windSpeed >= 10) {
                if (windSpeed > 20) throw new Exception("Usage of selected vehicle type is forbidden");
                else windSpeedExtraFee = 0.5;
            }
        }
        return windSpeedExtraFee;
    }


    /**
     * Calculating the extra fee based on weather phenomenon in case the selected vehicle is Scooter or Bike.
     *
     * @param phenomenon The weather phenomenon occurring in the specific city, or the degree of cloudiness in its absence.
     * @param vehicle    Vehicle type: Car / Scooter / Bike
     * @return Weather phenomenon extra fee calculated according to input parameters and business rules.
     * @throws Exception is thrown when the weather phenomenon is glaze, hail, or thunder, and the selected vehicle is Scooter or Bike.
     */
    public double WPEF(String phenomenon, String vehicle) throws Exception {
        List<String> snowOrSleet = new ArrayList<>(List.of("Light snow shower", "Moderate snow shower", "Heavy snow shower", "Light sleet", "Moderate sleet", "Light snowfall", "Moderate snowfall", "Heavy snowfall", "Blowing snow", "Drifting snow"));
        List<String> rain = new ArrayList<>(List.of("Light shower", "Moderate shower", "Heavy shower", "Light rain", "Moderate rain", "Heavy rain"));
        List<String> glazeHailThunder = new ArrayList<>(List.of("Glaze", "Hail", "Thunder", "Thunderstorm"));

        if (vehicle.equals("Scooter") || vehicle.equals("Bike")) {
            if (snowOrSleet.contains(phenomenon)) return 1;
            else if (rain.contains(phenomenon)) return 0.5;
            else if (glazeHailThunder.contains(phenomenon))
                throw new Exception("Usage of selected vehicle type is forbidden");
        }

        return 0;
    }
}
