package softcomputing.fuzzy.utils;

public class InputDomain {
    public final double min;
    public final double max;

    public InputDomain(double min, double max) {
        if (min >= max) {
            throw new IllegalArgumentException("Min must be less than max");
        }
        this.min = min;
        this.max = max;
    }
}
