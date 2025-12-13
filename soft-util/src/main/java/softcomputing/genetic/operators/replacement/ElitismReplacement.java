package softcomputing.genetic.operators.replacement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import softcomputing.genetic.chromosome.Chromosome;

public class ElitismReplacement<C extends Chromosome<?>> implements Replacement<C> {

    private final int eliteCount;

    public ElitismReplacement() {
        this(2);
    }

    public ElitismReplacement(int eliteCount) {
        if (eliteCount <= 0) {
            throw new IllegalArgumentException(
                    String.format("Elite count must be positive, got: %d", eliteCount));
        }
        this.eliteCount = eliteCount;
    }

    public static <C extends Chromosome<?>> ElitismReplacement<C> withPercentage(int populationSize,
            double elitePercentage) {
        if (populationSize <= 0)
            throw new IllegalArgumentException("Population size must be positive");
        if (elitePercentage <= 0.0 || elitePercentage >= 1.0) {
            throw new IllegalArgumentException(
                    String.format("Elite percentage must be in (0.0, 1.0), got: %.4f", elitePercentage));
        }
        int count = Math.max(1, (int) Math.round(populationSize * elitePercentage));
        return new ElitismReplacement<>(count);
    }

    public static <C extends Chromosome<?>> ElitismReplacement<C> withStandardRate(int populationSize) {
        int count = Math.max(2, (int) Math.round(populationSize * 0.05));
        return new ElitismReplacement<>(count);
    }

    @Override
    public List<C> replacePopulation(List<C> currentPopulation, List<C> newIndividuals) {
        if (currentPopulation == null || currentPopulation.isEmpty()) {
            return new ArrayList<>();
        }
        if (newIndividuals == null || newIndividuals.isEmpty()) {
            return new ArrayList<>(currentPopulation);
        }

        int populationSize = currentPopulation.size();
        int actualEliteCount = Math.min(eliteCount, populationSize);

        List<IndividualWithFitness<C>> currentWithFitness = currentPopulation.stream()
                .map(ind -> new IndividualWithFitness<>(ind, ind.getFitness()))
                .collect(Collectors.toList());

        currentWithFitness.sort(Comparator.comparingDouble(IndividualWithFitness<C>::getFitness).reversed());

        List<C> elite = new ArrayList<>(actualEliteCount);
        for (int i = 0; i < actualEliteCount; i++) {
            elite.add(currentWithFitness.get(i).getIndividual());
        }

        List<IndividualWithFitness<C>> offspringWithFitness = newIndividuals.stream()
                .map(ind -> new IndividualWithFitness<>(ind, ind.getFitness()))
                .collect(Collectors.toList());

        offspringWithFitness.sort(Comparator.comparingDouble(IndividualWithFitness<C>::getFitness).reversed());

        List<C> newPopulation = new ArrayList<>(populationSize);

        newPopulation.addAll(elite);

        int remainingSlots = populationSize - actualEliteCount;
        int offspringToAdd = Math.min(remainingSlots, offspringWithFitness.size());

        for (int i = 0; i < offspringToAdd; i++) {
            newPopulation.add(offspringWithFitness.get(i).getIndividual());
        }

        if (newPopulation.size() < populationSize) {
            int needed = populationSize - newPopulation.size();
            for (int i = actualEliteCount; i < currentWithFitness.size() && needed > 0; i++) {
                newPopulation.add(currentWithFitness.get(i).getIndividual());
                needed--;
            }
        }

        return newPopulation;
    }

    public int getEliteCount() {
        return eliteCount;
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
        return String.format("ElitismReplacement(eliteCount=%d)", eliteCount);
    }
}
