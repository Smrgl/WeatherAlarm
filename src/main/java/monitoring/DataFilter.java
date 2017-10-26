package main.java.monitoring;

import main.java.limits.LimitType;

import javax.json.JsonObject;
import java.util.HashMap;
import java.util.Map;

public class DataFilter {

    public Map<LimitType,Double> getWeatherData(JsonObject jList) {
        Map<LimitType,Double> weatherData = new HashMap<>();
        weatherData.put(LimitType.LOW_TEMP_LIMIT, jList.getJsonObject("main").getJsonNumber("temp_min").doubleValue());
        weatherData.put(LimitType.HIGH_TEMP_LIMIT, jList.getJsonObject("main").getJsonNumber("temp_max").doubleValue());
        weatherData.put(LimitType.HIGH_WIND_LIMIT, jList.getJsonObject("wind").getJsonNumber("speed").doubleValue());
        return weatherData;
    }
}
