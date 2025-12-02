package com.example.softcomputing.fuzzy.operators;

public class SumSNorm implements SNorm {
    @Override
    public double apply(double a, double b) {
        return a + b - (a * b);
    }
}
