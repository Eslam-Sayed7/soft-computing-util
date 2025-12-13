package softcomputing.genetic.operators.mutation;

import java.util.Random;

import softcomputing.genetic.chromosome.IntegerChromosome;

public class IntegerMutation implements MutationStrategy<IntegerChromosome> {

    private final double mutationRate;
    private final Random random = new Random();

    public IntegerMutation(double mutationRate) {
        if (mutationRate < 0.0 || mutationRate > 1.0) {
            throw new IllegalArgumentException("mutationRate must be in [0,1]");
        }
        this.mutationRate = mutationRate;
    }

    @Override
    public IntegerChromosome mutate(IntegerChromosome individual) {
        if (individual == null) throw new NullPointerException("individual is null");
        int len = individual.length();
        if (len == 0) return individual;

        if (random.nextDouble() < mutationRate) {
            int mutationValue = random.nextInt(11); // 0..10
            if (random.nextBoolean()) mutationValue = -mutationValue;
            int mutationIndex = random.nextInt(len);
            individual.setGene(mutationIndex, mutationValue);

        }
        return individual;
    }
}