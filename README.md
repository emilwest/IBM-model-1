# IBM model 1
Java implementation of the first IBM model, specifically using the EM-algorithm

# Instructions
To run with default corpora:

java -jar IBM.jar

or:

java IBM corpus.en corpus.sv

# What is this?
This is a java implementation of the first IBM model, a statistical alignment algorithm used in machine translation to predict 
the most probable English word e given a foreign word f.</p>

<p>The program outputs the 10 most likely lexical alignment parameters
t(e|f) at each iteration in the EM algorithm.
It also reports the perplexity of these parameters. 
Minimizing perplexity is the same as maximizing probability.</p>

# EM-training

 The idea behind EM-training is that in the E-step, we compute the expected 
counts for the t-parameter based on summing over the hidden variable (the alignment),
while in the M-step, we use these normalized counts to compute 
the maximum likelihood estimate of the probability t.


The t parameter refers to the translation probability t 
of an English word e given a foreign word f: t(e|f)


