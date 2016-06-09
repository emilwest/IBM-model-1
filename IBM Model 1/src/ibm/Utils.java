package ibm;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;


/** A class holding various utility methods.
 * @author Emil Westin
 *
 */
public class Utils {

	
	/** Calculates the binary logarithm of the value a
	 *  (i.e. the power to which the number 2 must be raised to obtain the value a)
	 * @param a the value
	 * @return the power to which the number 2 must be raised to obtain the value a
	 */
	public static double log2 (double a){
		return Math.log(a)/Math.log(2);
	}
	

	/** Method for importing text files 
	 * @param filename String Name of the file of interest
	 * @return ArrayList consisting of one sentence per line
	 * @throws IOException Throws IO Exception
	 */
	public static ArrayList<String> importFile(String filename) throws IOException {
		ArrayList<String> newList = new ArrayList<String>();
		
		final Path file = Paths.get(filename);
		Scanner sc = new Scanner(file);

		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			newList.add(line);
		}
		sc.close();
		
		return newList;
	}
	
	/** Splits up sentence into words using space ' ' as the delimiter
	 * @param List<String> unsplitted string list
	 * @return List<String> splitted list
	 */
	public static List<String> split(List<String> list) {
		List<String> split = new ArrayList<String>();

		list.forEach(s -> {
			String[] copy = s.split(" ");
			for (String t : copy) {
				split.add(t);
			}
		});

		return split;
	}
	
	/**
	 * Prints system properties, like java version and system OS.
	 */
	public static void printSystemProperties(){
	  	  System.out.println("Available processor cores: " + 
			        Runtime.getRuntime().availableProcessors());
		  Properties properties = System.getProperties();
	        Set<Object> keys = properties.keySet();
	        for(Object key : keys){
	        	if (key.equals("java.version"))
	        		System.out.println("Java version: " + properties.get(key));
	        	if (key.equals("os.arch"))
	        		System.out.println("OS architecture: " + properties.get(key));
	        	if (key.equals("os.name"))
	        		System.out.println("Operating System: "+ properties.get(key));
	        }
	}
	
	/** Collects unique words from corpus
	 * @param List<String> list takes a list as input parameter
	 * @return Map<String,Long> returns a map
	 */
	public static Map<String, Long> collectDistinctWords(List<String> list) {
		List<String> newList = split(list);
		Map<String, Long> collecter = newList.stream().distinct()
				.collect(Collectors.groupingByConcurrent(w -> w, Collectors.counting()));

		return collecter;

	}
	
	
	/** Prints all the unique words of the corpus
	 * @param Map<String, Long> input map
	 */
	public static void printMap(Map<String, Double> m){
		for (Entry<String, Double> entry : m.entrySet()){		
			System.out.println(entry.getKey() + "\t\t" + entry.getValue());
		}
	}
	
	/** Returns a stringbuilder of a formatted table printing out the values of a map, 
	 * as well as the perplexity.
	 * @param m The map to be printed out
	 * @param limit How many results to be displayed, for example 10
	 * @return a stringbuilder of a table
	 */
	public static StringBuilder mapToString(Map<String, Double> m, int limit){
		StringBuilder sb = new StringBuilder();
		int count = 0;
		double perplexity = 0.0;
		sb.append(String.format("%-20s %-20s\n", "word e given f" ,"t(e|f)"));
		for (Entry<String, Double> entry : m.entrySet()){		
			if (count<limit)
				sb.append(String.format("%-20s %-20.3f\n", entry.getKey(), entry.getValue()));
			if (entry.getValue() != 0 && count<limit)
				perplexity-= log2(entry.getValue());
			count++;
		}
		sb.append(String.format("\nPerplexity: %.1f\n",  perplexity));
		
		return sb;
		
	}
	
	/** Sorts a map in descending order.
	 * @param unsortMap map to be sorted
	 * @return a sorted map in descending order
	 */
	public static Map<String, Double> sortByComparator(Map<String, Double> unsortMap) {

		// Convert Map to List
		List<Entry<String, Double>> list = 
			new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			@Override
			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// Convert sorted map back to a Map
		Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for (Iterator<Entry<String, Double>> it = list.iterator(); it.hasNext();) {
			Entry<String, Double> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
}
