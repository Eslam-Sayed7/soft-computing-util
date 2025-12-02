package com.example.softcomputing.fuzzy.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuzzySet {
    private final String name;
    private final List<Point> membershipPoints;
    // optional named membership values map (linguistic label -> degree)
    private Map<String, Double> membershipValuesMap;

    public FuzzySet(String name) {
        this.name = name;
        this.membershipPoints = new ArrayList<>();
        this.membershipValuesMap = new HashMap<>();
    }

    public FuzzySet() {
        this("unnamed");
    }

    public FuzzySet(String name, List<Point> points) {
        this.name = name;
        this.membershipPoints = new ArrayList<>(points);
        this.membershipValuesMap = new HashMap<>();
    }

    public void addPoint(double x, double membershipValue) {
        membershipPoints.add(new Point(x, membershipValue));
    }

    public void addPoint(Point point) {
        membershipPoints.add(point);
    }

    public String getName() {
        return name;
    }

    public List<Point> getMembershipPoints() {
        return new ArrayList<>(membershipPoints);
    }

    public double getMembershipValue(double x) {
        if (membershipPoints.isEmpty()) {
            return 0.0;
        }

        if (membershipPoints.size() == 1) {
            return membershipPoints.get(0).getY();
        }

        for (int i = 0; i < membershipPoints.size() - 1; i++) {
            Point p1 = membershipPoints.get(i);
            Point p2 = membershipPoints.get(i + 1);

            if (x >= p1.getX() && x <= p2.getX()) {
                if (p2.getX() == p1.getX()) {
                    return p1.getY();
                }
                return p1.getY() + (p2.getY() - p1.getY()) * (x - p1.getX()) / (p2.getX() - p1.getX());
            }
        }

        if (x < membershipPoints.get(0).getX()) {
            return membershipPoints.get(0).getY();
        }
        return membershipPoints.get(membershipPoints.size() - 1).getY();
    }

    public enum ExtremumType {
        MIN, MAX
    }

    public double getExtremumX(ExtremumType type) { // to get min or max x value
        if (type == ExtremumType.MIN) {
            return membershipPoints.stream()
                    .mapToDouble(Point::getX)
                    .min()
                    .orElse(0.0);
        } else {
            return membershipPoints.stream()
                    .mapToDouble(Point::getX)
                    .max()
                    .orElse(0.0);
        }
    }

    public void setMembershipValues(java.util.Map<String, Double> map) {
        this.membershipValuesMap = new java.util.HashMap<>(map);
        this.membershipPoints.clear();
        int idx = 0;
        for (java.util.Map.Entry<String, Double> e : map.entrySet()) {
            this.membershipPoints.add(new Point(idx++, e.getValue()));
        }
    }

    /**
     * Returns the linguistic membership map
     */
    public java.util.Map<String, Double> getMembershipValues() {
        return new java.util.HashMap<>(membershipValuesMap);
    }

    @Override
    public String toString() {
        return String.format("FuzzySet{name='%s', points=%d}", name, membershipPoints.size());
    }
}
