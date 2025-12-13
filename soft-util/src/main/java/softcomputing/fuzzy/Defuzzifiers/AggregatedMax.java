package softcomputing.fuzzy.Defuzzifiers;

import java.util.Map;

import softcomputing.fuzzy.utils.FuzzySet;
import softcomputing.fuzzy.utils.OutputVariable;

public class AggregatedMax implements Defuzzifier {

    private OutputVariable outputVariable;

    public AggregatedMax(OutputVariable outputVariable) {
        this.outputVariable = outputVariable;
    }

    @Override
    public double defuzzify(FuzzySet fuzzySet) {
        Map<String, Double> membershipValues = fuzzySet.getMembershipValues();

        double numerator = 0.0;
        double denominator = 0.0;

        for (Map.Entry<String, Double> entry : membershipValues.entrySet()) {
            String term = entry.getKey();
            double firingStrength = entry.getValue();
            Double centroid = outputVariable.getCrispValue(term);

            if (centroid != null) {
                numerator += firingStrength * centroid;
                denominator += firingStrength;
            }
        }

        return denominator == 0.0 ? 0.0 : numerator / denominator;
    }
}