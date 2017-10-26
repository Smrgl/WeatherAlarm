package main.java.limits;

public class HighTempLimit implements ILimit {

    private Double limit;

    public HighTempLimit(Double limit) {
        this.limit = limit;
    }

    @Override
    public LimitType getType() {
        return LimitType.HIGH_TEMP_LIMIT;
    }

    @Override
    public Double getLimit() {
        return limit;
    }

    @Override
    public void setLimit(Double limit) {
        this.limit = limit;
    }


    @Override
    public String toString() {
        return "ALERT: High temperatures!";
    }
}
