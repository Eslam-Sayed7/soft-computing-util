package com.example.softcomputing.fuzzy.operators;

public class MaxSNorm implements SNorm {
    @Override
    public double apply(double a, double b) {
        return Math.max(a, b);
    }
}
