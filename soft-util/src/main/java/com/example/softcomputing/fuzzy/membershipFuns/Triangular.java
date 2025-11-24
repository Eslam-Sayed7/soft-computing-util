package com.example.softcomputing.fuzzy.membershipFuns;

import java.util.List;

import com.example.softcomputing.fuzzy.MembershipFunction;
import com.example.softcomputing.fuzzy.utils.Point;

public class Triangular implements MembershipFunction {

  private List<Point> points;
  private double b;
  private double slope;

  public Triangular(Point p1, Point p2, Point p3) {

    points.add(p1);
    points.add(p2);
    points.add(p3);

    if (p1 == p2) {
      calcSlope(p2, p3);
      this.b = p2.getY(); // y-intercept

    } else {
      calcSlope(p1, p2);
      this.b = p1.getY(); // zero
    }

  }

  @Override
  public double apply(double crispInput) {
    // y = mx + b
    return slope * crispInput + b;
  }

  public void calcSlope(Point first, Point second) {
    this.slope = (first.getY() - second.getY()) / (first.getX() - second.getX());
  }

  public List<Point> getPoints() {
    return points;
  }

  public void setPoints(List<Point> points) {
    this.points = points;
  }

  public double getb() {
    return b;
  }

  public void setb(double b) {
    this.b = b;
  }

  public double getSlope() {
    return slope;
  }

  public void setSlope(double slope) {
    this.slope = slope;
  }

}
