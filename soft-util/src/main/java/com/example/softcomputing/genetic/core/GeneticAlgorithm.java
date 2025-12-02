package com.example.softcomputing.genetic.core;

import java.util.ArrayList;
import java.util.List;

import com.example.softcomputing.genetic.chromosome.Chromosome;
import com.example.softcomputing.genetic.operators.crossover.CrossoverStrategy;
import com.example.softcomputing.genetic.operators.mutation.MutationStrategy;
import com.example.softcomputing.genetic.operators.replacement.Replacement;
import com.example.softcomputing.genetic.operators.selection.SelectionStrategy;
import com.example.softcomputing.utils.AppLogger;
import com.example.softcomputing.utils.FitnessFunction;

public class GeneticAlgorithm<C extends Chromosome<?>> {

    // default
    private int _populationSize;
    private long _MaxGeneration = 100;
    private List<C> _population;

    private SelectionStrategy<C> _selection;
    private CrossoverStrategy<C> _crossover;
    private MutationStrategy<C> _mutation;
    private Replacement<C> _replacement;
    private FitnessFunction<C> _fitnessFunction;
    AppLogger _logger = AppLogger.getLogger(GeneticAlgorithm.class);
    FitnessFunction _fitnessFunction;

    public GeneticAlgorithm(GeneticAlgorithmBuilder<C> builder) {
        this._populationSize = builder.populationSize;
        this._MaxGeneration = builder.maxGenerations;
        this._population = builder.population;
        this._selection = builder.selection;
        this._crossover = builder.crossover;
        this._mutation = builder.mutation;
        this._replacement = builder.replacement;
        this._logger = builder.logger;
        this._fitnessFunction = builder.fitnessFunction;
    }

    public void run() {

        if (_population == null || _population.isEmpty()) {
            _logger.error("No initial population provided. Aborting run.");
            return;
        }

        if (_selection == null || _crossover == null || _mutation == null || _replacement == null) {
            _logger.error("One or more strategies are not configured. Aborting run.");
            return;
        }

        // best for all generations
        C overallBest = null;
        double overallBestFitness = Double.NEGATIVE_INFINITY;

        for (int gen = 1; gen <= _MaxGeneration; gen++) {
            List<C> offspring = new ArrayList<>(_populationSize);

            // selection
            while (offspring.size() < _populationSize) {
                C parent1 = _selection.selectIndividual(_population);
                C parent2 = _selection.selectIndividual(_population);
                // _logger.info("Selected Parents: \n Parent1: " + parent1 + "\n Parent2: " +
                // parent2);

                // crossover
                List<C> children = _crossover.crossover(parent1, parent2);
                // _logger.info("Generated Children after Crossover: " + children);

                // mutation
                for (C child : children) {
                    C mutated = _mutation.mutate(child);
                    offspring.add(mutated);
                    // _logger.info("Mutated Child: " + mutated);
                    if (offspring.size() >= _populationSize)
                        break;
                }
            }

            // replacement
            _population = _replacement.replacePopulation(_population, offspring);

            // best for this generation
            C best = null;
            double bestFitness = Double.NEGATIVE_INFINITY;
            for (C ind : _population) {

                ind.setFitness(_fitnessFunction.evaluate(ind));

                double fitness = ind.getFitness();
                if (best == null || fitness > bestFitness) {
                    best = ind;
                    bestFitness = fitness;
                }
            }

            if (bestFitness > overallBestFitness) {
                overallBest = best;
                overallBestFitness = bestFitness;
            }

            _logger.info("Generation " + gen + " bestFitness=" + bestFitness + " best=" + best);

            if (Double.isFinite(bestFitness) && bestFitness >= Double.POSITIVE_INFINITY - 1)
                break;
        }

        _logger.info("Overall bestFitness=" + overallBestFitness + " best= " + overallBest);
        _logger.info("\n====================================");
        _logger.info("BEST SOLUTION FOUND OVERALL:");
        _logger.info("Best Fitness: " + overallBestFitness);
        _logger.info("Best Chromosome: " + overallBest);
        _logger.info("====================================\n");
    }

    public static <C extends Chromosome<?>> GeneticAlgorithmBuilder<C> builder() {
        return new GeneticAlgorithmBuilder<C>();
    }
}