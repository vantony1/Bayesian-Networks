/*
 * File: Distribution.java
 * Creator: George Ferguson
 * Created: Mon Mar 26 13:15:54 2012
 * Time-stamp: <Wed Mar 28 15:06:37 EDT 2012 ferguson>
 */

package Base;

import java.util.Map;

import Core.RandomVariable;
import Core.Value;
import Util.ArrayMap;

/**
 * Base implementation of a Distribution as an ArrayMap from Values to Doubles.
 * AIMA uses arrays indexed by the values of the variable. We use
 * a Map whose keys are the values. One could instead map the values
 * to integer indexes (by imposing an ordering on them), and then use
 * an array of doubles to represent the distribution. That might be
 * nice since one can't put primitive doubles in a Map...
 */
public class Distribution extends ArrayMap<Value,Double> implements Core.Distribution {
	
	protected RandomVariable variable;
	
	public RandomVariable getVariable() {
		return this.variable;
	}

	/**
	 * Construct and return a new empty Distribution for the given RandomVariable.
	 * Note that we don't actually enforce that the values in the Distribution
	 * are only those for the RandomVariable. But anyway, this is handy.
	 */
	public Distribution(RandomVariable X) {
		super(X.getDomain().size());
		this.variable = X;
	}

	/**
	 * Set the probability for the given Value in this Distribution.
	 */
	@Override
	public void set(Value value, double probability) {
		put(value, new Double(probability));
	}

	/**
	 * Return the probability for the given Value in this Distribution.
	 */
	@Override
	public double get(Value value) {
		Double p = super.get(value);
		if (p == null) {
			throw new IllegalArgumentException(value.toString());
		} else {
			return p.doubleValue();
		}
	}

	/**
	 * Normalize this distribution so that the probabilities add up to 1.
	 */
	@Override
	public void normalize() {
		double sum = 0.0;
		for (Double value : values()) {
			sum += value.doubleValue();
		}
		// Avoid concurrent modification exceptions by modifying the entries directly
		for (Map.Entry<Value,Double> entry : this.entrySet()) {
			entry.setValue(entry.getValue()/sum); 
		}
	}

	/**
	 * Test Distributions.
	 */
	public static void main(String[] argv) {
		Value v1 = new StringValue("v1");
		Value v2 = new StringValue("v2");
		Value v3 = new StringValue("v3");
		Domain domain = new Domain();
		domain.add(v1);
		domain.add(v2);
		domain.add(v3);
		RandomVariable V = new NamedVariable("V", domain); 
		Distribution dist = new Distribution(V);
		dist.set(v1, 0.4);
		dist.set(v2, 0.3);
		dist.set(v3, 0.1);
		System.out.println(dist);
		dist.normalize();
		System.out.println(dist);
	}
}
