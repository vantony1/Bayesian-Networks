package Algorithms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import Base.Distribution;
import Base.BayesianNetwork.Node;
import Core.Assignment;
import Core.CPT;
import Core.RandomVariable;
import Core.Value;
import Util.ArrayMap;
import Util.ArraySet;
import Base.WeightSample;

public class LikelihoodSampler {

	
	public LikelihoodSampler() {

	}
	
	//function LIKELIHOOD-WEIGHTING(X, e, bn,N) returns an estimate of P(X|e)
	/*
	 * inputs: X, the query variable
	 * e, observed values for variables E
	 * bn, a Bayesian network specifying joint distribution P(X1, . . . , Xn)
	 * N, the total number of samples to be generated
	 */
	public Distribution LikelihoodWeighting(RandomVariable X, Core.Assignment e, Core.BayesianNetwork bn, int N) {
		
		//local variables: W, a vector of weighted counts for each value of X, initially zero
		
		Distribution weights = new Distribution(X);
						
		Iterator iterator = X.getDomain().iterator();
				
		while(iterator.hasNext()) {
			weights.put((Value)iterator.next(), 0.0);
		}
		
		//for j = 1 to N do
		for(int i = 0; i < N; i++) {
			
			//x,w = WEIGHTED-SAMPLE(bn, e)
			WeightSample WS = WeightedSample(bn, e);
			
			//W[x] = W[x] + w where x is the value of X in x
			Assignment Y = WS.getE();
			double w = WS.getW();
			
			Value val = Y.get(X);
			
			double c = (double) weights.get(val);
			weights.put(val, (double) (c + w));
			
			
			
		}
		
		//return NORMALIZE(W)
		weights.normalize();
		
		return weights;
	}
	
	
	//function WEIGHTED-SAMPLE(bn, e) returns an event and a weight
	public WeightSample WeightedSample(Core.BayesianNetwork bn, Core.Assignment e){
		
		
		//w = 1; x = an event with n elements initialized from e
		double w = 1.0;
		Assignment x = e.copy();
		
		List<RandomVariable> vars = bn.getVariablesSortedTopologically();

		//foreach variable Xi in X1, . . . , Xn do
		for(RandomVariable var: vars) {
			//if Xi is an evidence variable with value xi in e
			if(e.containsKey(var)) {
				//then w = w × P(Xi = xi | parents(Xi))
				w = w * bn.getProbability(var, x);
			} else {
				
				//else x[i] = a random sample from P(Xi | parents(Xi))
				Iterator iterator = var.getDomain().iterator();
				
				Value randomValue = null;
				
				double random = Math.random();
			
				boolean chosen = false;
				
				while(iterator.hasNext() && !chosen) {
					
					Value value = (Value) iterator.next();
					
					Assignment copy = x.copy();
					
					copy.put(var, value);
									
					double p = bn.getProbability(var, copy);
					
					
					if(random > p) {
						random -= p;
					} else {
						randomValue = value;
						chosen = true;
					}

				}
				
				x.put(var, randomValue);
			}
			
			
		}
		
		
		//return x, w
		WeightSample WS = new WeightSample(x, w);
		
		return WS;
		
	}
	

	
	
}
