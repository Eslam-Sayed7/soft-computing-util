package softcomputing.genetic.chromosome;

/**
 * Represents a chromosome in a genetic algorithm.
 * <p>
 * A chromosome is a candidate solution to an optimization problem. It consists of genes
 * that encode the solution parameters. This interface provides the basic operations
 * required for genetic algorithms to manipulate and evaluate chromosomes.
 * </p>
 * 
 * @param <G> the type of genes in this chromosome (e.g., Integer, Double, Boolean)
 * 
 * @author Soft Computing Util
 * @version 0.0.1
 * @since 0.0.1
 */
public interface Chromosome<G> {
    
    /**
     * Converts the chromosome's genes to an array.
     * 
     * @return an array containing all genes in this chromosome
     */
    G[] toArray(); // genes

    /**
     * Returns the number of genes in this chromosome.
     * 
     * @return the length of the chromosome
     */
    int length();

    /**
     * Retrieves the gene at the specified position.
     * 
     * @param index the position of the gene to retrieve (0-based)
     * @return the gene at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    G getGene(int index);

    /**
     * Sets the gene at the specified position to a new value.
     * 
     * @param index the position of the gene to set (0-based)
     * @param value the new value for the gene
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    void setGene(int index, G value);

    // double evaluate();

    // double evaluate(ToDoubleFunction<Chromosome<G>> evaluator);

    /**
     * Returns the fitness value of this chromosome.
     * <p>
     * The fitness value represents how well this chromosome solves the problem.
     * Higher fitness values typically indicate better solutions.
     * </p>
     * 
     * @return the fitness value
     */
    double getFitness();

    /**
     * Sets the fitness value of this chromosome.
     * 
     * @param fitness the fitness value to set
     */
    void setFitness(double fitness);

}
