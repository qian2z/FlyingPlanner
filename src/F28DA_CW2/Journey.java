package F28DA_CW2;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.GraphPath;

/**
 * This is the Journey class.
 * 
 * @author Liew Pei Yee
 * @version 1.0
 * @since 10-03-2022
 * 
 */
public class Journey implements IJourneyPartB<Airport, Flight>, IJourneyPartC<Airport, Flight> {

	private Airport fromAirport;
	private Airport toAirport;
	private GraphPath<Airport, FlightEdge> graphPath;

	/**
	 * Creates a new Journey with the given origin airport, the given destination
	 * airport and the calculated graph path.
	 */
	public Journey(Airport from, Airport to, GraphPath<Airport, FlightEdge> gp) {
		this.fromAirport = from;
		this.toAirport = to;
		this.graphPath = gp;
	}

	/**
	 * This method returns the list of airports codes composing the journey.
	 * 
	 * @return List<String> This returns a list of airport codes.
	 */
	@Override
	public List<String> getStops() {
		List<Airport> airportList = graphPath.getVertexList();
		List<String> strList = new LinkedList<>();
		for (Airport a : airportList) {
			strList.add(a.getCode());
		}
		return strList;
	}

	/**
	 * This method returns the list of flight codes composing the journey.
	 * 
	 * @return List<String> This returns a list of flight codes.
	 */
	@Override
	public List<String> getFlights() {
		List<FlightEdge> flightList = graphPath.getEdgeList();
		List<String> strList = new LinkedList<>();
		for (FlightEdge f : flightList) {
			Flight ff = f.getFlight();
			strList.add(ff.getFlightCode());
		}
		return strList;
	}

	/**
	 * This method returns the number of connections of the journey.
	 * 
	 * @return int This returns the number of connections of the journey.
	 */
	@Override
	public int totalHop() {
		return getFlights().size();
	}

	/**
	 * This method returns the total cost of the journey.
	 * 
	 * @return int This returns the total cost of the journey.
	 */
	@Override
	public int totalCost() {
		List<FlightEdge> flightList = graphPath.getEdgeList();
		int totalCost = 0;
		for (FlightEdge f : flightList) {
			Flight ff = f.getFlight();
			totalCost += ff.getCost();
		}
		return totalCost;
	}

	/**
	 * This method returns the total time in flight of the journey.
	 * 
	 * @return int This returns the sum of time in each flight of the journey.
	 */
	@Override
	public int airTime() {
		List<FlightEdge> flightList = graphPath.getEdgeList();
		int totalMin = 0;
		for (FlightEdge f : flightList) {
			Flight ff = f.getFlight();
			String fromTime = ff.getFromGMTime();
			String toTime = ff.getToGMTime();
			totalMin += calculateTime(fromTime, toTime);
		}
		return totalMin;
	}

	/**
	 * This method returns the total time in connection of the journey.
	 * 
	 * @return int This returns the sum of time in each connection of the journey.
	 */
	@Override
	public int connectingTime() {
		List<FlightEdge> flightList = graphPath.getEdgeList();
		int totalMin = 0;
		for (int i = 0; i < flightList.size() - 1; i++) {
			FlightEdge f1 = flightList.get(i);
			FlightEdge f2 = flightList.get(i + 1);
			Flight ff1 = f1.getFlight();
			Flight ff2 = f2.getFlight();
			String startTime = ff1.getToGMTime();
			String endTime = ff2.getFromGMTime();
			totalMin += calculateTime(startTime, endTime);
		}
		return totalMin;

	}

	/**
	 * This method returns the total travel time of the journey.
	 * 
	 * @return int This returns the sum of the total time in flight and the total
	 *         time in connection of the journey.
	 */
	@Override
	public int totalTime() {
		return airTime() + connectingTime();
	}

	/**
	 * This method calculate the duration between two time in String.
	 * 
	 * @param from The start time in String type.
	 * @param to   The end time in String type.
	 * @return int This returns the calculated duration in minutes.
	 */
	public int calculateTime(String from, String to) {
		int h1 = 0, m1 = 0, h2 = 0, m2 = 0;
		int min1 = 0, min2 = 0;

		if (from.length() == 5 && to.length() == 5) {
			h1 = Integer.parseInt(from.substring(0, 2));
			m1 = Integer.parseInt(from.substring(3, 5));
			h2 = Integer.parseInt(to.substring(0, 2));
			m2 = Integer.parseInt(to.substring(3, 5));
		} else if (from.length() == 4 && to.length() == 4) {
			h1 = Integer.parseInt(from.substring(0, 2));
			m1 = Integer.parseInt(from.substring(2, 4));
			h2 = Integer.parseInt(to.substring(0, 2));
			m2 = Integer.parseInt(to.substring(2, 4));
		}

		if (h1 > h2) {
			h2 += 24;
		}

		if (h1 == h2) {
			if (m2 < m1) {
				h2 += 24;
			}
		}

		min1 = (h1 * 60) + m1;
		min2 = (h2 * 60) + m2;
		int min = min2 - min1;
		return min;
	}

	/**
	 * This method converts the input minutes into hours and minutes in String type.
	 * 
	 * @param minute The minutes that need to be converted.
	 * @return String This returns converted hours and minutes from the input
	 *         minutes in String type.
	 */
	public String printTime(int minute) {
		int hours = minute / 60;
		int minutes = minute % 60;
		String time = hours + " hours " + minutes + " minutes";
		return time;
	}

	/**
	 * This method prints the details of the journey.
	 */
	public void printJourney() {
		System.out.println();
		String from = fromAirport.getCity() + " (" + fromAirport.getCode() + ")";
		String to = toAirport.getCity() + " (" + toAirport.getCode() + ")";
		System.out.println("Journey for " + from + " to " + to);
		String format1 = "%-4s %-20s %-6s %-8s %-20s %-6s %n";
		System.out.printf(format1, "Leg", "Leave", "At", "On", "Arrive", "At");
		List<FlightEdge> flightList = graphPath.getEdgeList();
		int i = 1;
		for (FlightEdge f : flightList) {
			Flight ff = f.getFlight();
			Airport aFrom = ff.getFrom();
			Airport aTo = ff.getTo();
			String fromAP = aFrom.getCity() + " (" + aFrom.getCode() + ")";
			String toAP = aTo.getCity() + " (" + aTo.getCode() + ")";
			System.out.printf(format1, "" + i, fromAP, ff.getFromGMTime(), ff.getFlightCode(), toAP, ff.getToGMTime());
			i++;
		}
		System.out.println();
		String format2 = "%-22s";
		System.out.printf(format2, "Total Journey Cost");
		System.out.println(" = £" + totalCost());
		System.out.printf(format2, "Total Time in the Air");
		System.out.println(" = " + printTime(airTime()));
		if (connectingTime() != 0) {
			System.out.printf(format2, "Total Connecting Time");
			System.out.println(" = " + printTime(connectingTime()));
		}
		System.out.printf(format2, "Total Journey Time");
		System.out.println(" = " + printTime(totalTime()));
	}

	/**
	 * A private method that prints the details of the given journey.
	 * 
	 * @param j The journey that going to be printed.
	 */
	private void printJourney(Journey j) {
		Airport fromA = j.fromAirport;
		Airport toA = j.toAirport;
		String from = fromA.getCity() + " (" + fromA.getCode() + ")";
		String to = toA.getCity() + " (" + toA.getCode() + ")";
		List<FlightEdge> flightList = j.graphPath.getEdgeList();
		if (flightList.size() != 0) {
			System.out.println("Journey for " + from + " to " + to);
			String format1 = "%-4s %-20s %-6s %-8s %-20s %-6s %n";
			System.out.printf(format1, "Leg", "Leave", "At", "On", "Arrive", "At");
			int i = 1;
			for (FlightEdge f : flightList) {
				Flight ff = f.getFlight();
				Airport aFrom = ff.getFrom();
				Airport aTo = ff.getTo();
				String fromAP = aFrom.getCity() + " (" + aFrom.getCode() + ")";
				String toAP = aTo.getCity() + " (" + aTo.getCode() + ")";
				System.out.printf(format1, "" + i, fromAP, ff.getFromGMTime(), ff.getFlightCode(), toAP,
						ff.getToGMTime());
				i++;
			}
			System.out.println();
			String format2 = "%-22s";
			System.out.printf(format2, "Total Journey Cost");
			System.out.println(" = £" + j.totalCost());
			System.out.printf(format2, "Total Time in the Air");
			System.out.println(" = " + j.printTime(j.airTime()));
			if (j.connectingTime() != 0) {
				System.out.printf(format2, "Total Connecting Time");
				System.out.println(" = " + j.printTime(j.connectingTime()));
			}
			System.out.printf(format2, "Total Journey Time");
			System.out.println(" = " + j.printTime(j.totalTime()));
			System.out.println("--------------------------------------------------------------");
		}
	}

	/**
	 * The method that prints the details of the journeys for a meet up search.
	 * 
	 * @param j1        The first journey that going to be printed (from first
	 *                  origin airport to the best airport for meet up).
	 * @param j2        The second journey that going to be printed (from second
	 *                  origin airport to the best airport for meet up).
	 * @param type      The type of journey for meet up search (Least Cost, Least
	 *                  Changeovers, Least Total Journey Time).
	 * @param startTime The given departing time for the meet up search with the
	 *                  earliest meet.
	 */
	public void printMeetUp(Journey j1, Journey j2, String type, String startTime) {
		System.out.println();
		Airport fromAP1 = j1.fromAirport;
		Airport fromAP2 = j2.fromAirport;
		Airport toAP = j1.toAirport;
		String from1 = fromAP1.getCity() + " (" + fromAP1.getCode() + ")";
		String from2 = fromAP2.getCity() + " (" + fromAP2.getCode() + ")";
		String to = toAP.getCity() + " (" + toAP.getCode() + ")";
		System.out.println("Preferred Meet-Up Airport with " + type + " from " + from1 + " and " + from2 + ": " + to);
		System.out.println("--------------------------------------------------------------");
		printJourney(j1);
		printJourney(j2);
		int totalCost = j1.totalCost() + j2.totalCost();
		String format = "%-22s";
		System.out.printf(format, "Total Meet-Up Cost");
		System.out.println(" = £" + totalCost);
		if (type.equalsIgnoreCase("Least Total Journey Time")) {
			String startTimeF = startTime.substring(0, 2) + ":" + startTime.substring(2);
			List<FlightEdge> flightList1 = j1.graphPath.getEdgeList();
			Flight ff1 = flightList1.get(0).getFlight();
			String ff1Time = ff1.getFromGMTime();
			int timeA1 = j1.calculateTime(startTimeF, ff1Time);
			int timeB1 = j1.totalTime();
			int time1 = timeA1 + timeB1;

			List<FlightEdge> flightList2 = j2.graphPath.getEdgeList();
			Flight ff2 = flightList2.get(0).getFlight();
			String ff2Time = ff2.getFromGMTime();
			int timeA2 = j2.calculateTime(startTimeF, ff2Time);
			int timeB2 = j2.totalTime();
			int time2 = timeA2 + timeB2;

			int totalTime = 0;

			if (time1 > time2) {
				totalTime = time1;
			} else {
				totalTime = time2;
			}

			System.out.printf(format, "Estimated Meet Time");
			System.out.println(" = " + printTime(totalTime) + " after The Departing Time: " + startTimeF);
		}
	}

}
