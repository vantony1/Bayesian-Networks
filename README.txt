Name: Victor Antony
Course: CSC 242 Spring 2019

I worked alone on this project. The project utilizes code produced by Prof. George Fergusion (CS: URochester)

This project presents Bayesian Network Inferencers using Enumeration, Rejection Sampling, Likelihood Sampling and Gibbs Sampling

Inferencers are set to parse bif or xml automatically detecting file type

The algorithms for said inferencers are found in the Algorithms package
whereas, the classes with main methods that parse input and call on these inferecers and produce result are found in the Test package

NOTE: I change the format as to how the inferencers accept input and the format is shown below

I have included all the example bif and xml files in the Examples package

The code can be run either through the terminal or through manipulating the run configuration in eclipse:

FOR TERMINAL 

cd to BayesianNetwork/src
javac Test/ExactInferencer.java  OR javac Test/ApproxInferencer.java

to call exact inferencer from terminal use this format:

PATH QueryVar Evidence Value Evidence Value ....
example:   Examples/aima-alarm.xml B J true M true


to call approx inferencer from terminal use this format:

PATH N QueryVar Evidence Value Evidence Value ....

example:   Examples/aima-alarm.xml 10000 B J true M true


FOR ECLIPSE

the format is the same but must include src in the PATH

example: src/Examples/aima-alarm.xml B J true M true
example: src/Examples/aima-alarm.xml 10000 B J true M true


if you have any questions, please feel free to contact me at: vantony@u.rochester.edu