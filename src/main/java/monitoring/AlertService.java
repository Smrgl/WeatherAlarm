package main.java.monitoring;

import main.java.limits.ILimit;
import main.java.limits.LimitType;

import java.util.Map;

public class AlertService {
    public boolean limitCrossed(ILimit limit, Map<LimitType,Double> map){

        switch (limit.getType()){
            case LOW_TEMP_LIMIT:
                return map.get(LimitType.LOW_TEMP_LIMIT) <= limit.getLimit();
            case HIGH_TEMP_LIMIT:
                return  map.get(LimitType.HIGH_TEMP_LIMIT) >= limit.getLimit();
            case HIGH_WIND_LIMIT:
                return  map.get(LimitType.HIGH_WIND_LIMIT) >= limit.getLimit();
            default:
                return false;
        }
    }
}
