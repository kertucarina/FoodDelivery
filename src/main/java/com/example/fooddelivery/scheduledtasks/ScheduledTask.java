package com.example.fooddelivery.scheduledtasks;

import com.example.fooddelivery.controller.WeatherConditionController;
import com.example.fooddelivery.entity.WeatherCondition;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.xml.XMLConstants;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduledTask {

    @Autowired
    private WeatherConditionController weatherConditionController;

    /**
     * A CronJob that requests weather data from <a href="https://www.ilmateenistus.ee/">Estonian National Weather Service</a>
     * every hour at HH:15:00 and inserts the received weather data for Tallinn, Tartu and Pärnu
     * into the database.
     */
    //@Scheduled(cron = "* 15 * * * *")
    @Scheduled(cron = "* * * * * *")
    public void retrieveWeatherData() {
        try {
            List<String> stations = new ArrayList<>(List.of("Tallinn-Harku", "Tartu-Tõravere", "Pärnu"));
            URL url = new URL("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php");
            SAXBuilder sax = new SAXBuilder();

            // prevent xxe - https://rules.sonarsource.com/java/RSPEC-2755
            sax.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            sax.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

            Document doc = sax.build(url);

            Element rootNode = doc.getRootElement();
            List<Element> observations = rootNode.getChildren("station");

            // parse timestamp
            String timestampString = rootNode.getAttributeValue("timestamp");
            LocalDateTime timestamp = Instant.ofEpochSecond(Long.parseLong(timestampString)).atZone(ZoneId.of("UTC+2")).toLocalDateTime();

            for (Element observation : observations) {
                String station = observation.getChildText("name");
                if (stations.contains(station)) {
                    // parse other necessary fields
                    String city = station.split("-")[0];
                    int WMO = Integer.parseInt(observation.getChildText("wmocode"));
                    double temperature = Double.parseDouble(observation.getChildText("airtemperature"));
                    double windSpeed = Double.parseDouble(observation.getChildText("windspeed"));
                    String phenomenon = observation.getChildText("phenomenon");

                    // save WeatherCondition entity
                    saveEntity(station, city, WMO, temperature, windSpeed, phenomenon, timestamp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method creates a WeatherCondition entity with the given parameters and
     * inserts it into the database.
     */
    public void saveEntity(String station, String city, int WMO, double temperature, double windSpeed, String phenomenon, LocalDateTime timestamp) {
        WeatherCondition weatherCondition = new WeatherCondition();
        weatherCondition.setStation(station);
        weatherCondition.setCity(city);
        weatherCondition.setWMO(WMO);
        weatherCondition.setTemperature(temperature);
        weatherCondition.setWindSpeed(windSpeed);
        weatherCondition.setPhenomenon(phenomenon);
        weatherCondition.setTimestamp(timestamp);

        // save the entity to the database
        weatherConditionController.post(weatherCondition);
    }

}