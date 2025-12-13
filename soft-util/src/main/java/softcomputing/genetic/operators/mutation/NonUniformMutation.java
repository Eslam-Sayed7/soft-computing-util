package softcomputing.genetic.operators.mutation;

import java.util.Random;

import softcomputing.genetic.chromosome.FloatingPointChromosome;

public class NonUniformMutation implements MutationStrategy<FloatingPointChromosome> {

    private final Double mutationRate;
    private final Random random = new Random();
    private final int maxGenerations;
    private final double dependencyFactor;
    private int currentGeneration;

    public NonUniformMutation(Double mutationRate, int maxGenerations, double dependencyFactor) {
        this.mutationRate = mutationRate;
        this.maxGenerations = maxGenerations;
        this.dependencyFactor = dependencyFactor;
        this.currentGeneration = 0;
    }

    public void setCurrentGeneration(int generation) {
        this.currentGeneration = generation;
    }

    @Override
    public FloatingPointChromosome mutate(FloatingPointChromosome individual) {
        int length = individual.length();
        Double[] genes = individual.toArray();
        Double lowerBound = individual.getLowerBound();
        Double upperBound = individual.getUpperBound();

        for (int i = 0; i < length; i++) {
            if (random.nextDouble() < mutationRate) {
                Double xi = genes[i];
                Double r11 = random.nextDouble();
                Double r = random.nextDouble();
                
                Double deltaL = xi - lowerBound;
                Double deltaU = upperBound - xi;

                Double y;
                if (r11 <= 0.5) {
                    y = deltaL;
                } else {
                    y = deltaU;
                }
                
                double ratio = 1.0 - ((double) currentGeneration / maxGenerations);
                double exponent = Math.pow(ratio, dependencyFactor);
                double delta = y * (1.0 - Math.pow(r, exponent));
                
                Double newValue;
                if (r11 <= 0.5) {
                    newValue = xi - delta;
                } else {
                    newValue = xi + delta;
                }
                
                newValue = Math.max(lowerBound, Math.min(upperBound, newValue));
                individual.setGene(i, newValue);
            }
        }

        return individual;
    }
}