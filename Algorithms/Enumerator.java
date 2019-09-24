package Algorithms;

import Base.Distribution;
import Base.NamedVariable;
import Base.Value;
import Core.RandomVariable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import Base.Assignment;
import Base.BayesianNetwork;
import Base.BayesianNetwork.Node;



public class Enumerator {

	public Enumerator() {
		// TODO Auto-generated constructor stub
	}
	
	
	/*
	 * function ELIMINATION-ASK(X, e, bn) returns a distribution over X
	 * inputs: X, the query variable e, observed values for variables E
     * bn, a Bayes net with variables {X} E Y /* Y = hidden variables 
	 */
	
	public Distribution EnumerationAsk(RandomVariable X, Core.Assignment e, Core.BayesianNetwork bn){
		

		//Q(X) a distribution over X, initially empty

		Distribution Q = new Distribution(X);
		
		//for each value xi of X do
		Iterator iterator = X.getDomain().iterator();
		
		while(iterator.hasNext()) {
			
			Core.Assignment copy = e.copy();
			
			Value d = (Value) iterator.next();

			List<RandomVariable> vars = bn.getVariablesSortedTopologically();
			
			copy.put(X, d);
			
			//Q(xi) = ENUMERATE-ALL(bn.VARS, exi )
			//where exi is e extended with X = xi
			Q.put(d, EnumerateAll(bn, vars, copy));
			
		}
		
		//Normalizing Q 
		Q.normalize();
				
		return Q;
	}
	
	
	//function ENUMERATE-ALL(vars, e) returns a real number
	public double EnumerateAll(Core.BayesianNetwork network, List<RandomVariable> vars, Core.Assignment e) {
		
		//if EMPTY?(vars) then return 1.0
		if(vars.isEmpty()) {
			return 1.0;
		}
		
		//Y = FIRST(vars)
		RandomVariable Y = vars.get(0);
		
		//if Y has value y in e
		if(e.variableSet().contains(Y)) {
			//then return P(y | parents(Y )) × ENUMERATE-ALL(REST(vars), e)
			
			double p = network.getProbability(Y, e);
			 
			return p * EnumerateAll(network, vars.subList(1, vars.size()), e);
		} else {
			
			//else returny P(y | parents(Y )) × ENUMERATE-ALL(REST(vars), ey)
			//where ey is e extended with Y = y
			
			double sum = 0;
			
			Iterator iterator = Y.getDomain().iterator();
			
			
			while(iterator.hasNext()) {
				Value value = (Value) iterator.next();
				
				Core.Assignment copy = e.copy();
				
				copy.put(Y, value);

				sum += network.getProbability(Y, copy) * EnumerateAll(network, vars.subList(1, vars.size()), copy);

			}
			return sum;
		}
			
		
	}

}
