package com.example.softcomputing.fuzzy;

import com.example.softcomputing.fuzzy.utils.FuzzySet;

public interface Defuzzifier {
    double defuzzify(FuzzySet fuzzySet);
}

