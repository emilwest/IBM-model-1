package ibm;

import java.io.IOException;

/**<h1>IBM MODEL I</h1>
 * 
 * <p>Java implementation of the first IBM model,
 * a statistical alignment algorithm used in machine translation to predict 
 * the most probable English word e given a foreign word f.</p>
 * 
 * <p>The program outputs the 10 most likely lexical alignment parameters
 * t(e|f) at each iteration in the EM algorithm.
 * It also reports the perplexity of these parameters. 
 * Minimizing perplexity is the same as maximizing probability.</p>
 * 
 * @author Emil Westin
 * @since May 2016
 */
public class Main {

	public static void main(String[] args) {

		String swedish = "";
		String english = "";

		if (args.length < 2) {
			System.out.println("No arguments given. Using default corpora.");
			System.out.println("(You can give two arguments, one corpus ending in .en or .eng\n"
					+ "and the other ending in .sv or .swe)");
			swedish = "corpus.sv";
			english = "corpus.en";
		} else {
			for (String a : args) {
				if (a.endsWith(".en") || a.endsWith(".eng")) {
					english = a;
				} else if (a.endsWith(".sv") || a.endsWith(".swe")) {
					swedish = a;
				}
			}
		}
		
		long tStart = System.currentTimeMillis();
		
		try {
			EM algorithm = new EM(swedish, english, 5);
			algorithm.initUniformProbabilities();
			algorithm.train();
			algorithm.printResults();
		} catch (IOException e) {
			e.printStackTrace();
		}

		long tEnd = System.currentTimeMillis();
		long tDelta = tEnd - tStart;
		double elapsedSeconds = tDelta / 1000.0;
		System.out.println("-----------------");
		System.out.println("System Info");
		System.out.println("\nElapsed time: " + elapsedSeconds + " seconds.");
		Utils.printSystemProperties();
	}
}