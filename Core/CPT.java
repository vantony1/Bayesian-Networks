package Core;

/**
 * A conditional probability table (CPT) for a RandomVariable in a
 * BayesianNetwork gives a probability, for each Value in the variable's Domain,
 * for every possible assignment of Values to its parents.
 * So this could be a Map, but I'm not going to define it that way just yet. 
 */
public interface CPT {
	
	/**
	 * Return the RandomVariable for this CPT.
	 */
	public RandomVariable getVariable();
	
	/**
	 * Set the probability of the given Value for this CPT's RandomVariable,
	 * given the values of its parents in the given Assignment, to p.
	 */
	public void set(Value value, Assignment assignment, double p);
	
	/**
	 * Return the probability of the given Value for this CPT's RandomVariable,
	 * given the values of its parents in the given Assignment.
	 * Note that the assignment may contain values for variables other than
	 * this CPT's variable (but it must contain at least values for the parents
	 * or an error will occur). 
	 */
	public double get(Value value, Assignment assignment);
	

}
