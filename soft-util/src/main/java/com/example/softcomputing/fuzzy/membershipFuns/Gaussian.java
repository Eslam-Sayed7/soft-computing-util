package com.example.softcomputing.fuzzy.membershipFuns;

public class Gaussian implements MembershipFunction {

    private double c;
    private double sigma;

    public Gaussian(double c, double sigma) {
        if (sigma <= 0) {
            throw new IllegalArgumentException("Sigma must be > 0 for Gaussian MF");
        }
        this.c = c;
        this.sigma = sigma;
    }

    @Override
    public double apply(double x) {
        return Math.exp(-Math.pow(x - c, 2) / (2 * sigma * sigma));
    }

    public double getCenter() {
        return c;
    }

    public double getSigma() {
        return sigma;
    }
}
