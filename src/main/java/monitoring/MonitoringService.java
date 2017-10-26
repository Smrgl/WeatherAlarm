package main.java.monitoring;

import main.java.limits.HighTempLimit;
import main.java.limits.HighWindLimit;
import main.java.limits.ILimit;
import main.java.limits.LowTempLimit;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.core.UriBuilder;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MonitoringService  {

    private JsonRequestService requestService;
    private LoggingService loggingService;

    public MonitoringService(JsonRequestService requestService, LoggingService loggingService) {
        this.requestService = requestService;
        this.loggingService = loggingService;
    }

    public void initiateMonitoring() {

        JsonObject joConfigJson = null;
        try {
            joConfigJson = requestService.requestJsonObject(new FileInputStream("./resources/monitoring.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonArray jaCities = joConfigJson != null ? joConfigJson.getJsonArray("cities") : null;
        String APIAccessToken = joConfigJson != null ? joConfigJson.getString("appid") : null;
        int checking_period = joConfigJson != null ? joConfigJson.getInt("checking_period") : 0;
        int number_of_cities = jaCities != null ? jaCities.size() : 0;


        Path alertsPath = Paths.get("./logs/alerts");
        Path logPath = Paths.get("./logs/monitoring.log");

        while (true) {
            try {
                Files.deleteIfExists(alertsPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < number_of_cities; i++) {
                JsonObject object = jaCities.getJsonObject(i);
                int city_id = object.getJsonNumber("id").intValue();

                List<ILimit> limits = getLimits(object);

                URL   openWeatherMapURL = null;
                try {
                    openWeatherMapURL = UriBuilder.fromUri("http://api.openweathermap.org/data/2.5/forecast")
                            .queryParam("units","metric")
                            .queryParam("appid",APIAccessToken)
                            .queryParam("id",city_id)
                            .build()
                            .toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                InputStream inputStream = null;
                try {
                    if (openWeatherMapURL != null) {
                        inputStream = openWeatherMapURL.openStream();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(String.format("Trying to reconnect in %d seconds", checking_period));
                    try {
                        TimeUnit.SECONDS.sleep(checking_period);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    this.initiateMonitoring();
                }
                JsonObject joWeatherForecast = requestService.requestJsonObject(inputStream);

                loggingService.logForecast(joWeatherForecast, limits,alertsPath,logPath);

                System.out.println(String.format("5 day forecast for %s was written to logs/monitoring.log",
                        jaCities.getJsonObject(i).getJsonString("name")));
            }

            try {
                TimeUnit.SECONDS.sleep(checking_period);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public List<ILimit> getLimits(JsonObject joLimitConf) {

        List<ILimit> limits = new ArrayList<>();

        limits.add(new LowTempLimit(joLimitConf.getJsonNumber("temp_low").doubleValue()));
        limits.add(new HighTempLimit(joLimitConf.getJsonNumber("temp_high").doubleValue()));
        limits.add(new HighWindLimit(joLimitConf.getJsonNumber("wind_high").doubleValue()));

        return limits;
    }
}
