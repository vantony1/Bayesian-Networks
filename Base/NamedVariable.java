package Base;

import Core.Domain;
import Core.Named;
import Core.RandomVariable;

/**
 * Base implementation of a RandomVariable that has a name as well
 * as its Domain of Values.
 */
public class NamedVariable implements RandomVariable, Named {
	
	protected String name;
	protected Domain domain;
	
	public NamedVariable(String name, Domain domain) {
		this.name = name;
		this.domain = domain;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public Domain getDomain() {
		return this.domain;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	/*
	 * Not sure this is needed since we don't actually compare RandomVariables
	 * to each other using equals(). So the default hashCode() implementation
	 * may be sufficient.
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	 */
	
	// Testing
	
	public static void main(String[] argv) {
		StringValue red = new StringValue("red");
		StringValue green = new StringValue("green");
		StringValue blue = new StringValue("blue");
		Base.Domain domain = new Base.Domain();
		domain.add(red);
		domain.add(green);
		domain.add(blue);
		NamedVariable v1 = new NamedVariable("Color", domain);
		System.out.format("%s : %s\n", v1, v1.getDomain());
		Base.Domain booleans = new Base.Domain(BooleanValue.TRUE, BooleanValue.FALSE);
		NamedVariable v2 = new NamedVariable("IsFurry", booleans);
		System.out.format("%s : %s\n", v2, v2.getDomain());
	}

}
