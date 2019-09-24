package Test;

import java.util.Set;

import Core.Assignment;
import Core.BayesianNetwork;
import Core.CPT;
import Core.Distribution;
import Core.Inferencer;
import Core.RandomVariable;
import Algorithms.Enumerator;
import Algorithms.GibbsSampler;
import Algorithms.LikelihoodSampler;
import Algorithms.RejectionSampler;
import Base.BooleanDomain;
import Base.BooleanValue;
import Base.NamedVariable;
import Util.ArraySet;

/**
 * The AIMA Burglar Alarm example of a BayesianNetwork (AIMA Fig 14.2).
 * <p>
 * P(B|j,m) = \alpha &lt;0.00059224,0.0014919&gt; ~= &lt;0.284,0.716&gt; (p. 524)
 */
public class AIMA_Alarm {
	
	public static void main(String[] args) {
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
		Bprior.set(TRUE, a, 0.001);
		Bprior.set(FALSE, a, 1-0.001);
		bn.connect(B, new ArraySet<RandomVariable>() , Bprior);

		// E (no parents)
		CPT Eprior = new Base.CPT(E);
		a = new Base.Assignment();
		Eprior.set(TRUE, a, 0.002);
		Eprior.set(FALSE, a, 1-0.002);
		bn.connect(E, new ArraySet<RandomVariable>() , Eprior);

		// B,E -> A
		Set<RandomVariable> BE = new ArraySet<RandomVariable>();
		BE.add(B);
		BE.add(E);
		CPT AgivenBE = new Base.CPT(A);
		a = new Base.Assignment();
		a.put(B, TRUE);
		a.put(E, TRUE);
		AgivenBE.set(TRUE, a, 0.95);
		AgivenBE.set(FALSE, a, 1-0.95);
		a = new Base.Assignment();
		a.put(B, TRUE);
		a.put(E, FALSE);
		AgivenBE.set(TRUE, a, 0.94);
		AgivenBE.set(FALSE, a, 1-0.94);
		a = new Base.Assignment();
		a.put(B, FALSE);
		a.put(E, TRUE);
		AgivenBE.set(TRUE, a, 0.29);
		AgivenBE.set(FALSE, a, 1-0.29);
		a = new Base.Assignment();
		a.put(B, FALSE);
		a.put(E, FALSE);
		AgivenBE.set(TRUE, a, 0.001);
		AgivenBE.set(FALSE, a, 1-0.001);
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
		
		System.out.println(bn);
		
		//Creating enumertor and other three samplers and producing output
		System.out.println("P(B|j,m) = \\alpha <0.00059224,0.0014919> ~= <0.284,0.716>");
		Enumerator exact = new Enumerator();
		LikelihoodSampler lSampler = new LikelihoodSampler();
		RejectionSampler rSampler = new RejectionSampler();
		GibbsSampler gSampler = new GibbsSampler();
		a = new Base.Assignment();
		a.put(J, TRUE);
		a.put(M, TRUE);
		Distribution dist = exact.EnumerationAsk(B, a, bn);
		System.out.println(dist);
		Distribution dist_L = lSampler.LikelihoodWeighting(B, a, bn, 100000);
		System.out.println(dist_L);
		System.out.println("REjection");

		Distribution dist_R = rSampler.RejectionSampling(B, a, bn, 100000);
		System.out.println(dist_R);
		Distribution dist_G = gSampler.GibbsAsk(B, a, bn, 100000);
		System.out.println(dist_G);

	}
	

}
