package softcomputing.genetic.core;

import java.util.List;
import java.util.Objects;

import softcomputing.genetic.chromosome.Chromosome;
import softcomputing.genetic.chromosome.Factories.ChromosomeFactory;
import softcomputing.genetic.operators.crossover.CrossoverStrategy;
import softcomputing.genetic.operators.mutation.MutationStrategy;
import softcomputing.genetic.operators.replacement.Replacement;
import softcomputing.genetic.operators.selection.SelectionStrategy;
import softcomputing.utils.AppLogger;
import softcomputing.utils.FitnessFunction;

/**
 * Builder class for configuring and creating {@link GeneticAlgorithm} instances.
 * <p>
 * This class provides a fluent API for configuring all aspects of a genetic algorithm,
 * including population parameters, genetic operators, and fitness evaluation.
 * It follows the builder pattern to ensure proper configuration before algorithm execution.
 * </p>
 * 
 * <h2>Usage Example</h2>
 * <pre>{@code
 * GeneticAlgorithm<BinaryChromosome> ga = new GeneticAlgorithmBuilder<BinaryChromosome>()
 *     .withChromosomeFactory(new BinaryChromosomeFactory())
 *     .withPopulationSize(100)
 *     .withGeneLength(20)
 *     .withMaxGenerations(1000)
 *     .withMutationRate(0.01)
 *     .withCrossoverRate(0.7)
 *     .withSelectionStrategy(new TournamentSelection<>(5))
 *     .withCrossoverStrategy(new SinglePointCrossover<>())
 *     .withMutationStrategy(new BinaryMutation(0.01))
 *     .withReplacementStrategy(new ElitismReplacement<>(10))
 *     .withFitnessFunction(chromosome -> evaluateFitness(chromosome))
 *     .withPopulation()  // generates initial population
 *     .build();
 * }</pre>
 * 
 * @param <C> the chromosome type, must extend {@link Chromosome}
 * 
 * @author Soft Computing Util
 * @version 0.0.1
 * @since 0.0.1
 * 
 * @see GeneticAlgorithm
 */
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
    FitnessFunction<C> fitnessFunction;

    AppLogger logger = AppLogger.getLogger(GeneticAlgorithmBuilder.class);

    /**
     * Sets the chromosome factory for creating chromosomes.
     * 
     * @param factory the factory to use for chromosome creation
     * @return this builder instance for method chaining
     */
    public GeneticAlgorithmBuilder<C> withChromosomeFactory(ChromosomeFactory<?, C> factory) {
        this.chromosomeFactory = factory;
        return this;
    }

    /**
     * Sets a pre-initialized population.
     * <p>
     * Use this method if you want to provide a custom initial population instead
     * of generating it automatically with {@link #withPopulation()}.
     * </p>
     * 
     * @param population the initial population to use
     * @return this builder instance for method chaining
     */
    public GeneticAlgorithmBuilder<C> withPopulation(List<C> population) {
        this.population = population;
        return this;
    }

    /**
     * Sets the crossover rate.
     * 
     * @param rate the probability of crossover occurring (typically between 0.6 and 0.9)
     * @return this builder instance for method chaining
     */
    public GeneticAlgorithmBuilder<C> withCrossoverRate(double rate) {
        this.crossoverRate = rate;
        return this;
    }

    /**
     * Sets the mutation rate.
     * 
     * @param rate the probability of mutation for each gene (typically between 0.001 and 0.1)
     * @return this builder instance for method chaining
     */
    public GeneticAlgorithmBuilder<C> withMutationRate(double rate) {
        this.mutationRate = rate;
        return this;
    }

    /**
     * Sets the length of each chromosome's gene sequence.
     * 
     * @param length the number of genes in each chromosome
     * @return this builder instance for method chaining
     */
    public GeneticAlgorithmBuilder<C> withGeneLength(int length) {
        this.geneLength = length;
        return this;
    }

    /**
     * Sets the fitness function for evaluating chromosomes.
     * 
     * @param fitnessFunction the function to evaluate chromosome fitness
     * @return this builder instance for method chaining
     */
    public GeneticAlgorithmBuilder<C> withFitnessFunction(FitnessFunction<C> fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
        return this;
    }

    /**
     * Generates an initial population using the configured chromosome factory.
     * <p>
     * The chromosome factory must be set before calling this method.
     * The population size and gene length should also be configured.
     * </p>
     * 
     * @return this builder instance for method chaining
     * @throws IllegalStateException if chromosome factory is not set
     */
    public GeneticAlgorithmBuilder<C> withPopulation() {
        if (chromosomeFactory == null) {
            throw new IllegalStateException("Chromosome factory must be set before initializing population.");
        }

        List<C> initialPopulation = chromosomeFactory.createPopulation(populationSize, geneLength);
        this.population = initialPopulation;
        return this;
    }

    /**
     * Sets the selection strategy for choosing parents.
     * 
     * @param s the selection strategy to use
     * @return this builder instance for method chaining
     * @see softcomputing.genetic.operators.selection.SelectionStrategy
     */
    public GeneticAlgorithmBuilder<C> withSelectionStrategy(SelectionStrategy<C> s) {
        this.selection = s;
        return this;
    }

    /**
     * Sets the crossover strategy for combining parent chromosomes.
     * 
     * @param c the crossover strategy to use
     * @return this builder instance for method chaining
     * @see softcomputing.genetic.operators.crossover.CrossoverStrategy
     */
    public GeneticAlgorithmBuilder<C> withCrossoverStrategy(CrossoverStrategy<C> c) {
        this.crossover = c;
        return this;
    }

    /**
     * Sets the mutation strategy for introducing variations.
     * 
     * @param m the mutation strategy to use
     * @return this builder instance for method chaining
     * @see softcomputing.genetic.operators.mutation.MutationStrategy
     */
    public GeneticAlgorithmBuilder<C> withMutationStrategy(MutationStrategy<C> m) {
        this.mutation = m;
        return this;
    }

    /**
     * Sets the replacement strategy for updating the population.
     * 
     * @param r the replacement strategy to use
     * @return this builder instance for method chaining
     * @see softcomputing.genetic.operators.replacement.Replacement
     */
    public GeneticAlgorithmBuilder<C> withReplacementStrategy(Replacement<C> r) {
        this.replacement = r;
        return this;
    }

    /**
     * Sets the population size.
     * 
     * @param size the number of individuals in the population
     * @return this builder instance for method chaining
     */
    public GeneticAlgorithmBuilder<C> withPopulationSize(int size) {
        this.populationSize = size;
        return this;
    }

    /**
     * Sets the maximum number of generations.
     * 
     * @param maxGen the maximum number of generations to evolve
     * @return this builder instance for method chaining
     */
    public GeneticAlgorithmBuilder<C> withMaxGenerations(long maxGen) {
        this.maxGenerations = maxGen;
        return this;
    }

    /**
     * Builds and returns a configured {@link GeneticAlgorithm} instance.
     * <p>
     * This method validates that all required configuration is provided before
     * creating the genetic algorithm instance. The following are required:
     * </p>
     * <ul>
     *   <li>Selection strategy</li>
     *   <li>Crossover strategy</li>
     *   <li>Mutation strategy</li>
     *   <li>Replacement strategy</li>
     *   <li>Chromosome factory</li>
     *   <li>Fitness function</li>
     *   <li>Population size greater than 0</li>
     * </ul>
     * 
     * @return a new configured {@link GeneticAlgorithm} instance
     * @throws NullPointerException if any required strategy or function is null
     * @throws IllegalArgumentException if population size is not positive
     */
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

        return new GeneticAlgorithm<>(this);
    }
}
