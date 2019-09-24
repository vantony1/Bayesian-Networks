package Test;

import java.util.Set;

import Base.BooleanDomain;
import Base.BooleanValue;
import Base.NamedVariable;
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
import Util.ArraySet;

/**
 * The AIMA WetGrass example of a BayesianNetwork (AIMA Fig. 14.12).
 * <p>
 * P(Rain|Sprinkler=true) = &lt;0.3,0.7&gt; (p. 532)
 */
public class AIMA_WetGrass {

	public static void main(String[] args) {
		RandomVariable C = new NamedVariable("C", new BooleanDomain());
		RandomVariable S = new NamedVariable("S", new BooleanDomain());
		RandomVariable R = new NamedVariable("R", new BooleanDomain());
		RandomVariable W = new NamedVariable("W", new BooleanDomain());
		BayesianNetwork bn = new Base.BayesianNetwork();
		bn.add(C);
		bn.add(S);
		bn.add(R);
		bn.add(W);
		// Shorthands
		BooleanValue TRUE = BooleanValue.TRUE;
		BooleanValue FALSE = BooleanValue.FALSE;
		Assignment a;

		// C (no parents)
		CPT Bprior = new Base.CPT(C);
		a = new Base.Assignment();
		Bprior.set(TRUE, a, 0.5);
		Bprior.set(FALSE, a, 1-0.5);
		bn.connect(C, new ArraySet<RandomVariable>() , Bprior);

		// C -> S
		Set<RandomVariable> justC = new ArraySet<RandomVariable>();
		justC.add(C);
		CPT SgivenC = new Base.CPT(S);
		a = new Base.Assignment();
		a.put(C, TRUE);
		SgivenC.set(TRUE, a, 0.1);
		SgivenC.set(FALSE, a, 1-0.1);
		a = new Base.Assignment();
		a.put(C, FALSE);
		SgivenC.set(TRUE, a, 0.5);
		SgivenC.set(FALSE, a, 1-0.5);
		bn.connect(S, justC, SgivenC);

		// C -> R
		justC.add(C);
		CPT RgivenC = new Base.CPT(R);
		a = new Base.Assignment();
		a.put(C, TRUE);
		RgivenC.set(TRUE, a, 0.8);
		RgivenC.set(FALSE, a, 1-0.8);
		a = new Base.Assignment();
		a.put(C, FALSE);
		RgivenC.set(TRUE, a, 0.2);
		RgivenC.set(FALSE, a, 1-0.2);
		bn.connect(R, justC, RgivenC);

		// S,R -> W
		Set<RandomVariable> SR = new ArraySet<RandomVariable>();
		SR.add(S);
		SR.add(R);
		CPT WgivenSR = new Base.CPT(W);
		a = new Base.Assignment();
		a.put(S, TRUE);
		a.put(R, TRUE);
		WgivenSR.set(TRUE, a, 0.99);
		WgivenSR.set(FALSE, a, 1-0.99);
		a = new Base.Assignment();
		a.put(S, TRUE);
		a.put(R, FALSE);
		WgivenSR.set(TRUE, a, 0.90);
		WgivenSR.set(FALSE, a, 1-0.90);
		a = new Base.Assignment();
		a.put(S, FALSE);
		a.put(R, TRUE);
		WgivenSR.set(TRUE, a, 0.90);
		WgivenSR.set(FALSE, a, 1-0.90);
		a = new Base.Assignment();
		a.put(S, FALSE);
		a.put(R, FALSE);
		WgivenSR.set(TRUE, a, 0.0);
		WgivenSR.set(FALSE, a, 1-0.0);
		bn.connect(W, SR, WgivenSR);
		
		System.out.println(bn);
		
		
		//Creating enumertor and other three samplers and producing output
		System.out.println("P(Rain|Sprinkler=true) = <0.3,0.7>");
		Enumerator exact = new Enumerator();
		LikelihoodSampler lSampler = new LikelihoodSampler();
		RejectionSampler rSampler = new RejectionSampler();
		GibbsSampler gSampler = new GibbsSampler();
		a = new Base.Assignment();
		a.put(S, TRUE);
		Distribution dist = exact.EnumerationAsk(R, a, bn);
		System.out.println(dist);
		Distribution dist_L = lSampler.LikelihoodWeighting(R, a, bn, 1000);
		System.out.println(dist_L);
		Distribution dist_R = rSampler.RejectionSampling(R, a, bn, 10000);
		System.out.println(dist_R);
		Distribution dist_G = gSampler.GibbsAsk(R, a, bn, 10000);
		System.out.println(dist_G);

	}

}
