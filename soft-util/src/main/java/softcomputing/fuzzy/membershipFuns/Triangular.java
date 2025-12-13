package softcomputing.fuzzy.membershipFuns;

import softcomputing.fuzzy.utils.Point;

public class Triangular implements MembershipFunction {

  private Point p1; // left point
  private Point p2; // peak point
  private Point p3; // right point

  public Triangular(Point p1, Point p2, Point p3) {
    this.p1 = p1;
    this.p2 = p2;
    this.p3 = p3;
  }

  @Override
  public double apply(double x) {
    // Triangular membership function
    // Returns 0 if outside [p1.x, p3.x]
    // Linear rise from p1 to p2, linear fall from p2 to p3

    if (x <= p1.getX() || x >= p3.getX()) {
      return 0.0;
    }

    if (x == p2.getX()) {
      return p2.getY(); // peak value
    }

    if (x < p2.getX()) {
      // Rising slope from p1 to p2
      double slope = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
      return p1.getY() + slope * (x - p1.getX());
    } else {
      // Falling slope from p2 to p3
      double slope = (p3.getY() - p2.getY()) / (p3.getX() - p2.getX());
      return p2.getY() + slope * (x - p2.getX());
    }
  }

  public Point getP1() {
    return p1;
  }

  public Point getP2() {
    return p2;
  }

  public Point getP3() {
    return p3;
  }

}
