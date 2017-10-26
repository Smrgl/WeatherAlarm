package main.java.limits;

public class LowTempLimit implements ILimit {

    private Double limit;

    public LowTempLimit(Double limit) {
        this.limit = limit;
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
    public LimitType getType() {
        return LimitType.LOW_TEMP_LIMIT;
    }

    @Override
    public String toString() {
        return "ALERT: Low temperatures!";
    }

}
