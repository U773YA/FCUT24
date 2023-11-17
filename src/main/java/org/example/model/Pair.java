package org.example.model;

public class Pair {
    private final Integer first;
    private final Double second;

    public Pair(Integer first, Double second) {
        this.first = first;
        this.second = second;
    }

    public Integer getFirst() {
        return first;
    }

    public Double getSecond() {
        return second;
    }
}