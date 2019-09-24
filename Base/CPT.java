package Base;

import java.util.Map;

import Core.Assignment;
import Core.RandomVariable;
import Core.Value;
import Util.ArrayMap;

/**
 * Base implementation of a conditional probability table (CPT)
 * as a Map from Assignments to Distributions.
 * <p>
 * Per AIMA, for finite, discrete distributions, we use a table with one
 * row for every combination of Values for the parents (an Assignment), and
 * a column for each Value of the CPT's variable (a Distribution).
 * <p>
 * CPTs are also used to store prior Distributions for variables with no
 * parents. In that case there is one entry in the map, with an empty
 * Assignment, and probabilities for each of the variable's Values.
 */
public class CPT implements Core.CPT {
	
	protected RandomVariable variable;
	
	public RandomVariable getVariable() {
		return variable;
	}
	
	public CPT(RandomVariable variable) {
		this.variable = variable;
	}
	
	/**
	 * A Map from Assignments to Distributions, used for the ``rows'' of a CPT.
	 */
	protected class AssignmentMap extends ArrayMap<Assignment,Distribution> {
	}
	
	/**
	 * The rows and columns of this CPT.
	 */
	protected AssignmentMap table = new AssignmentMap();
	
	/**
	 * Return the ``row'' (Distribution) for the given Assignment in this CPT.
	 * We can't just use the assignment as the key to find the row, since
	 * it may contain entries for other variables besides just our parents.
	 * Using ArrayMaps for Assignments, the entrySet has to be created from
	 * the underlying ArrayList. The default implementation in AbstractSet
	 * is probably expensive, making this expensive. Sigh.
	 */
	protected Distribution getRowForAssignment(Assignment a) {
		// Can't just do this, unfortunately:
		//return this.table.get(a);
		// Instead iterate through the assignments in our table until we find
		// one that matches (that is, is a subset of) the given one.
		for (Map.Entry<Assignment,Distribution> entry : table.entrySet()) {
			Assignment thisAssignment = entry.getKey();
			if (a.containsAll(thisAssignment)) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Add a ``row'' (Distribution) for the given Assignment to this CPT.
	 */
	protected Distribution addRowForAssignment(Assignment a) {
		Distribution row = new Distribution(this.variable);
		this.table.put(a, row);
		return row;
	}
	
	/**
	 * Set the probability of the given Value for this CPT's RandomVariable,
	 * given the values of its parents in the given Assignment, to the double p.
	 */
	@Override
	public void set(Value value, Assignment assignment, double p) {
		//System.out.format("CPT.set: %s %s=%s : %f\n", assignment, this.variable, value, p);
		Distribution row = getRowForAssignment(assignment);
		if (row == null) {
			row = addRowForAssignment(assignment);
		}
		row.put(value, p);
	}
	
	/**
	 * Return the probability of the given Value for this CPT's RandomVariable,
	 * given the values of its parents in the given Assignment.
	 * Note that the assignment may contain values for variables other than
	 * this CPT's variable (but it must contain at least values for the parents
	 * or an error will occur). 
	 * Throws IllegalArgumentException if there is no entry for the given
	 * Assignment and Value in this CPT.
	 */
	@Override
	public double get(Value value, Assignment assignment) throws IllegalArgumentException {
		Distribution row = getRowForAssignment(assignment);
		if (row == null) {
			throw new IllegalArgumentException(assignment.toString());
		} else {
			return row.get(value);
		}
	}
	
	/**
	 * Return the contents of this CPT as a String.
	 */
	@Override
	public String toString() {
		return this.table.toString();
	}
	
	public static void main(String[] argv) {
		Value a1 = new StringValue("a1");
		Value a2 = new StringValue("a2");
		Value a3 = new StringValue("a3");
		Domain adomain = new Domain();
		adomain.add(a1);
		adomain.add(a2);
		adomain.add(a3);
		RandomVariable A = new NamedVariable("A", adomain);
		System.out.format("%s : %s\n", A, A.getDomain());
		Value b1 = new StringValue("b1");
		Value b2 = new StringValue("b2");
		Domain bdomain = new Domain();
		bdomain.add(b1);
		bdomain.add(b2);
		RandomVariable B = new NamedVariable("B", bdomain);
		System.out.format("%s : %s\n", B, B.getDomain());
		Value x1 = new StringValue("x1");
		Value x2 = new StringValue("x2");
		Value x3 = new StringValue("x3");
		Domain xdomain = new Domain();
		xdomain.add(x1);
		xdomain.add(x2);
		xdomain.add(x3);
		RandomVariable X = new NamedVariable("X", xdomain);
		System.out.format("%s : %s\n", X, X.getDomain());
		CPT cpt = new CPT(X);
		double p = 0.1;
		for (Value ai : A.getDomain()) {
			for (Value bi : B.getDomain()) {
				Assignment assignment = new Base.Assignment();
				assignment.put(A, ai);
				assignment.put(B, bi);
				System.out.println(assignment);
				for (Value xi : X.getDomain()) {
					cpt.set(xi,  assignment,  p);
					p += 0.05;
				}
			}
		}
		System.out.println(cpt);
		// Test lookup
		Assignment a = new Base.Assignment();
		a.put(A, a1);
		a.put(B, b1);
		Value val = x1;
		p = cpt.get(val, a);
		System.out.format("P(%s=%s|%s) = %f\n", cpt.variable, val, a, p);
		// Test lookup with extra variables in evidence
		RandomVariable Z = new NamedVariable("Z", new BooleanDomain());
		System.out.format("%s : %s\n", Z, Z.getDomain());
		a.put(A, a2);
		a.put(B, b2);
		a.put(Z, BooleanValue.TRUE);
		val = x2;
		p = cpt.get(val, a);
		System.out.format("P(%s=%s|%s) = %f\n", cpt.variable, val, a, p);

	}
}
