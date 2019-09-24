package Test;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import Algorithms.Enumerator;
import Algorithms.GibbsSampler;
import Algorithms.LikelihoodSampler;
import Algorithms.PriorSampler;
import Algorithms.RejectionSampler;
import Base.Assignment;
import Base.Value;
import Core.BayesianNetwork;
import Core.Distribution;
import Core.RandomVariable;
import Parser.BIFParser;
import Parser.XMLBIFParser;


//Class with the main method that reads a BN and returns sampling values with
//Likelihood, Rejection and Gibbs Sampling
public class ApproxInferencer {

	public static void main(String[] args)throws IOException, ParserConfigurationException, SAXException {
		//relevant booleans
		boolean xml = false; 
		boolean bif = false;
		boolean rejection = false;
		boolean likelihood = false;
		boolean gibbs = false;
		
		//filename/PATH
		String filename = args[0];
		
		//chooses right file type
		if(filename.contains("xml")) {
			xml = true;
		} else if (filename.contains("bif")) {
			bif = true;
		} else {
			System.out.println("incorrect file format! ending program");
			return;
		}
		
		//determines N from input
		int N = Integer.parseInt(args[1]);
		
		//determines Query var name
		String QueryName = args[2];
		
		Assignment e = new Base.Assignment();
		
		BayesianNetwork network;
		
		//parses BN from input file 
		if(xml) {
			XMLBIFParser parser = new XMLBIFParser();
			network = parser.readNetworkFromFile(filename);
		} else {
			BIFParser parser = new BIFParser(new FileInputStream(filename));
			network = parser.parseNetwork();
		}
		
		//Getting query var using network
		RandomVariable X = network.getVariableByName(QueryName);

		//updating assignment from input
		for(int i = 3; i+1 < args.length; i = i + 2) {
			RandomVariable var_e = network.getVariableByName(args[i]);
						
			Value val_e = new Value(args[i+1]);
			
			e.put(var_e, val_e);
		}
		
		//creating the three samplers
		LikelihoodSampler lSampler = new LikelihoodSampler();
		RejectionSampler rSampler = new RejectionSampler();
		GibbsSampler gSampler = new GibbsSampler();
		
		
		//Calling and presenting output from samplers
		Distribution dist_R = rSampler.RejectionSampling(X, e, network, N);
		System.out.println("Rejection Sample: ");
		System.out.println(dist_R);
		
		Distribution dist_L = lSampler.LikelihoodWeighting(X, e, network, N);
		System.out.println("Likelihood Sample: ");
		System.out.println(dist_L);

		
		Distribution dist_G = gSampler.GibbsAsk(X, e, network, N);
		System.out.println("Gibbs Sample: ");
		System.out.println(dist_G);
		
		

		
	}

}
