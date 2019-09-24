package Algorithms;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import Base.Assignment;
import Base.BayesianNetwork;
import Core.RandomVariable;
import Base.Value;
import Util.ArrayMap;

public class PriorSampler {
	
	public PriorSampler() {
		
	}
	
	/*
	 * function PRIOR-SAMPLE(bn) returns an event sampled from the prior specified by bn
	 *	inputs: bn, a Bayesian network specifying joint distribution P(X1, . . . , Xn)
	 */
	public Assignment PriorSample(Core.BayesianNetwork bn){
		
		
		List<RandomVariable> vars = bn.getVariablesSortedTopologically();
		
		// x = an event with n elements
		Assignment event = new Assignment();
		
		//foreach variable Xi in X1, . . . , Xn do
		for(RandomVariable var : vars) {
			

			//x[i] a random sample from P(Xi | parents(Xi))
			
			Iterator iterator = var.getDomain().iterator();
			
			Value randomValue = null;
			
			double random = Math.random();
		
			boolean chosen = false;
			
			while(iterator.hasNext() && !chosen) {
				
				Value value = (Value) iterator.next();
				
				Assignment copy = event.copy();
				
				copy.put(var, value);
								
				double p = bn.getProbability(var, copy);
				
				
				if(random > p) {
					random -= p;
				} else {
					randomValue = value;
					chosen = true;
				}

			}
			
			event.put(var, randomValue);
			
		}
		
		//return x
		return event;
	}
	

}
