package softcomputing.genetic.operators.crossover;
import java.util.List;

/**
 * Strategy interface for performing crossover operations in a genetic algorithm.
 * <p>
 * Crossover (also called recombination) combines genetic material from two parent
 * chromosomes to create offspring. This is a crucial operator for sharing information
 * between different solutions and exploring the solution space.
 * </p>
 * 
 * <h2>Common Implementations</h2>
 * <ul>
 *   <li><b>Single-Point Crossover:</b> Selects one crossover point and swaps genes after it</li>
 *   <li><b>Two-Point Crossover:</b> Selects two points and swaps the genes between them</li>
 *   <li><b>Uniform Crossover:</b> Each gene is randomly selected from one of the parents</li>
 * </ul>
 * 
 * @param <Chromosome> the chromosome type
 * 
 * @author Soft Computing Util
 * @version 0.0.1
 * @since 0.0.1
 * 
 * @see softcomputing.genetic.operators.crossover.SinglePointCrossover
 * @see softcomputing.genetic.operators.crossover.TwoPointCrossover
 * @see softcomputing.genetic.operators.crossover.UniformCrossover
 */
public interface CrossoverStrategy<Chromosome> {
	
	/**
	 * Performs crossover between two parent chromosomes to produce offspring.
	 * 
	 * @param parent1 the first parent chromosome
	 * @param parent2 the second parent chromosome
	 * @return a list of offspring chromosomes (typically 1 or 2 children)
	 * @throws IllegalArgumentException if parents are null or incompatible
	 */
	List<Chromosome> crossover(Chromosome parent1, Chromosome parent2);
}
