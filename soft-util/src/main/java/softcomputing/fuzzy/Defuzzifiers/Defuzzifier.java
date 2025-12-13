package softcomputing.fuzzy.Defuzzifiers;

import softcomputing.fuzzy.utils.FuzzySet;

public interface Defuzzifier {
    double defuzzify(FuzzySet fuzzySet);
}
