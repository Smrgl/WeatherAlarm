package test.java.monitoring;

import main.java.limits.LimitType;
import main.java.monitoring.DataFilter;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataFilterTest {
    DataFilter filter = new DataFilter();

    @Test
    void getWeatherData() {

        JsonObject jo = Json.createReader(new StringReader("{\"main\": {\"temp_min\": 2.07, \"temp_max\": 5.82 }, \"wind\": {\"speed\": 2.11 } }")).readObject();
        Map<LimitType,Double> weatherData =filter.getWeatherData(jo);

        assertEquals(2.07, weatherData.get(LimitType.LOW_TEMP_LIMIT).doubleValue());
        assertEquals(5.82, weatherData.get(LimitType.HIGH_TEMP_LIMIT).doubleValue());
        assertEquals(2.11, weatherData.get(LimitType.HIGH_WIND_LIMIT).doubleValue());

    }

}