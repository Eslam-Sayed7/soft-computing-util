/**
 * Provides chromosome representations and factories for genetic algorithms.
 * <p>
 * This package contains different chromosome implementations that encode solutions
 * in various formats, and factory classes for creating and initializing populations.
 * </p>
 * 
 * <h2>Chromosome Types</h2>
 * <ul>
 *   <li>{@link softcomputing.genetic.chromosome.BinaryChromosome} - 
 *       For binary-encoded solutions (arrays of 0s and 1s)</li>
 *   <li>{@link softcomputing.genetic.chromosome.IntegerChromosome} - 
 *       For integer-valued solutions</li>
 *   <li>{@link softcomputing.genetic.chromosome.FloatingPointChromosome} - 
 *       For real-valued solutions</li>
 * </ul>
 * 
 * <h2>Usage</h2>
 * <p>
 * Choose the chromosome type that best represents your problem domain.
 * For optimization problems with continuous variables, use {@code FloatingPointChromosome}.
 * For combinatorial problems or binary decisions, use {@code BinaryChromosome}.
 * For discrete integer problems, use {@code IntegerChromosome}.
 * </p>
 * 
 * @since 0.0.1
 * @version 0.0.1-SNAPSHOT
 */
package softcomputing.genetic.chromosome;
