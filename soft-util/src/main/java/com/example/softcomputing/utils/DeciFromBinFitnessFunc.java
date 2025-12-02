package com.example.softcomputing.utils;

import com.example.softcomputing.genetic.chromosome.Chromosome;

public class DeciFromBinFitnessFunc implements FitnessFunction {

    @Override
    public double evaluate(Chromosome<?> chromosome) {
        double fitness = 0.0;
        int len = chromosome.length();
        for (int i = 0; i < len; i++) {
            int gene = (Integer) chromosome.getGene(i);
            fitness += gene * Math.pow(2, len - i - 1);
        }
        return fitness;
    }
}
