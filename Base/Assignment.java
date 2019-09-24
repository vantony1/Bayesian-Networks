/*
 * File: Assignment.java
 * Creator: George Ferguson
 * Created: Tue Mar 27 13:03:29 2012
 * Time-stamp: <Wed Apr  3 18:38:46 EDT 2013 ferguson>
 */

package Base;

import java.util.Set;
import java.util.Collection;
import java.util.Map;

import Core.RandomVariable;
import Core.Value;
import Util.ArrayMap;

/**
 * Base implementation of an Assignment as an ArrayMap from
 * RandomVariables to Values.
 * @see bn.core.Assignment
 * @see bn.util.ArrayMap
 */
public class Assignment extends ArrayMap<RandomVariable,Value> implements Core.Assignment {

	public Assignment() {
		super();
	}

	/**
	 * Return a Set view of the RandomVariables in this Assignment.
	 * @see Map.keySet()
	 */
	public Set<RandomVariable> variableSet() {
		return this.keySet();
	}

	/**
	 * Return true if this Assignment contains all the assignments
	 * in the given other Assignment. That is, the other Assignment is
	 * a subset of this one (or they are equal).
	 * <p>
	 * Maps are not Collections according to Java, so we need to implement this
	 * method ourselves. This is quadratic time but at least no memory when
	 * using ArrayMaps (which are ArraySets, themselves ArrayLists, of
	 * Map.Entrys).
	 * @see ArrayMap.entrySet
	 * @see ArraySet.iterator
	 * @see ArrayList.iterator
	 */
	@Override
	public boolean containsAll(Core.Assignment other) {
		Set<Map.Entry<RandomVariable,Value>> ourEntries = this.entrySet();
		Set<Map.Entry<RandomVariable,Value>> theirEntries = other.entrySet();
		return ourEntries.containsAll(theirEntries);
	}
	
	/**
	 * Return a shallow copy of this Assignment (that is, an Assignment that
	 * contains the same assignments without copying the RandomVariables or
	 * Values).
	 */
	public Assignment copy() {
		Assignment result = new Assignment();
		for (Map.Entry<RandomVariable, Value> entry : this.entrySet()) {
			RandomVariable var = entry.getKey();
			Value val = entry.getValue();
			result.put(var, val);
		}
		return result;
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
		Value b1 = new StringValue("b1");
		Value b2 = new StringValue("b2");
		Domain bdomain = new Domain();
		bdomain.add(b1);
		bdomain.add(b2);
		RandomVariable B = new NamedVariable("B", bdomain); 
		Assignment assignment = new Assignment();
		assignment.put(A, a1);
		assignment.put(B, b1);
		System.out.println(assignment);
		assignment.put(B, b2);
		System.out.println(assignment);
		assignment.put(A, a3);
		System.out.println(assignment);
		Assignment assignment2 = new Assignment();
		assignment2.put(A, a3);
		System.out.format("%s containsAll %s? %s\n", assignment, assignment2,
				assignment.containsAll(assignment2));
		System.out.format("%s containsAll %s? %s\n", assignment2, assignment,
				assignment2.containsAll(assignment));
		System.out.format("%s equals %s? %s\n", assignment, assignment2,
				assignment.equals(assignment2));
		assignment2.put(B, b2);
		System.out.format("%s equals %s? %s\n", assignment, assignment2,
				assignment.equals(assignment2));
	}


}
