package Algorithms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import Base.Assignment;
import Base.Distribution;
import Core.Value;
import Core.RandomVariable;
import Util.ArrayMap;

public class RejectionSampler {
	
	public RejectionSampler() {
		
	}
	
	
	// function REJECTION-SAMPLING(X, e, bn,N) returns an estimate of P(X|e)
	/* inputs: X, the query variable 
	 * e, observed values for variables E
	 * bn, a Bayesian network
	 * N, the total number of samples to be generated
	 */
	public Distribution RejectionSampling(RandomVariable X, Core.Assignment e, Core.BayesianNetwork bn, int N) {
		//local variables: N, a vector of counts for each value of X, initially zero
		Distribution count = new Distribution(X);
		
		ArrayMap<Value, Integer>counts = new ArrayMap(X.getDomain().size());
		
		PriorSampler priorSampler = new PriorSampler();
		
		Iterator iterator = X.getDomain().iterator();
		
		while(iterator.hasNext()) {
			count.put((Value)iterator.next(), 0.0);
		}
		
		//for j = 1 to N do
		for(int i = 0; i < N; i++) {
			
			//x = PRIOR-SAMPLE(bn)
			Assignment a = priorSampler.PriorSample(bn);
			
			//if x is consistent with e then
			if(isConsistent(a, e)) {
				
				//N[x ] = N[x ]+1 where x is the value of X in x
				Value val = a.get(X);
				int c = (int) count.get(val);
				count.put(val, (double) (c+1));
			}
		}
		
		//return NORMALIZE(N)
		count.normalize();
		
		return count;
	}
	
	
	
	//funtion that computes if a is consistent with evidence by
	//ensuring that the common vars have the same value
	public boolean isConsistent(Assignment a, Core.Assignment e) {
		
		if(!a.containsAll(e)) {
			return false;
		}
		
		Set<RandomVariable> vars = e.variableSet();
		
		for(RandomVariable var: vars) {
			
			if(!a.get(var).equals(e.get(var))) {
				return false;
			}
			
		}
		
		return true;
	}

}
