package Core;

import java.util.Iterator;

/**
 * The domain of a RandomVariable is a set of Values.
 * <p>
 * This is a subset of the Set interface (we could just extend Set\&lt;Value&gt;
 * instead). Note that in the Set interface, contains() and remove() take
 * Objects as parameters,rather than whatever the element type is (Value in
 * this case). Not sure why that is...
 * @see https://stackoverflow.com/questions/104799/why-arent-java-collections-remove-methods-generic
 */
public interface Domain extends Iterable<Value> {
	
	public boolean add(Value value);
	
	public boolean contains(Object obj);
	
	public boolean remove(Object obj);
	
	public void clear();
	
	public boolean isEmpty();
	
	public int size();
	
	public Iterator<Value> iterator();

}
