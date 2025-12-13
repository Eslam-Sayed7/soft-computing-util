package softcomputing.genetic.core;

import java.util.ArrayList;
import java.util.List;

import softcomputing.genetic.chromosome.Chromosome;
import softcomputing.genetic.operators.crossover.CrossoverStrategy;
import softcomputing.genetic.operators.mutation.MutationStrategy;
import softcomputing.genetic.operators.replacement.Replacement;
import softcomputing.genetic.operators.selection.SelectionStrategy;
import softcomputing.utils.AppLogger;
import softcomputing.utils.FitnessFunction;

/**
 * Main class for running genetic algorithms.
 * <p>
 * This class implements a standard genetic algorithm that evolves a population of chromosomes
 * over multiple generations to find optimal or near-optimal solutions to problems. The algorithm
 * uses configurable strategies for selection, crossover, mutation, and replacement operations.
 * </p>
 * 
 * <h2>Algorithm Flow</h2>
 * <ol>
 *   <li>Initialize population with candidate solutions</li>
 *   <li>Evaluate fitness of all individuals</li>
 *   <li>For each generation:
 *     <ul>
 *       <li>Select parents based on fitness</li>
 *       <li>Apply crossover to create offspring</li>
 *       <li>Apply mutation to offspring</li>
 *       <li>Replace old population with new population</li>
 *       <li>Track best solution found</li>
 *     </ul>
 *   </li>
 *   <li>Return best solution after all generations</li>
 * </ol>
 * 
 * @param <C> the chromosome type, must extend {@link Chromosome}
 * 
 * @author Soft Computing Util
 * @version 0.0.1
 * @since 0.0.1
 * 
 * @see GeneticAlgorithmBuilder
 * @see Chromosome
 */
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

    /**
     * Constructs a genetic algorithm with the specified configuration.
     * <p>
     * This constructor is typically called by {@link GeneticAlgorithmBuilder}
     * and should not be invoked directly. Use the builder pattern instead.
     * </p>
     * 
     * @param builder the builder containing the algorithm configuration
     * @see GeneticAlgorithmBuilder
     */
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

    /**
     * Executes the genetic algorithm.
     * <p>
     * This method runs the evolutionary process for the configured number of generations.
     * In each generation, it performs selection, crossover, mutation, and replacement operations
     * to evolve the population. The algorithm tracks and logs the best solution found in each
     * generation and overall.
     * </p>
     * 
     * The algorithm will terminate early if:
     * <ul>
     *   <li>The maximum number of generations is reached</li>
     *   <li>A solution with near-infinite fitness is found</li>
     * </ul>
     * 
     * @throws IllegalStateException if population is null/empty or strategies are not configured
     */
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

    /**
     * Creates a new builder for configuring a genetic algorithm.
     * <p>
     * This is the recommended way to create a {@link GeneticAlgorithm} instance.
     * The builder provides a fluent API for configuring all aspects of the algorithm.
     * </p>
     * 
     * @param <C> the chromosome type
     * @return a new {@link GeneticAlgorithmBuilder} instance
     * 
     * @see GeneticAlgorithmBuilder
     */
    public static <C extends Chromosome<?>> GeneticAlgorithmBuilder<C> builder() {
        return new GeneticAlgorithmBuilder<>();
    }
}