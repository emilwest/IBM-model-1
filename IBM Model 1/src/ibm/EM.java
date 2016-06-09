package ibm;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/** A blueprint of the Expectation-Maximation algorithm used 
 * in IBM model 1.
 * @author Emil Westin
 * @since May 2016
 */
public class EM {

	private StringBuilder resultSaver = new StringBuilder();
	private int iterations;
	private boolean converged = false;
	
	// stores english and swedish corpora in lists, one sentence per line
	private List<String> englishSentences;
	private List<String> swedishSentences;
	
	// arrays storing unique swedish and english words from respective corpus
	private String [] sweWords;
	private String [] engWords;
	
	// target language initial uniform probability
	private double initialUniformProbabilityTarget;
	private Map<String, Double> results = new LinkedHashMap<String, Double>();

	// I could just have created a 2d array here, but what the heck, why not be fancy.
	ProbabilityChart chart;

	/** Constructs a new EM object.
	 * @param source The source language corpus
	 * @param target The target language corpus
	 * @param iterations Specifies the number of iterations in the EM algorithm
	 * @throws IOException If the files couldn't be imported correctly
	 */
	public EM (String source, String target, int iterations) throws IOException {
		this.iterations = iterations;
		this.swedishSentences = Utils.importFile(source);
		this.englishSentences = Utils.importFile(target);
		this.sweWords = Utils.collectDistinctWords(swedishSentences).keySet().toArray(new String[0]);
		this.engWords = Utils.collectDistinctWords(englishSentences).keySet().toArray(new String[0]);
		this.initialUniformProbabilityTarget = (1.0) / (engWords.length);
		this.chart = new ProbabilityChart(sweWords.length, engWords.length);
	}

	/**
	 * Initializes the uniform probabilities by adding them into the probability chart.
	 */
	public void initUniformProbabilities(){
		
		for (int k = 0; k < swedishSentences.size(); k++) {
			String[] fWords = swedishSentences.get(k).split(" ");
			String[] eWords = englishSentences.get(k).split(" ");	
			for (int e = 0; e < eWords.length; e++) {
				for (int f = 0; f < fWords.length; f++) {
					chart.set(e, f, initialUniformProbabilityTarget);
				}
			}
		}
	}
	
	/** Help-method to reset the array back to 0 after every iteration
	 * @param doubleArray
	 * @param singleArray
	 */
	private static void resetArraysToZero(double[][] doubleArray, double[] singleArray){
		for (int i = 0; i<doubleArray.length; i++){
			for (int j = 0; j<doubleArray[i].length; j++){
				doubleArray[i][j] = 0.0;
			}
		}
		Arrays.fill(singleArray, 0.0);
	}

	/**
	 * Prints out the results, i.e. the translation probabilities.
	 */
	public void printResults(){
		System.out.println(resultSaver);
	}
	
	/**<h1>EM training</h1>
	 * <p>
	 * The idea behind EM-training is that in the E-step, we compute the expected 
	 * counts for the t-parameter based on summing over the hidden variable (the alignment),
	 * while in the M-step, we use these normalized counts to compute 
	 * the maximum likelihood estimate of the probability t.
	 * </p>
	 * <p>
	 * The t parameter refers to the translation probability t 
	 * of an English word e given a foreign word f: t(e|f)
	 * </p>
	 */
	public void train() {
		double [] s_totalCount = new double [engWords.length];
		double [][] count_e_given_f = new double [engWords.length][sweWords.length];
		double [] count_total_f = new double[sweWords.length];
		int next = 1;
		
		while (!converged) {
			resultSaver.append(
					"\n-------------------\n"
					+ "Iteration: " + next +
					"\n-------------------\n"
					);
			
			resetArraysToZero(count_e_given_f, count_total_f);

			// k = line number of each sentence
			for (int k = 0; k < swedishSentences.size(); k++) {
				String[] fWords = swedishSentences.get(k).split(" ");
				String[] eWords = englishSentences.get(k).split(" ");

				// E step 1: compute normalization to weigh count
				for (int e = 0; e < eWords.length; e++) {
					s_totalCount[e] = 0.0;
					// for each english word, go through all swedish words (i.e. all possible alignments)
					for (int f = 0; f < fWords.length; f++) {
						s_totalCount[e] += chart.get(e, f);
					}			
				}
				
				// E step 2: compute expected counts
				for (int e = 0; e < eWords.length; e++) {
					for (int f = 0; f < fWords.length; f++) {
						double count = chart.get(e, f);
						double normalizedCount = count / s_totalCount[e];
						count_e_given_f[e][f] += normalizedCount;
						count_total_f[f] += normalizedCount;	
					}
				}
			}

			// M step: estimating probabilities
			for (int f = 0; f < sweWords.length; f++) {
				for (int e = 0; e < engWords.length; e++) {
					if (count_total_f[f] > 0.01){
						// t(e|f) = count(e|f) /total(f)
						double estimate = (count_e_given_f[e][f]) / count_total_f[f];		
						chart.set(e, f, estimate);
						results.put("t(" + engWords[e] + "|" + sweWords[f] + ")", estimate);
					}
					results = Utils.sortByComparator(results);
				}
			}
			resultSaver.append(Utils.mapToString(results, 10));
			if (next == iterations)
				converged = true;
			next++;
		}
	}
}
