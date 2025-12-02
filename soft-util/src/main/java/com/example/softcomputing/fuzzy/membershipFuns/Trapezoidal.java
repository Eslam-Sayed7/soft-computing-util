package com.example.softcomputing.fuzzy.membershipFuns;

public class Trapezoidal implements MembershipFunction {

    private double a, b, c, d;

    public Trapezoidal(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public double apply(double x) {
        if (x <= a || x >= d) {
            return 0.0;
        } else if (x >= b && x <= c) {
            return 1.0;
        } else if (x > a && x < b) {
            return (x - a) / (b - a);
        } else { // x > c && x < d
            return (d - x) / (d - c);
        }
    }
}
