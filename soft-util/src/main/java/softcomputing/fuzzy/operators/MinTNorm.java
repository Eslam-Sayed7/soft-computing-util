package softcomputing.fuzzy.operators;

public class MinTNorm implements TNorm {
    @Override
    public double apply(double a, double b) {
        return Math.min(a, b);
    }
}
