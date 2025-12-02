package com.example.softcomputing.genetic.operators.selection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.example.softcomputing.genetic.chromosome.Chromosome;

public class RouletteWheelSelection<C extends Chromosome<?>> implements SelectionStrategy<C> {

    private Map<C, Pair<Double, Double>> wheel;

    public RouletteWheelSelection() {
        this.wheel = new HashMap<>();
    }

    public RouletteWheelSelection(List<C> population) {
        createWheel(population);
    }

    public void createWheel(List<C> population) {
        if (population == null || population.isEmpty()) {
            throw new IllegalArgumentException("Population must not be null or empty");
        }
        wheel = new HashMap<>();
        Double lowerBound = 0.0, upperBound = 0.0;
        Double currentFitness = 0.0, totalFitness = 0.0;

        Map<C, Double> fitnessMap = new HashMap<>();
        wheel = new HashMap<>();

        for (C individual : population) {
            currentFitness = individual.getFitness();
            totalFitness += currentFitness;
            fitnessMap.put(individual, currentFitness);
        }

        for (Map.Entry<C, Double> entry : fitnessMap.entrySet()) {
            C individual = entry.getKey();
            Double fitness = entry.getValue();

            upperBound = lowerBound + (fitness / totalFitness);
            lowerBound = upperBound;
            wheel.put(individual, new Pair<>(lowerBound, upperBound));
        }
    }

    @Override
    public C selectIndividual(List<C> population) {
        if (wheel == null || wheel.isEmpty()) {
            createWheel(population);
        }
        Double randomValue = Math.random();
        for (Entry<C, Pair<Double, Double>> entry : wheel.entrySet()) {
            Pair<Double, Double> bounds = entry.getValue();
            if (randomValue >= bounds.getFirst() && randomValue < bounds.getSecond()) {
                return entry.getKey();
            }
        }
        return population.get((int) (Math.random() * population.size()));
    }
}
