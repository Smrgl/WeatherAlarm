package test.java.monitoring;

import main.java.limits.*;
import main.java.monitoring.AlertService;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlertServiceTest {

    AlertService as = new AlertService();

    @Test
    void limitCrossed() {
        Map<LimitType,Double> hm = new HashMap<>();


        ILimit lowTemp = new LowTempLimit(-10.0);
        ILimit highTemp = new HighTempLimit(-10.0);
        ILimit highWind = new HighWindLimit(-10.0);

        hm.put(LimitType.LOW_TEMP_LIMIT, -10.0);
        hm.put(LimitType.HIGH_TEMP_LIMIT, -10.0);
        hm.put(LimitType.HIGH_WIND_LIMIT, -10.0);

        assertEquals(true, as.limitCrossed(lowTemp,hm));
        assertEquals(true, as.limitCrossed(highTemp,hm));
        assertEquals(true, as.limitCrossed(highWind,hm));



        lowTemp.setLimit(10.0);
        highTemp.setLimit(10.0);
        highWind.setLimit(10.0);

        hm.put(LimitType.LOW_TEMP_LIMIT, 10.0);
        hm.put(LimitType.HIGH_TEMP_LIMIT, 10.0);
        hm.put(LimitType.HIGH_WIND_LIMIT, 10.0);

        assertEquals(true, as.limitCrossed(lowTemp,hm));
        assertEquals(true, as.limitCrossed(highTemp,hm));
        assertEquals(true, as.limitCrossed(highWind,hm));



        lowTemp.setLimit(-10.0);
        highTemp.setLimit(-10.0);
        highWind.setLimit(-10.0);

        hm.put(LimitType.LOW_TEMP_LIMIT, 10.0);
        hm.put(LimitType.HIGH_TEMP_LIMIT, 10.0);
        hm.put(LimitType.HIGH_WIND_LIMIT, 10.0);

        assertEquals(false, as.limitCrossed(lowTemp,hm));
        assertEquals(true, as.limitCrossed(highTemp,hm));
        assertEquals(true, as.limitCrossed(highWind,hm));




        lowTemp.setLimit(10.0);
        highTemp.setLimit(10.0);
        highWind.setLimit(10.0);

        hm.put(LimitType.LOW_TEMP_LIMIT, -10.0);
        hm.put(LimitType.HIGH_TEMP_LIMIT, -10.0);
        hm.put(LimitType.HIGH_WIND_LIMIT, -10.0);

        assertEquals(true, as.limitCrossed(lowTemp,hm));
        assertEquals(false, as.limitCrossed(highTemp,hm));
        assertEquals(false, as.limitCrossed(highWind,hm));
    }

}