package ibm;

/** A chart for storing probability values.
 * @author Emil Westin
 *
 */
public class ProbabilityChart {
	
	/**
	 * Two dimensional jagged array
	 */
	private final double [][] chart;
	
	/** Constructs a new Chart for storing probabilities at given positions
	 * @param nFWords number of foreign words
	 * @param nEWords number of english words
	 */
	public ProbabilityChart(int nEWords, int nFWords){
		this.chart = new double[nEWords][nFWords];
	}

	/** Returns the probability of the specified span
	 * @param min the leftmost position 
	 * @param max the rightmost position 
	 * @return the probability of the specified span, or <code>null</null>
	 */
	public double get(int min, int max) {
		return chart[min][max];
	}

	/** Sets the probability of the specified span
	 * @param min the leftmost position 
	 * @param max the rightmost position 
	 * @param score the new probability of the specified span
	 */
	public void set(int min, int max, double score){
		chart[min][max] = score;
	}

}
