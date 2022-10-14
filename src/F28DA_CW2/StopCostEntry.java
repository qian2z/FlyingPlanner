package F28DA_CW2;

/**
 * A helper class that I implemented to create an entry object that store the
 * airport code, the number of stops / connections in the journey and the total
 * journey cost for every possible meet up airports for the meet up search.
 * 
 * @author Liew Pei Yee
 * @version 1.0
 * @since 10-03-2022
 * 
 **/

public class StopCostEntry {

	private String code;
	private int stop;
	private int cost;

	/**
	 * Creates a new entry with the given meet up airport code, the number of stops
	 * or connections and the total cost in the journey to the meet up airport.
	 */
	public StopCostEntry(String cd, int s, int c) {
		this.code = cd;
		this.stop = s;
		this.cost = c;
	}

	/**
	 * This method returns the code of the meet up airport.
	 * 
	 * @return String This returns the code of the meet up airport.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * This method returns the number of stops or connections in the journey to the
	 * meet up airport.
	 * 
	 * @return int This returns the number of stops or connections in the journey to
	 *         the meet up airport.
	 */
	public int getStop() {
		return stop;
	}

	/**
	 * This method returns the total journey cost to the meet up airport.
	 * 
	 * @return int This returns the total journey cost to the meet up airport.
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * This method returns a string representation of the Entry object.
	 * 
	 * @return String This returns a string representation of the Entry object.
	 */
	public String toString() {
		return "" + code + " " + stop + " " + cost;
	}

}

