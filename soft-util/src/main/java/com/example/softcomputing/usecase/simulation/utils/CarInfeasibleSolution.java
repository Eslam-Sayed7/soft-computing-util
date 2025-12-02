package com.example.softcomputing.usecase.simulation.utils;

import com.example.softcomputing.genetic.chromosome.FloatingPointChromosome;
import com.example.softcomputing.utils.InfeasibleSolution;

public class CarInfeasibleSolution implements InfeasibleSolution<FloatingPointChromosome> {
    public CarInfeasibleSolution() {
    };

    @Override
    public boolean checkInfeasible(FloatingPointChromosome chromosome) {
        Double[] genes = chromosome.toArray();

        for (Double gene : genes) {
            if (gene == null || gene.isNaN() || gene.isInfinite()) {
                return true;
            }
            if (Math.abs(gene) > 1.0) {
                return true;
            }
        }
        return false;
    }
}
