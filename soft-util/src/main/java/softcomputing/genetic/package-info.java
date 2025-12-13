/**
 * Provides classes and interfaces for implementing genetic algorithms.
 * 
 * <h2>Overview</h2>
 * <p>
 * This package contains a comprehensive framework for creating and running genetic algorithms,
 * a metaheuristic inspired by the process of natural selection. Genetic algorithms are commonly
 * used to generate high-quality solutions to optimization and search problems.
 * </p>
 * 
 * <h2>Main Components</h2>
 * <ul>
 *   <li><b>Core:</b> Contains the main {@link softcomputing.genetic.core.GeneticAlgorithm} 
 *       class and {@link softcomputing.genetic.core.GeneticAlgorithmBuilder} for configuration</li>
 *   <li><b>Chromosome:</b> Defines different chromosome representations (binary, integer, floating-point)</li>
 *   <li><b>Operators:</b> Implements genetic operators including selection, crossover, mutation, and replacement</li>
 *   <li><b>Utils:</b> Utility classes for population initialization and other helper functions</li>
 * </ul>
 * 
 * <h2>Usage Example</h2>
 * <pre>{@code
 * GeneticAlgorithm<BinaryChromosome> ga = new GeneticAlgorithmBuilder<BinaryChromosome>()
 *     .withChromosomeFactory(new BinaryChromosomeFactory())
 *     .withPopulationSize(100)
 *     .withMaxGenerations(1000)
 *     .withSelectionStrategy(new TournamentSelection<>(5))
 *     .withCrossoverStrategy(new SinglePointCrossover<>())
 *     .withMutationStrategy(new BinaryMutation(0.01))
 *     .withReplacementStrategy(new ElitismReplacement<>(10))
 *     .withFitnessFunction(chromosome -> evaluateFitness(chromosome))
 *     .build();
 * 
 * ga.run();
 * }</pre>
 * 
 * @since 0.0.1
 * @version 0.0.1-SNAPSHOT
 */
package softcomputing.genetic;
