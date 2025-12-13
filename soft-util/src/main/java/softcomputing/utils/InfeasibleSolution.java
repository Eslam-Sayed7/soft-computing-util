package softcomputing.utils;

import softcomputing.genetic.chromosome.Chromosome;

/**
 * Interface for checking if a chromosome represents an infeasible solution.
 * <p>
 * Some optimization problems have constraints that make certain solutions invalid.
 * This interface allows implementing constraint checking to identify infeasible solutions.
 * </p>
 * 
 * @param <C> the chromosome type
 * 
 * @author Soft Computing Util
 * @version 0.0.1
 * @since 0.0.1
 */
public interface InfeasibleSolution<C extends Chromosome<?>> {
    
    /**
     * Checks if the given chromosome represents an infeasible solution.
     * 
     * @param chromosome the chromosome to check
     * @return true if the solution is infeasible (violates constraints), false otherwise
     */
    boolean checkInfeasible(C chromosome);
}
