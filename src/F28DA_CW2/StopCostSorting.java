package F28DA_CW2;

import java.util.Comparator;

/**
 * A helper class that I implemented to sort the StopCostEntry in an ArrayList.
 * Firstly, sort according to the number of stop / connections in the journey,
 * secondly, sort according to total cost of the journey.
 * 
 * @author Liew Pei Yee
 * @version 1.0
 * @since 10-03-2022
 * 
 **/

public class StopCostSorting implements Comparator<StopCostEntry> {

	@Override
	public int compare(StopCostEntry s1, StopCostEntry s2) {

		/**
		 * Mahrsee, R. (2021) Comparator Interface in Java with Examples. Available at:
		 * https://www.geeksforgeeks.org/comparator-interface-java/ 
		 */
		int stopCompare = 0;
		int stop1 = s1.getStop();
		int stop2 = s2.getStop();
		if (stop1 == stop2) {
			stopCompare = 0;
		} else if (stop1 > stop2) {
			stopCompare = 1;
		} else {
			stopCompare = -1;
		}

		int costCompare = 0;
		int cost1 = s1.getCost();
		int cost2 = s2.getCost();
		if (cost1 == cost2) {
			costCompare = 0;
		} else if (cost1 > cost2) {
			costCompare = 1;
		} else {
			costCompare = -1;
		}

		// 2nd level comparison
		return (stopCompare == 0) ? costCompare : stopCompare;

	}

}

