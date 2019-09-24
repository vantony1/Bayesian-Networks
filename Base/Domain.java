package Base;

import java.util.Collection;

import Core.Value;
import Util.ArraySet;

/**
 * Base implementation of a Domain as an ArraySet of Values. 
 * We use a ArraySet here since iteration is the main use for Domains
 * in Bayesian network algorithms.
 */
public class Domain extends ArraySet<Value> implements Core.Domain {

	public static final long serialVersionUID = 1L;

	public Domain() {
		super();
	}

	public Domain(int size) {
		super(size);
	}

	public Domain(Value... values) {
		this();
		for (Value value : values) {
			this.add(value);
		}
	}

	public Domain(Collection<Value> collection) {
		this();
		for (Value value : collection) {
			this.add(value);
		}
	}

	public static void main(String[] argv) {
		StringValue red = new StringValue("red");
		StringValue green = new StringValue("green");
		StringValue blue = new StringValue("blue");
		Domain domain = new Domain();
		domain.add(red);
		domain.add(green);
		domain.add(blue);
		System.out.println(domain);
		Domain booleans = new Domain(BooleanValue.TRUE, BooleanValue.FALSE);
		System.out.println(booleans);
	}
}
