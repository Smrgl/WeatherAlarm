package test.java.monitoring;

import main.java.monitoring.JsonRequestService;
import org.junit.jupiter.api.Test;

import javax.json.JsonObject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


class JsonRequestServiceTest {
    @Test
    void requestJsonObject() throws IOException {
        InputStream is = new ByteArrayInputStream("{\"main\": {\"temp_min\": 2.07, \"temp_max\": 100 }, \"wind\": {\"someString\": \"2.11abc\"} }".getBytes("UTF-8"));
        JsonRequestService jrs = new JsonRequestService();
        JsonObject jo = jrs.requestJsonObject(is);

        String s = jo.getJsonObject("wind").getJsonString("someString").getString();
        double d = jo.getJsonObject("main").getJsonNumber("temp_min").doubleValue();
        int i = jo.getJsonObject("main").getJsonNumber("temp_max").intValue();

        assertEquals(true,2.07 == d);
        assertEquals(true,100 == i);
        assertEquals(true,"2.11abc".equals(s) );

        assertEquals(false,40 == d);
        assertEquals(true,100.0 == i);

        assertEquals(false,2 == d);
        assertEquals(true,100.0 == i);


    }

}