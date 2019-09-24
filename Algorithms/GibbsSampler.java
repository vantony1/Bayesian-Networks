package Algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import Base.Assignment;
import Base.Distribution;
import Core.BayesianNetwork;
import Core.RandomVariable;
import Core.Value;

public class GibbsSampler {
	
	public GibbsSampler() {
		
	}

	/*function GIBBS-ASK(X, e, bn,N) returns an estimate of P(X|e)
	 * local variables: N, a vector of counts for each value of X, initially zero
	 * Z, the nonevidence variables in bn
	 * x, the current state of the network, initially copied from e
	 */
	public Distribution GibbsAsk(RandomVariable X, Core.Assignment e, Core.BayesianNetwork bn, int N) {
		
		//initialize x with random values for the variables in Z
		Distribution count = new Distribution(X);

		Iterator iterator = X.getDomain().iterator();
		
		while(iterator.hasNext()) {
			count.put((Value)iterator.next(), 0.0);
		}
		
		List<RandomVariable> vars  = bn.getVariablesSortedTopologically();
		
		Assignment x = (Assignment) e.copy();
		
		List<RandomVariable> ZVars = new ArrayList<>();
		
		//initializing non evidence vars by random sampling
		for(RandomVariable var: vars) {
			if(!e.containsKey(var)) {
				ZVars.add(var);
				Iterator iter = var.getDomain().iterator();
				
				Value randomValue = null;
				
				double random = Math.random();
			
				boolean chosen = false;
				
				while(iter.hasNext() && !chosen) {
					
					Value value = (Value) iter.next();
					
					Assignment copy = (Assignment) x.copy();
					
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
		
		//for j = 1 to N do
		for(int i = 0; i < N; i++) {
			
			//for each Zi in Z do
			for(RandomVariable ZVar : ZVars) {
		
				//set the value of Zi in x by sampling from P(Zi|mb(Zi))
				Iterator iter = ZVar.getDomain().iterator();
				
				Value randomValue = null;
				
				Distribution dist = MarkovBlanketDist(ZVar, bn, x);
				
				double random = Math.random();
			
				boolean chosen = false;
				
				while(iter.hasNext() && !chosen) {
					
					Value value = (Value) iter.next();		
									
					double p = dist.get(value);
					
					if(random > p) {
						random -= p;
					} else {
						randomValue = value;
						chosen = true;
					}

				}
				
				x.put(ZVar, randomValue);
				
				//N[x ] = N[x] + 1 where x is the value of X in x
				Value val = x.get(X);
				int c = (int) count.get(val);
				count.put(val, (double) (c+1));
				
				
			}
		}
	
		//Normalize and return N
		count.normalize();
		return count;
		
	}
	
	
	/*Function that returns a variable distribution over its markov blanket
	 * by using formula 14.12 AIMA 
	 */
	public Distribution MarkovBlanketDist(RandomVariable X, BayesianNetwork bn, Core.Assignment x){
		
		Distribution dist = new Distribution(X);

		Set<RandomVariable> children = bn.getChildren(X);

		Iterator iterator = X.getDomain().iterator();
		
		while(iterator.hasNext()) {
			
		Value val = (Value) iterator.next();
		
		Assignment copy = (Assignment) x.copy();
			
		copy.put(X, val);

		double p = bn.getProbability(X, copy);
		
		for(RandomVariable child : children) {
			
			p = p * bn.getProbability(child, copy);
		}
		
		dist.put(val, p);
		
		}
		
		
		dist.normalize();
		
		return dist;
		
	}
	
	
	/* ATTEMPT AT MarkovBlanket which makes absolutely no sense;
	 * public Assignment MarkovBlanket(RandomVariable X, BayesianNetwork bn,
	 * Core.Assignment e) {
	 * 
	 * 
	 * Set<RandomVariable> parents = bn.getParents(X); Set<RandomVariable> children
	 * = bn.getChildren(X); Set<RandomVariable> childrenparents = new
	 * HashSet<RandomVariable>();
	 * 
	 * for(RandomVariable child: children) { Set<RandomVariable> holder =
	 * bn.getChildren(child);
	 * 
	 * childrenparents.addAll(holder);
	 * 
	 * }
	 * 
	 * Assignment a = new Assignment();
	 * 
	 * for(RandomVariable var : parents) { a.put(var, e.get(var)); }
	 * 
	 * for(RandomVariable var : children) { a.put(var, e.get(var)); }
	 * 
	 * for(RandomVariable var : childrenparents) { a.put(var, e.get(var)); }
	 * 
	 * return a;
	 * 
	 * }
	 */
}
