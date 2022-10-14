package F28DA_CW2;

/**
 * This is the Flight class.
 * 
 * @author Liew Pei Yee
 * @version 1.0
 * @since 10-03-2022
 * 
 */
public class Flight implements IFlight {

	private String flightCode;
	private Airport toAP;
	private Airport fromAP;
	private String toGMTime;
	private String fromGMTime;
	private int cost;

	/**
	 * Creates a new Flight with the given flight code, departure Airport, departure
	 * time, arrival Airport, arrival time and cost of the flight.
	 */
	public Flight(String fc, Airport from, String fromTime, Airport to, String toTime, int c) {
		this.flightCode = fc;
		this.fromAP = from;
		this.fromGMTime = fromTime;
		this.toAP = to;
		this.toGMTime = toTime;
		this.cost = c;
	}

	/**
	 * This method returns the code of the flight.
	 * 
	 * @return String This returns the code of the flight.
	 */
	@Override
	public String getFlightCode() {
		return flightCode;
	}

	/**
	 * This method returns the arrival airport of the flight.
	 * 
	 * @return Airport This returns the arrival airport of the flight.
	 */
	@Override
	public Airport getTo() {
		return toAP;
	}

	/**
	 * This method returns the departure airport of the flight.
	 * 
	 * @return Airport This returns the departure airport of the flight.
	 */
	@Override
	public Airport getFrom() {
		return fromAP;
	}

	/**
	 * This method returns the departure time of the flight.
	 * 
	 * @return String This returns the departure time of the flight.
	 */
	@Override
	public String getFromGMTime() {
		return fromGMTime.substring(0, 2) + ":" + fromGMTime.substring(2);
	}

	/**
	 * This method returns the arrival time of the flight.
	 * 
	 * @return String This returns the arrival time of the flight.
	 */
	@Override
	public String getToGMTime() {
		return toGMTime.substring(0, 2) + ":" + toGMTime.substring(2);
	}

	/**
	 * This method returns the cost of the flight.
	 * 
	 * @return int This returns the cost of the flight.
	 */
	@Override
	public int getCost() {
		return cost;
	}

	/**
	 * This method returns a string representation of the Flight object.
	 * 
	 * @return String This returns a string representation of the Flight object.
	 */
	public String toString() {
		return flightCode + " " + fromAP + " " + fromGMTime + " " + toAP + " " + toGMTime + " " + cost;
	}

}
