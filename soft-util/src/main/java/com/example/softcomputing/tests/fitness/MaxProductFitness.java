package com.example.softcomputing.tests.fitness;

import com.example.softcomputing.genetic.chromosome.FloatingPointChromosome;
import com.example.softcomputing.utils.FitnessFunction;

public class MaxProductFitness implements FitnessFunction<FloatingPointChromosome> {

    @Override
    public double evaluate(FloatingPointChromosome chromosome) {
        double product = 1.0;
        for (int i = 0; i < chromosome.length(); i++) {
            product *= chromosome.getGene(i);
        }
        return product;
    }
}