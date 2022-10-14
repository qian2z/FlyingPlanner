package F28DA_CW2;

import org.jgrapht.graph.DefaultEdge;

/**
 * Custom edge class labeled with Flight object.
 * 
 * @author qian2z
 * @version 1.0
 * @since 10-03-2022
 * @reference Kinable, J. (2018)
 *            https://github.com/jgrapht/jgrapht/blob/master/jgrapht-demo/src/main/java/org/jgrapht/demo/LabeledEdges.java
 */
public class FlightEdge extends DefaultEdge {

	private Flight flight;

	/**
	 * Creates a edge with the label of Flight object.
	 *
	 * @param f The Flight object that labeled in the edge.
	 * 
	 */
	public FlightEdge(Flight f) {
		this.flight = f;
	}

	/**
	 * This method returns the Flight object that labeled in the edge.
	 * 
	 * @return Airport This returns the Flight object that labeled in the edge.
	 */
	public Flight getFlight() {
		return flight;
	}

	/**
	 * This method returns a string representation of the FlightEdge object.
	 * 
	 * @return String This returns a string representation of the FlightEdge object.
	 */
	public String toString() {
		return "(" + getSource() + " : " + getTarget() + " : " + flight.getFlightCode() + ")";
	}

}

