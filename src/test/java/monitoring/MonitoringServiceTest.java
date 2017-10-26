package test.java.monitoring;

import main.java.monitoring.*;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonitoringServiceTest {

    @Test
    void getLimits() {
        AlertService alertService = new AlertService();
        DataFilter dataFilter = new DataFilter();
        LoggingService loggingService = new LoggingService(alertService,dataFilter);
        JsonRequestService jsonRequestService = new JsonRequestService();
        MonitoringService mService = new MonitoringService(jsonRequestService,loggingService);


        JsonObject jo = Json.createReader(new StringReader("    {\"name\": \"Honolulu\", \"id\": 5856195, \"temp_low\": -10.0, \"temp_high\": 20.0, \"wind_high\": 30.0 }")).readObject();

        assertEquals(-10.0,mService.getLimits(jo).get(0).getLimit().doubleValue());
        assertEquals(20.0,mService.getLimits(jo).get(1).getLimit().doubleValue());
        assertEquals(30.0,mService.getLimits(jo).get(2).getLimit().doubleValue());


        assertEquals(false,10.0 == mService.getLimits(jo).get(0).getLimit());
        assertEquals(false,-20.0 == mService.getLimits(jo).get(1).getLimit());
        assertEquals(false,20.0 == mService.getLimits(jo).get(2).getLimit());
    }

}