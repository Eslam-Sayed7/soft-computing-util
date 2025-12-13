package softcomputing.genetic.operators.mutation;

import java.util.Random;

import softcomputing.genetic.chromosome.FloatingPointChromosome;

public class UniformMutation implements MutationStrategy<FloatingPointChromosome> {

    private final Double mutationRate;
    private final Random random = new Random();

    public UniformMutation(Double mutationRate) {
        this.mutationRate = mutationRate;
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
                Double r1 = random.nextDouble(); 
                Double newValue;
                double r2 = random.nextDouble();

                if (r1 <= 0.5) { // move left
                    newValue = xi - r2;
                } else { // move right
                    newValue = xi + r2;
                }

                newValue = Math.max(lowerBound, Math.min(upperBound, newValue));
                individual.setGene(i, newValue);
            }
        }

        return individual;
    }
}
