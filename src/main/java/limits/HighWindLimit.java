package main.java.limits;

public class HighWindLimit implements ILimit {

    private Double limit;

    public HighWindLimit(Double limit) {
        this.limit = limit;
    }

    @Override
    public LimitType getType() { return LimitType.HIGH_WIND_LIMIT; }

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
        return "ALERT: Strong wind!";
    }
}
