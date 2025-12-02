package com.example.softcomputing.tests.fitness;

import com.example.softcomputing.genetic.chromosome.BinaryChromosome;
import com.example.softcomputing.utils.FitnessFunction;

public class BinaryTargetFitness implements FitnessFunction<BinaryChromosome> {

    private final int[] target;

    public BinaryTargetFitness(int[] target) {
        this.target = target;
    }

    @Override
    public double evaluate(BinaryChromosome chromosome) {
        int matches = 0;
        for (int i = 0; i < chromosome.length(); i++) {
            if (chromosome.getGene(i) == target[i]) {
                matches++;
            }
        }
        return matches;
    }
}