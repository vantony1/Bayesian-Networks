package Test;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import Algorithms.Enumerator;
import Base.Assignment;
import Base.BooleanValue;
import Base.StringValue;
import Core.BayesianNetwork;
import Core.Distribution;
import Core.RandomVariable;
import Base.Value;
import Parser.BIFParser;
import Parser.XMLBIFParser;
import Parser.XMLBIFPrinter;

public class ExactInferencer {

	public static void main(String[] args)throws IOException, ParserConfigurationException, SAXException {
		//relevant booleans
		boolean xml = false; 
		boolean bif = false;
		
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
		
		//determines Query var name from input
		String QueryName = args[1];
		
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
		
		//Getting query var from network

		RandomVariable X = network.getVariableByName(QueryName);

		//updating assignment using input

		for(int i = 2; i < args.length; i = i + 2) {
			RandomVariable var_e = network.getVariableByName(args[i]);
						
			Value val_e = new Value(args[i+1]);
			
			e.put(var_e, val_e);
		}
		
		//new Enumerator
		Enumerator exact = new Enumerator();
		
		//Calling and presenting output from enumerator
		Distribution dist = exact.EnumerationAsk(X, e, network);
	
		System.out.println("Enumerator Output: ");
		System.out.println(dist);
			
		
		
	}

}
