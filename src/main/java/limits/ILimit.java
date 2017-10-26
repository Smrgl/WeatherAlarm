package main.java.limits;

public interface ILimit {

    LimitType getType();
    Double getLimit();
    void setLimit(Double limit);
}
