package com.example.softcomputing.utils;

import com.example.softcomputing.genetic.chromosome.Chromosome;

public interface  FitnessFunction {

    double evaluate(Chromosome<?> chromosome);
}
