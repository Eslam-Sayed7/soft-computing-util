package softcomputing.genetic.operators.mutation;
import softcomputing.genetic.chromosome.Chromosome;

/**
 * Strategy interface for performing mutation operations in a genetic algorithm.
 * <p>
 * Mutation introduces random variations into chromosomes to maintain genetic diversity
 * and enable exploration of new areas of the solution space. It helps prevent premature
 * convergence to local optima.
 * </p>
 * 
 * <h2>Common Implementations</h2>
 * <ul>
 *   <li><b>Binary Mutation:</b> Flips bits in binary chromosomes with a given probability</li>
 *   <li><b>Integer Mutation:</b> Randomly changes integer genes within their valid range</li>
 *   <li><b>Uniform Mutation:</b> Replaces genes with random values from a uniform distribution</li>
 * </ul>
 * 
 * <p>
 * The mutation rate is typically low (e.g., 0.001 to 0.1) to avoid disrupting good solutions
 * while still maintaining diversity.
 * </p>
 * 
 * @param <C> the chromosome type
 * 
 * @author Soft Computing Util
 * @version 0.0.1
 * @since 0.0.1
 * 
 * @see softcomputing.genetic.operators.mutation.BinaryMutation
 * @see softcomputing.genetic.operators.mutation.IntegerMutation
 * @see softcomputing.genetic.operators.mutation.UniformMutation
 */
public interface MutationStrategy<C extends Chromosome<?>> {
	
	/**
	 * Applies mutation to a chromosome.
	 * 
	 * @param individual the chromosome to mutate
	 * @return the mutated chromosome (may be a new instance or the modified original)
	 * @throws IllegalArgumentException if individual is null
	 */
	C mutate(C individual);
}
