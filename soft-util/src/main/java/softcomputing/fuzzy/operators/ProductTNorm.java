package softcomputing.fuzzy.operators;

public class ProductTNorm implements TNorm {
    @Override
    public double apply(double a, double b) {
        return a * b;
    }
}
