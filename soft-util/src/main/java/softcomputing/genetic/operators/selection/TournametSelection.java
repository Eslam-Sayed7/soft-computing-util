package softcomputing.genetic.operators.selection;

import java.util.List;
import java.util.Random;

import softcomputing.genetic.chromosome.Chromosome;

public class TournametSelection<C extends Chromosome<?>> implements SelectionStrategy<C> {

    private final int tournamentSize;
    private final Random random;

    public TournametSelection(int tournamentSize) {
        this.tournamentSize = tournamentSize;
        this.random = new Random();
    }

    @Override
    public C selectIndividual(List<C> population) {
        if (population == null || population.isEmpty()) {
            throw new IllegalArgumentException("Population must not be null or empty");
        }

        C best = null;
        double bestFitness = Double.NEGATIVE_INFINITY;

        // Randomly select tournamentSize individuals and pick the best
        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = random.nextInt(population.size());
            C candidate = population.get(randomIndex);
            double fitness = candidate.getFitness();

            if (fitness > bestFitness) {
                bestFitness = fitness;
                best = candidate;
            }
        }

        return best;
    }
}