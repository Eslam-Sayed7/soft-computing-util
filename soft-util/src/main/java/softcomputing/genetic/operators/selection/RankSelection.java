package softcomputing.genetic.operators.selection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import softcomputing.genetic.chromosome.Chromosome;

public class RankSelection<C extends Chromosome<?>> implements SelectionStrategy<C> {

    public enum RankingMode {
        LINEAR,
        EXPONENTIAL
    }

    private final double selectionPressure;
    private final RankingMode rankingMode;
    private final Random random;
    private final boolean maximization;

    private List<C> cachedPopulation;
    private List<RankedIndividual<C>> rankedPopulation;
    private double[] cumulativeProbabilities;

    public RankSelection() {
        this(1.5, RankingMode.LINEAR, true);
    }

    public RankSelection(double selectionPressure) {
        this(selectionPressure, RankingMode.LINEAR, true);
    }

    public RankSelection(double selectionPressure, RankingMode rankingMode, boolean maximization) {
        if (selectionPressure < 1.0 || selectionPressure > 2.0) {
            throw new IllegalArgumentException(
                    "Selection pressure must be between 1.0 and 2.0, got: " + selectionPressure);
        }
        this.selectionPressure = selectionPressure;
        this.rankingMode = rankingMode;
        this.maximization = maximization;
        this.random = new Random();
    }

    @Override
    public C selectIndividual(List<C> population) {
        if (population == null || population.isEmpty()) {
            throw new IllegalArgumentException("Population must not be null or empty");
        }

        // need to rebuild the ranking ?
        if (cachedPopulation != population || rankedPopulation == null) {
            buildRanking(population);
            cachedPopulation = population;
        }

        double randomValue = random.nextDouble();

        for (int i = 0; i < cumulativeProbabilities.length; i++) {
            if (randomValue <= cumulativeProbabilities[i]) {
                return rankedPopulation.get(i).individual;
            }
        }

        // best individual (should rarely happen due to floating point)
        return rankedPopulation.get(rankedPopulation.size() - 1).individual;
    }

    private void buildRanking(List<C> population) {
        int n = population.size();

        // Step 1: Evaluate fitness for all individuals and create ranked list
        rankedPopulation = new ArrayList<>(n);
        for (C individual : population) {
            double fitness = individual.getFitness();
            rankedPopulation.add(new RankedIndividual<>(individual, fitness));
        }

        rankedPopulation.sort(maximization
                ? Comparator.comparingDouble((RankedIndividual<C> ri) -> ri.fitness)
                : Comparator.comparingDouble((RankedIndividual<C> ri) -> ri.fitness).reversed());

        assignRanks();

        double[] probabilities = new double[n];

        if (rankingMode == RankingMode.LINEAR) {
            calculateLinearProbabilities(probabilities, n);
        } else {
            calculateExponentialProbabilities(probabilities, n);
        }

        // Build cumulative for wheel selection
        cumulativeProbabilities = new double[n];
        cumulativeProbabilities[0] = probabilities[0];
        for (int i = 1; i < n; i++) {
            cumulativeProbabilities[i] = cumulativeProbabilities[i - 1] + probabilities[i];
        }

        if (cumulativeProbabilities[n - 1] > 0) {
            for (int i = 0; i < n; i++) {
                cumulativeProbabilities[i] /= cumulativeProbabilities[n - 1];
            }
        }
    }

    private void assignRanks() {
        if (rankedPopulation.isEmpty())
            return;

        int currentRank = 1;
        rankedPopulation.get(0).rank = currentRank;

        for (int i = 1; i < rankedPopulation.size(); i++) {
            if (Math.abs(rankedPopulation.get(i).fitness - rankedPopulation.get(i - 1).fitness) > 1e-10) {
                currentRank = i + 1;
            }
            rankedPopulation.get(i).rank = currentRank;
        }
    }

    private void calculateLinearProbabilities(double[] probabilities, int n) {
        double s = selectionPressure;

        for (int i = 0; i < n; i++) {
            int rank = rankedPopulation.get(i).rank;

            // p(i) = (2-s)/n + (2*i*(s-1))/(n*(n-1))
            if (n == 1) {
                probabilities[i] = 1.0;
            } else {
                probabilities[i] = (2.0 - s) / n + (2.0 * rank * (s - 1.0)) / (n * (n - 1.0));
            }
        }
    }

    // p(i) = (1 - e^(-i)) / C where C normalizes probabilities to sum to 1
    private void calculateExponentialProbabilities(double[] probabilities, int n) {
        double sumProbabilities = 0.0;

        // Calculate unnormalized probabilities
        for (int i = 0; i < n; i++) {
            int rank = rankedPopulation.get(i).rank;
            probabilities[i] = 1.0 - Math.exp(-rank);
            sumProbabilities += probabilities[i];
        }

        if (sumProbabilities > 0) {
            for (int i = 0; i < n; i++) {
                probabilities[i] /= sumProbabilities;
            }
        } else {
            for (int i = 0; i < n; i++) {
                probabilities[i] = 1.0 / n;
            }
        }
    }

    public double getSelectionPressure() {
        return selectionPressure;
    }

    public RankingMode getRankingMode() {
        return rankingMode;
    }

    public boolean isMaximization() {
        return maximization;
    }

    public void clearCache() {
        cachedPopulation = null;
        rankedPopulation = null;
        cumulativeProbabilities = null;
    }

    private static class RankedIndividual<C extends Chromosome<?>> {
        final C individual;
        final double fitness;
        int rank;

        RankedIndividual(C individual, double fitness) {
            this.individual = individual;
            this.fitness = fitness;
        }
    }
}
