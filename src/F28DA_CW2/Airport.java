package F28DA_CW2;

import java.util.Set;

/**
 * This is the Airport class.
 * 
 * @author Liew Pei Yee
 * @version 1.0
 * @since 10-03-2022
 * 
 */
public class Airport implements IAirportPartB, IAirportPartC {

	private String code;
	private String city;
	private String name;
	private Set<Airport> directlyConnectedAirport;
	private int directlyConnectedOrder;

	/**
	 * Creates a new Airport with the given airport code, the given city name that the
	 * airport located and the given airport name.
	 */
	public Airport(String c, String ct, String n) {
		this.code = c;
		this.city = ct;
		this.name = n;
		this.directlyConnectedAirport = null;
		this.directlyConnectedOrder = 0;
	}

	/**
	 * This method returns the code of the airport.
	 * 
	 * @return String This returns the code of the airport.
	 */
	@Override
	public String getCode() {
		return code;
	}

	/**
	 * This method returns the city name of the airport.
	 * 
	 * @return String This returns the city name of the airport.
	 */
	public String getCity() {
		return city;
	}

	/**
	 * This method returns the name of the airport.
	 * 
	 * @return String This returns the name of the airport.
	 */
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setDicrectlyConnected(Set<Airport> directlyConnected) {
		this.directlyConnectedAirport = directlyConnected;
	}

	@Override
	public Set<Airport> getDicrectlyConnected() {
		return directlyConnectedAirport;
	}

	@Override
	public void setDicrectlyConnectedOrder(int order) {
		this.directlyConnectedOrder = order;
	}

	@Override
	public int getDirectlyConnectedOrder() {
		return directlyConnectedOrder;
	}

	/**
	 * This method returns a string representation of the Airport object.
	 * 
	 * @return String This returns a string representation of the Airport object.
	 */
	public String toString() {
		return code + " " + city + " " + name;
	}

}
