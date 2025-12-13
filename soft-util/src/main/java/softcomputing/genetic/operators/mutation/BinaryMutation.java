package softcomputing.genetic.operators.mutation;

import softcomputing.genetic.chromosome.BinaryChromosome;
import java.util.Random;

public class BinaryMutation implements MutationStrategy<BinaryChromosome> {
    private final double mutationProbability;
    private final Random random;

    public BinaryMutation(double mutationProbability) {
        this(mutationProbability, false);
    }

    public BinaryMutation(double mutationProbability, boolean adaptive) {
        validateMutationProbability(mutationProbability);
        this.mutationProbability = mutationProbability;
        this.random = new Random();
    }

    public BinaryMutation(double mutationProbability, Random random) {
        validateMutationProbability(mutationProbability);
        this.mutationProbability = mutationProbability;
        this.random = random;
    }

    private void validateMutationProbability(double probability) {
        if (probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException(
                    String.format("Mutation probability must be in [0.0, 1.0], got: %.4f", probability));
        }
    }

    @Override
    public BinaryChromosome mutate(BinaryChromosome individual) {
        int length = individual.length();

        if (length == 0) {
            return new BinaryChromosome(new int[0]);
        }

        Integer[] originalGenes = individual.toArray();
        int[] mutatedGenes = new int[length];

        int mutateIndex = random.nextInt(length);
        for (int i = 0; i < length; i++) {
            mutatedGenes[i] = originalGenes[i];
        }
        
        if (random.nextDouble() < mutationProbability) {
            // (0 -> 1, 1 -> 0)
            mutatedGenes[mutateIndex] = 1 - mutatedGenes[mutateIndex];
        }

        return new BinaryChromosome(mutatedGenes);
    }

    public double getMutationProbability() {
        return mutationProbability;
    }


    public static BinaryMutation withStandardRate(int chromosomeLength) {
        if (chromosomeLength <= 0) {
            throw new IllegalArgumentException("Chromosome length must be positive");
        }
        return new BinaryMutation(1.0 / chromosomeLength);
    }

    @Override
    public String toString() {
        return String.format("BinaryMutation(probability=%.4f)", mutationProbability);
    }
}
