package softcomputing.genetic.operators.replacement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import softcomputing.genetic.chromosome.Chromosome;

public class SteadyStateReplacement<C extends Chromosome<?>> implements Replacement<C> {

    public enum ReplacementMode {
        FITNESS
    }

    private final int replacementCount;
    private final ReplacementMode mode;
    private final boolean allowParentReplacement;

    public SteadyStateReplacement(int replacementCount) {
        this(replacementCount, ReplacementMode.FITNESS, true);
    }

    public SteadyStateReplacement(int replacementCount, ReplacementMode mode) {
        this(replacementCount, mode, true);
    }

    public SteadyStateReplacement(int replacementCount, ReplacementMode mode, boolean allowParentReplacement) {
        if (replacementCount <= 0) {
            throw new IllegalArgumentException(
                    String.format("Replacement count must be positive, got: %d", replacementCount));
        }
        this.replacementCount = replacementCount;
        this.mode = mode;
        this.allowParentReplacement = allowParentReplacement;
    }

    public static <C extends Chromosome<?>> SteadyStateReplacement<C> withPercentage(
            int populationSize, double replacementPercentage) {
        if (populationSize <= 0) {
            throw new IllegalArgumentException("Population size must be positive");
        }
        if (replacementPercentage <= 0.0 || replacementPercentage >= 1.0) {
            throw new IllegalArgumentException(
                    String.format("Replacement percentage must be in (0.0, 1.0), got: %.4f", replacementPercentage));
        }
        int count = Math.max(1, (int) Math.round(populationSize * replacementPercentage));
        return new SteadyStateReplacement<>(count);
    }

    // with 10% replacement
    public static <C extends Chromosome<?>> SteadyStateReplacement<C> withStandardRate(int populationSize) {
        return withPercentage(populationSize, 0.1);
    }

    @Override
    public List<C> replacePopulation(List<C> currentPopulation, List<C> newIndividuals) {

        if (currentPopulation == null || currentPopulation.isEmpty())
            return new ArrayList<>();
        if (newIndividuals == null || newIndividuals.isEmpty())
            return new ArrayList<>(currentPopulation);

        int populationSize = currentPopulation.size();

        // Adjust replacement count
        int actualReplacementCount = Math.min(replacementCount, populationSize);
        actualReplacementCount = Math.min(actualReplacementCount, newIndividuals.size());

        return replaceFitnessBased(currentPopulation, newIndividuals, populationSize, actualReplacementCount);
    }

    private List<C> replaceFitnessBased(List<C> currentPopulation, List<C> offspring,
            int populationSize, int actualReplacementCount) {

        // parents
        List<IndividualWithFitness<C>> currentWithFitness = currentPopulation.stream()
                .map(ind -> new IndividualWithFitness<>(ind, ind.getFitness()))
                .collect(Collectors.toList());

        currentWithFitness.sort(Comparator.comparingDouble(IndividualWithFitness<C>::getFitness).reversed());

        // offsprings
        List<IndividualWithFitness<C>> offspringWithFitness = offspring.stream()
                .map(ind -> new IndividualWithFitness<>(ind, ind.getFitness()))
                .collect(Collectors.toList());

        offspringWithFitness.sort(Comparator.comparingDouble(IndividualWithFitness<C>::getFitness).reversed());

        List<C> newPopulation = new ArrayList<>(populationSize);

        int keepCount = populationSize - actualReplacementCount;
        for (int i = 0; i < keepCount; i++) {
            newPopulation.add(currentWithFitness.get(i).getIndividual());
        }

        for (int i = 0; i < actualReplacementCount; i++) {
            newPopulation.add(offspringWithFitness.get(i).getIndividual());
        }

        return newPopulation;
    }

    public int getReplacementCount() {
        return replacementCount;
    }

    public ReplacementMode getMode() {
        return mode;
    }

    public boolean isAllowParentReplacement() {
        return allowParentReplacement;
    }

    private static class IndividualWithFitness<C extends Chromosome<?>> {
        private final C individual;
        private final double fitness;

        public IndividualWithFitness(C individual, double fitness) {
            this.individual = individual;
            this.fitness = fitness;
        }

        public C getIndividual() {
            return individual;
        }

        public double getFitness() {
            return fitness;
        }
    }

    @Override
    public String toString() {
        return String.format("SteadyStateReplacement(count=%d, mode=%s, allowParentReplacement=%s)",
                replacementCount, mode, allowParentReplacement);
    }
}
