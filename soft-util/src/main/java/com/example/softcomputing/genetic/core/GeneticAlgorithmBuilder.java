package com.example.softcomputing.genetic.core;

import java.util.List;
import java.util.Objects;

import com.example.softcomputing.genetic.chromosome.Chromosome;
import com.example.softcomputing.genetic.chromosome.Factories.ChromosomeFactory;
import com.example.softcomputing.genetic.operators.crossover.CrossoverStrategy;
import com.example.softcomputing.genetic.operators.mutation.MutationStrategy;
import com.example.softcomputing.genetic.operators.replacement.Replacement;
import com.example.softcomputing.genetic.operators.selection.SelectionStrategy;
import com.example.softcomputing.utils.AppLogger;
import com.example.softcomputing.utils.FitnessFunction;

public class GeneticAlgorithmBuilder<C extends Chromosome<?>> {

    long maxGenerations = 100;
    int geneLength = 10;
    int populationSize = 100;
    double mutationRate = 0.01;
    double crossoverRate = 0.7;

    List<C> population = null;
    SelectionStrategy<C> selection;
    CrossoverStrategy<C> crossover;
    MutationStrategy<C> mutation;
    Replacement<C> replacement;
    ChromosomeFactory<?, C> chromosomeFactory;
    FitnessFunction fitnessFunction;

    AppLogger logger = AppLogger.getLogger(GeneticAlgorithmBuilder.class);

    public GeneticAlgorithmBuilder<C> withChromosomeFactory(ChromosomeFactory<?, C> factory) {
        this.chromosomeFactory = factory;
        return this;
    }

    public GeneticAlgorithmBuilder<C> withPopulation(List<C> population) {
        this.population = population;
        return this;
    }

    public GeneticAlgorithmBuilder<C> withCrossoverRate(double rate) {
        this.crossoverRate = rate;
        return this;
    }

    public GeneticAlgorithmBuilder<C> withMutationRate(double rate) {
        this.mutationRate = rate;
        return this;
    }

    public GeneticAlgorithmBuilder<C> withGeneLength(int length) {
        this.geneLength = length;
        return this;
    }

    public GeneticAlgorithmBuilder<C> withFitnessFunction(FitnessFunction<C> fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
        return this;
    }

    public GeneticAlgorithmBuilder<C> withPopulation() {
        if (chromosomeFactory == null) {
            throw new IllegalStateException("Chromosome factory must be set before initializing population.");
        }

        List<C> initialPopulation = chromosomeFactory.createPopulation(populationSize, geneLength);
        this.population = initialPopulation;
        return this;
    }

    public GeneticAlgorithmBuilder<C> withSelectionStrategy(SelectionStrategy<C> s) {
        this.selection = s;
        return this;
    }

    public GeneticAlgorithmBuilder<C> withCrossoverStrategy(CrossoverStrategy<C> c) {
        this.crossover = c;
        return this;
    }

    public GeneticAlgorithmBuilder<C> withMutationStrategy(MutationStrategy<C> m) {
        this.mutation = m;
        return this;
    }

    public GeneticAlgorithmBuilder<C> withReplacementStrategy(Replacement<C> r) {
        this.replacement = r;
        return this;
    }

    public GeneticAlgorithmBuilder<C> withPopulationSize(int size) {
        this.populationSize = size;
        return this;
    }

    public GeneticAlgorithmBuilder<C> withMaxGenerations(long maxGen) {
        this.maxGenerations = maxGen;
        return this;
    }

    public GeneticAlgorithmBuilder<C> withFitnessFunction(FitnessFunction fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
        return this;
    }

    public GeneticAlgorithm<C> build() {
        Objects.requireNonNull(selection, "selection strategy is required");
        Objects.requireNonNull(crossover, "crossover strategy is required");
        Objects.requireNonNull(mutation, "mutation strategy is required");
        Objects.requireNonNull(replacement, "replacement strategy is required");
        Objects.requireNonNull(chromosomeFactory, "chromosome factory is required");
        Objects.requireNonNull(fitnessFunction, "fitness function is required");
        if (populationSize <= 0) {
            throw new IllegalArgumentException("populationSize must be > 0");
        }

        return new GeneticAlgorithm<C>(this);
    }
}
