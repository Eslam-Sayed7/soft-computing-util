package com.example.softcomputing.utils;

import com.example.softcomputing.genetic.chromosome.Chromosome;

public interface InfeasibleSolution<C extends Chromosome<?>> {
    boolean checkInfeasible(C chromosome);
}
