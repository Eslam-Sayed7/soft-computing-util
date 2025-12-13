package softcomputing.fuzzy.utils;

import java.util.HashMap;
import java.util.Map;

public class OutputVariable {
    private String name;
    private Map<String, Double> linguisticTermMapping;
    private double minValue;
    private double maxValue;

    public OutputVariable(String name, double minValue, double maxValue) {
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.linguisticTermMapping = new HashMap<>();
    }

    public void addTerm(String linguisticTerm, double crispValue) {
        if (crispValue < minValue || crispValue > maxValue) {
            throw new IllegalArgumentException(
                    "Crisp value " + crispValue + " outside domain [" + minValue + ", " + maxValue + "]");
        }
        linguisticTermMapping.put(linguisticTerm, crispValue);
    }

    public String getName() {
        return name;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public Map<String, Double> getLinguisticTermMapping() {
        return new HashMap<>(linguisticTermMapping);
    }

    public Double getCrispValue(String linguisticTerm) {
        return linguisticTermMapping.get(linguisticTerm);
    }
}
