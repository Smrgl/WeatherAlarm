package main.java.monitoring;

import main.java.limits.ILimit;
import main.java.limits.LimitType;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.*;

public class LoggingService {

    private AlertService alertService;
    private DataFilter dataFilter;

    public LoggingService(AlertService alertService, DataFilter dataFilter) {
        this.alertService = alertService;
        this.dataFilter = dataFilter;
    }

    void logForecast(JsonObject joWeatherForecast, List<ILimit> limits, Path alertsPath, Path logPath){

        int amountOfRecordPerCity =  joWeatherForecast.getJsonArray("list").size();
        String city = joWeatherForecast.getJsonObject("city").getJsonString("name").toString();
        String newLine = System.lineSeparator();

        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder jsonBuilder = factory.createObjectBuilder();
        jsonBuilder.add("city",city);
        Map<LimitType,String> alertsMap = new HashMap<>();

        // write forecast data to file logs/monitoring.log
        for (int i = 0; i < amountOfRecordPerCity; i++) {

            JsonObject listOfWeatherDataRecodrs = joWeatherForecast.getJsonArray("list").getJsonObject(i);
            Map<LimitType,Double> weatherData = this.dataFilter.getWeatherData(listOfWeatherDataRecodrs);

            StringBuilder sbForecastText = getForecastText(city,listOfWeatherDataRecodrs);

            try (FileWriter fw = new FileWriter(logPath.toString(), true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {

                // mark values that hit the limit and trigger alert
                for (ILimit limit:limits) {
                    if(this.alertService.limitCrossed(limit,weatherData)){
                        sbForecastText.append(limit).append(newLine);
                        alertsMap.put(limit.getType(),limit.toString());
                    }
                }

                sbForecastText.append(newLine).append("--------------------").append(newLine);
                out.println(sbForecastText.toString());
                sbForecastText.setLength(0);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // write detected alerts to file logs/alerts
        try (FileWriter fw = new FileWriter(alertsPath.toString(), true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            for (LimitType type:alertsMap.keySet()) {
                jsonBuilder.add(type.toString(),alertsMap.get(type));
            }
            JsonObject logJson = jsonBuilder.build();
            out.println(logJson.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public StringBuilder getForecastText(String city,JsonObject jList) {
        StringBuilder sb = new StringBuilder();
        String newLine = System.lineSeparator();

        sb.append("city: ").append(city).append(newLine);
        sb.append("date_time: ").append(jList.getJsonString("dt_txt").toString()).append(newLine);
        sb.append("data updated: ").append(new Date(new Timestamp(System.currentTimeMillis()).getTime())).append(newLine);

        Iterator<?> keys = jList.getJsonObject("main").keySet().iterator();
        while( keys.hasNext() ) {
            String key = (String)keys.next();
            String value = jList.getJsonObject("main").getJsonNumber(key).toString();
            sb.append(key).append(": ").append(value).append(newLine);
        }

        keys = jList.getJsonObject("wind").keySet().iterator();
        while( keys.hasNext() ) {
            String key = (String)keys.next();
            String value = jList.getJsonObject("wind").getJsonNumber(key).toString();
            sb.append("wind_").append(key).append(": ").append(value).append(newLine);
        }

        sb.append("--------------------").append(newLine);

        return sb;
    }
}
