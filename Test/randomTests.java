package Test;

import java.util.Set;

import Algorithms.PriorSampler;
import Base.BooleanDomain;
import Base.BooleanValue;
import Base.NamedVariable;
import Core.Assignment;
import Core.BayesianNetwork;
import Core.CPT;
import Core.RandomVariable;
import Util.ArraySet;

public class randomTests {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RandomVariable B = new NamedVariable("B", new BooleanDomain());
		RandomVariable E = new NamedVariable("E", new BooleanDomain());
		RandomVariable A = new NamedVariable("A", new BooleanDomain());
		RandomVariable J = new NamedVariable("J", new BooleanDomain());
		RandomVariable M = new NamedVariable("M", new BooleanDomain());
		BayesianNetwork bn = new Base.BayesianNetwork();
		bn.add(B);
		bn.add(E);
		bn.add(A);
		bn.add(J);
		bn.add(M);
		// Shorthands
		BooleanValue TRUE = BooleanValue.TRUE;
		BooleanValue FALSE = BooleanValue.FALSE;
		Assignment a;

		// B (no parents)
		CPT Bprior = new Base.CPT(B);
		a = new Base.Assignment();
		Bprior.set(TRUE, a, 0.3);
		Bprior.set(FALSE, a, 1-0.3);
		bn.connect(B, new ArraySet<RandomVariable>() , Bprior);

		// E (no parents)
		CPT Eprior = new Base.CPT(E);
		a = new Base.Assignment();
		Eprior.set(TRUE, a, 0.5);
		Eprior.set(FALSE, a, 1-0.5);
		bn.connect(E, new ArraySet<RandomVariable>() , Eprior);

		// B,E -> A
		Set<RandomVariable> BE = new ArraySet<RandomVariable>();
		BE.add(B);
		BE.add(E);
		CPT AgivenBE = new Base.CPT(A);
		a = new Base.Assignment();
		a.put(B, TRUE);
		a.put(E, TRUE);
		AgivenBE.set(TRUE, a, 0.75);
		AgivenBE.set(FALSE, a, 1-0.75);
		a = new Base.Assignment();
		a.put(B, TRUE);
		a.put(E, FALSE);
		AgivenBE.set(TRUE, a, 0.64);
		AgivenBE.set(FALSE, a, 1-0.64);
		a = new Base.Assignment();
		a.put(B, FALSE);
		a.put(E, TRUE);
		AgivenBE.set(TRUE, a, 0.29);
		AgivenBE.set(FALSE, a, 1-0.29);
		a = new Base.Assignment();
		a.put(B, FALSE);
		a.put(E, FALSE);
		AgivenBE.set(TRUE, a, 0.1);
		AgivenBE.set(FALSE, a, 1-0.1);
		bn.connect(A, BE, AgivenBE);

		// A -> J
		Set<RandomVariable> justA = new ArraySet<RandomVariable>();
		justA.add(A);
		CPT JgivenA = new Base.CPT(J);
		a = new Base.Assignment();
		a.put(A, TRUE);
		JgivenA.set(TRUE, a, 0.9);
		JgivenA.set(FALSE, a, 1-0.9);
		a = new Base.Assignment();
		a.put(A, FALSE);
		JgivenA.set(TRUE, a, 0.05);
		JgivenA.set(FALSE, a, 1-0.05);
		bn.connect(J, justA, JgivenA);

		// A -> M
		CPT MgivenA = new Base.CPT(M);
		a = new Base.Assignment();
		a.put(A, TRUE);
		MgivenA.set(TRUE, a, 0.7);
		MgivenA.set(FALSE, a, 1-0.8);
		a = new Base.Assignment();
		a.put(A, FALSE);
		MgivenA.set(TRUE, a, 0.01);
		MgivenA.set(FALSE, a, 1-0.01);
		bn.connect(M, justA, MgivenA);
		
		PriorSampler pSampler = new PriorSampler();
		
		Assignment r = pSampler.PriorSample(bn);

		System.out.print(r.toString());
	}

}
