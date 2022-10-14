package F28DA_CW2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

/**
 * This is the central class of the program.
 * 
 * @author Liew Pei Yee
 * @version 1.0
 * @since 10-03-2022
 * 
 */
public class FlyingPlanner implements IFlyingPlannerPartB<Airport, Flight>, IFlyingPlannerPartC<Airport, Flight> {

	/**
	 * The main weighted graph in this class that stored Airport as vertices and
	 * FlightEdge as edges with the cost of flight as weight.
	 */
	private Graph<Airport, FlightEdge> costWeightedGraph = new SimpleDirectedWeightedGraph<>(FlightEdge.class);
	/**
	 * The HashMap that stored each of the airport code (String) as keys, the
	 * Airport object as values while adding the Airport objects to the main graph
	 * as vertices for tracking and fast retrieving purpose.
	 */
	private HashMap<String, Airport> airports = new HashMap<>();
	/**
	 * The HashMap that stored each of the flight code (String) as keys, the Flight
	 * object as values while adding the Flight objects to the main graph as edges
	 * for tracking and fast retrieving purpose.
	 */
	private HashMap<String, Flight> flights = new HashMap<>();
	/**
	 * The main unweighted graph in this class that stored Airport as vertices and
	 * FlightEdge as edges only.
	 */
	private Graph<Airport, FlightEdge> hopUnweightedGraph = new SimpleDirectedGraph<>(FlightEdge.class);
	/**
	 * The DAG graph that stored all airports and the acyclic edges (flights).
	 */
	private DirectedAcyclicGraph<Airport, FlightEdge> directedAcyclicGraph;
	/**
	 * The DAG graph that stored all airports but only the flights flying to an
	 * airport destination with strictly higher number of directly connected
	 * airports than the origin airport.
	 */
	private DirectedAcyclicGraph<Airport, FlightEdge> betterdirectedAcyclicGraph;
	/**
	 * The HashMap that stored Airport as keys, the size of the airport's directly
	 * connected set as values for every airport that available in the main graph.
	 */
	private HashMap<Airport, Integer> directConnections = new HashMap<Airport, Integer>();

	/**
	 * A private method that populate a new graph with the airports and flights
	 * information that stored in main graph / that deep clone (copy) the main graph
	 * to a new empty graph.
	 * 
	 * @param a      The HashMap that stored airports information in the main graph.
	 * @param f      The HashSet that stored flights information in the main graph.
	 * @param g      The empty graph that going to have deep clone (copy) from the
	 *               main graph.
	 * @param weight True if the new empty graph is going to have weighted edges,
	 *               false if the new empty graph is going to have unweighted edges.
	 * @return boolean This returns true if the operation was successful.
	 */
	private boolean populate(HashMap<String, Airport> a, HashMap<String, Flight> f, Graph<Airport, FlightEdge> g,
			boolean weight) {
		Collection<Airport> airportList = a.values();
		for (Airport ap : airportList) {
			g.addVertex(ap);
		}

		Collection<Flight> flightList = f.values();
		for (Flight ff : flightList) {
			Airport from = ff.getFrom();
			Airport to = ff.getTo();
			g.addEdge(from, to, new FlightEdge(ff));
			if (weight == true) {
				g.setEdgeWeight(from, to, ff.getCost());
			}
		}
		return true;
	}

	/**
	 * This method populates the graph with the airports and flights information
	 * from a flight reader object.
	 * 
	 * @param fr The FlightsReader object which reads the airports and flights
	 *           information.
	 * @return boolean This returns true if the operation was successful.
	 */
	@Override
	public boolean populate(FlightsReader fr) {
		HashSet<String[]> airportList = fr.getAirports();
		HashSet<String[]> flightList = fr.getFlights();
		return populate(airportList, flightList);
	}

	/**
	 * This method populates the graph with the airports and flights information
	 * stored in HashSet.
	 * 
	 * @param airportList The HashSet that stored airports information.
	 * @param flightList  The HashSet that stored flights information.
	 * @return boolean This returns true if the operation was successful.
	 */
	@Override
	public boolean populate(HashSet<String[]> airportList, HashSet<String[]> flightList) {
		for (String[] a : airportList) {
			Airport ap = new Airport(a[0], a[1], a[2]);
			airports.put(a[0], ap);
			costWeightedGraph.addVertex(ap);
			hopUnweightedGraph.addVertex(ap);
		}

		for (String[] f : flightList) {
			Airport from = airports.get(f[1]);
			Airport to = airports.get(f[3]);
			Flight fl = new Flight(f[0], from, f[2], to, f[4], Integer.parseInt(f[5]));
			flights.put(f[0], fl);
			costWeightedGraph.addEdge(from, to, new FlightEdge(fl));
			costWeightedGraph.setEdgeWeight(from, to, Integer.parseInt(f[5]));
			hopUnweightedGraph.addEdge(from, to, new FlightEdge(fl));
		}
		return true;
	}

	/**
	 * This method returns the airport object of the given airport code.
	 * 
	 * @param code The code of the airport in String.
	 * @return Airport This returns the Airport object.
	 */
	@Override
	public Airport airport(String code) {
		return airports.get(code);
	}

	/**
	 * This method returns the flight object of the given flight code.
	 * 
	 * @param code The code of the flight in String.
	 * @return Flight This returns the Flight object.
	 */

	@Override
	public Flight flight(String code) {
		return flights.get(code);
	}

	/**
	 * A private method returns a cheapest, following by least connections, lastly
	 * least journey time flight journey from one airport (airport code) to another
	 * from a selected weighted graph.
	 * 
	 * @param from The code of the origin airport.
	 * @param to   The code of the destination airport.
	 * @param g    The weighted graph that stored all the airports and flights
	 *             information.
	 * @return Journey This returns the Journey with least cost, following by least
	 *         connections, lastly least journey time from origin airport to
	 *         destination airport.
	 * @throws FlyingPlannerException If there is no journey available from origin
	 *                                airport to destination airport.
	 */
	private Journey leastCost(String from, String to, Graph<Airport, FlightEdge> g) throws FlyingPlannerException {
		Airport fromA = airport(from);
		Airport toA = airport(to);
		GraphPath<Airport, FlightEdge> dijkstraShortestPath = DijkstraShortestPath.findPathBetween(g, fromA, toA);

		if (dijkstraShortestPath == null) {
			throw new FlyingPlannerException("Journey not available.");
		} else {
			Journey leastCostJ = new Journey(fromA, toA, dijkstraShortestPath);
			Journey leastHopJ = leastHop(from, to);

			if (leastCostJ.totalCost() == leastHopJ.totalCost()) {
				return leastHopJ;
			}
			return leastCostJ;
		}
	}

	/**
	 * This method returns a cheapest, following by least connections, lastly least
	 * journey time flight journey from one airport (airport code) to another.
	 * 
	 * @param from The code of the origin airport.
	 * @param to   The code of the destination airport.
	 * @return Journey This returns the Journey with least cost, following by least
	 *         connections, lastly least journey time from origin airport to
	 *         destination airport.
	 * @throws FlyingPlannerException If there is no journey available from origin
	 *                                airport to destination airport.
	 */
	@Override
	public Journey leastCost(String from, String to) throws FlyingPlannerException {
		return leastCost(from, to, costWeightedGraph);
	}

	/**
	 * This method returns a cheapest, following by least connections, lastly least
	 * journey time flight journey from one airport (airport code) to another,
	 * excluding a list of airport (airport codes).
	 * 
	 * @param from      The code of the origin airport.
	 * @param to        The code of the destination airport.
	 * @param excluding The list containing all the airport codes that need to be
	 *                  excluded from the journey.
	 * @return Journey This returns the Journey with least cost, following by least
	 *         connections, lastly least journey time from origin airport to
	 *         destination airport, excluding a list of airport (airport codes).
	 * @throws FlyingPlannerException If there is no journey available from origin
	 *                                airport to destination airport, excluding a
	 *                                list of airport (airport codes).
	 */
	@Override
	public Journey leastCost(String from, String to, List<String> excluding) throws FlyingPlannerException {
		Graph<Airport, FlightEdge> cloneGraph = new SimpleDirectedWeightedGraph<>(FlightEdge.class);
		populate(airports, flights, cloneGraph, true);

		for (String code : excluding) {
			Airport a = airport(code);
			cloneGraph.removeVertex(a);
		}
		return leastCost(from, to, cloneGraph);
	}

	/**
	 * A private method returns a least connections, following by least cost, lastly
	 * least journey time flight journey from one airport (airport code) to another
	 * from a selected weighted graph and a selected unweighted graph.
	 * 
	 * @param from The code of the origin airport.
	 * @param to   The code of the destination airport.
	 * @param wg   The weighted graph that stored all the airports and flights
	 *             information.
	 * @param uwg  The unweighted graph that stored all the airports and flights
	 *             information.
	 * @return Journey This returns the Journey with a least connections, following
	 *         by least cost, lastly least journey time from origin airport to
	 *         destination airport.
	 * @throws FlyingPlannerException If there is no journey available from origin
	 *                                airport to destination airport.
	 */
	private Journey leastHop(String from, String to, Graph<Airport, FlightEdge> wg, Graph<Airport, FlightEdge> uwg)
			throws FlyingPlannerException {
		Airport fromA = airport(from);
		Airport toA = airport(to);
		GraphPath<Airport, FlightEdge> shortestPath = DijkstraShortestPath.findPathBetween(uwg, fromA, toA);

		if (shortestPath == null) {
			throw new FlyingPlannerException("Journey not available.");
		} else {

			AllDirectedPaths<Airport, FlightEdge> kPaths = new AllDirectedPaths<Airport, FlightEdge>(wg);
			List<GraphPath<Airport, FlightEdge>> kList = kPaths.getAllPaths(fromA, toA, true, shortestPath.getLength());
			List<Double> costList = new LinkedList<Double>();

			for (GraphPath<Airport, FlightEdge> gp : kList) {
				costList.add(gp.getWeight());
			}

			double minCost = Collections.min(costList);

			List<GraphPath<Airport, FlightEdge>> minList = new LinkedList<>();
			List<Integer> timeList = new LinkedList<>();
			for (GraphPath<Airport, FlightEdge> path : kList) {
				Journey j = new Journey(fromA, toA, path);
				if (j.totalCost() == minCost) {
					minList.add(path);
					timeList.add(j.totalTime());
				}
			}

			Journey leastHopJ;
			if (timeList.size() != 0) {
				int minTime = Collections.min(timeList);
				int index = timeList.indexOf(minTime);
				GraphPath<Airport, FlightEdge> leastHopLeastCostLeastTimePath = minList.get(index);
				leastHopJ = new Journey(fromA, toA, leastHopLeastCostLeastTimePath);
			} else {
				int index = costList.indexOf(minCost);
				GraphPath<Airport, FlightEdge> shortestLeastCostPath = kList.get(index);
				leastHopJ = new Journey(fromA, toA, shortestLeastCostPath);
			}

			return leastHopJ;
		}
	}

	/**
	 * This method returns a least connections, following by least cost, lastly
	 * least journey time flight journey from one airport (airport code) to another.
	 * 
	 * @param from The code of the origin airport.
	 * @param to   The code of the destination airport.
	 * @return Journey This returns the Journey with a least connections, following
	 *         by least cost, lastly least journey time from origin airport to
	 *         destination airport.
	 * @throws FlyingPlannerException If there is no journey available from origin
	 *                                airport to destination airport.
	 */
	@Override
	public Journey leastHop(String from, String to) throws FlyingPlannerException {
		return leastHop(from, to, costWeightedGraph, hopUnweightedGraph);
	}

	/**
	 * This method returns a least connections, following by least cost, lastly
	 * least journey time flight journey from one airport (airport code) to another,
	 * excluding a list of airport (airport codes).
	 * 
	 * @param from      The code of the origin airport.
	 * @param to        The code of the destination airport.
	 * @param excluding The list containing all the airport codes that need to be
	 *                  excluded from the journey.
	 * @return Journey This returns the Journey with a least connections, following
	 *         by least cost, lastly least journey time from origin airport to
	 *         destination airport, excluding a list of airport (airport codes).
	 * @throws FlyingPlannerException If there is no journey available from origin
	 *                                airport to destination airport, excluding a
	 *                                list of airport (airport codes).
	 */
	@Override
	public Journey leastHop(String from, String to, List<String> excluding) throws FlyingPlannerException {
		Graph<Airport, FlightEdge> cloneWeightedGraph = new SimpleDirectedWeightedGraph<>(FlightEdge.class);
		populate(airports, flights, cloneWeightedGraph, true);
		Graph<Airport, FlightEdge> cloneUnweightedGraph = new SimpleDirectedGraph<>(FlightEdge.class);
		populate(airports, flights, cloneUnweightedGraph, false);

		for (String code : excluding) {
			Airport a = airport(code);
			cloneWeightedGraph.removeVertex(a);
			cloneUnweightedGraph.removeVertex(a);
		}

		return leastHop(from, to, cloneWeightedGraph, cloneUnweightedGraph);
	}

	/**
	 * A private method returns a least journey time, following by least cost flight
	 * journey from one airport (airport code) to another from a selected weighted
	 * graph with a maximum number of connections in the journey.
	 * 
	 * @param from The code of the origin airport.
	 * @param to   The code of the destination airport.
	 * @param wg   The weighted graph that stored all the airports and flights
	 *             information.
	 * @param num  The maximum number of connections / stops that allowed to be in
	 *             the journey.
	 * @return Journey This returns the Journey with a least journey time from
	 *         origin airport to destination airport.
	 * @throws FlyingPlannerException If there is no journey available from origin
	 *                                airport to destination airport.
	 */
	private Journey leastTime(String from, String to, Graph<Airport, FlightEdge> wg, int num)
			throws FlyingPlannerException {
		Airport fromA = airport(from);
		Airport toA = airport(to);
		AllDirectedPaths<Airport, FlightEdge> kPaths = new AllDirectedPaths<Airport, FlightEdge>(wg);
		List<GraphPath<Airport, FlightEdge>> kList = kPaths.getAllPaths(fromA, toA, true, num);

		if (kList.size() == 0) {
			throw new FlyingPlannerException("Journey not available.");
		} else {
			List<Integer> durationList = new LinkedList<Integer>();
			for (GraphPath<Airport, FlightEdge> gp : kList) {
				Journey j = new Journey(fromA, toA, gp);
				durationList.add(j.totalTime());
			}

			int minTime = Collections.min(durationList);
			List<GraphPath<Airport, FlightEdge>> minList = new LinkedList<>();
			List<Integer> costList = new LinkedList<>();
			for (GraphPath<Airport, FlightEdge> path : kList) {
				Journey j = new Journey(fromA, toA, path);
				if (j.totalTime() == minTime) {
					minList.add(path);
					costList.add(j.totalCost());
				}
			}

			int minCost = Collections.min(costList);
			int index = costList.indexOf(minCost);
			GraphPath<Airport, FlightEdge> leastTimeLeastCostPath = minList.get(index);
			Journey leastTimeJ = new Journey(fromA, toA, leastTimeLeastCostPath);

			return leastTimeJ;
		}
	}

	/**
	 * This method returns a least journey time flight journey from one airport
	 * (airport code) to another.
	 * 
	 * @param from The code of the origin airport.
	 * @param to   The code of the destination airport.
	 * @param num  The maximum number of connections / stops that allowed to be in
	 *             the journey.
	 * @return Journey This returns the Journey with a least journey time from
	 *         origin airport to destination airport.
	 * @throws FlyingPlannerException If there is no journey available from origin
	 *                                airport to destination airport.
	 */
	public Journey leastTime(String from, String to, int num) throws FlyingPlannerException {
		return leastTime(from, to, costWeightedGraph, num);
	}

	/**
	 * This method returns the set of airports directly connected to the given
	 * airport. Two airports are directly connected if there exist two flights
	 * connecting them in a single hop in both direction.
	 * 
	 * @param airport The given Airport object.
	 * @return Set<Airport> This returns the set of airports directly connected to
	 *         the given airport.
	 */
	@Override
	public Set<Airport> directlyConnected(Airport airport) {
		directedAcyclicGraph = new DirectedAcyclicGraph<Airport, FlightEdge>(FlightEdge.class);
		Set<Airport> directAirport = new HashSet<Airport>();
		Collection<Airport> airportList = airports.values();
		for (Airport a : airportList) {
			directedAcyclicGraph.addVertex(a);
		}
		Collection<Flight> flightList = flights.values();
		for (Flight f : flightList) {
			Airport fromA = f.getFrom();
			Airport toA = f.getTo();
			if (fromA.getCode().equalsIgnoreCase(airport.getCode())
					|| toA.getCode().equalsIgnoreCase(airport.getCode())) {
				try {
					directedAcyclicGraph.addEdge(fromA, toA, new FlightEdge(f));
				} catch (IllegalArgumentException e) {
					if (!fromA.getCode().equalsIgnoreCase(airport.getCode())) {
						if (!directAirport.contains(fromA)) {
							directAirport.add(fromA);
						}
					} else if (!toA.getCode().equalsIgnoreCase(airport.getCode())) {
						if (!directAirport.contains(toA)) {
							directAirport.add(toA);
						}
					}
				}
			}
		}

		airport.setDicrectlyConnected(directAirport);
		return directAirport;
	}

	/**
	 * This method records, for each airport object, the airport's directly
	 * connected set and its size.
	 * 
	 * @return int This returns the sum of all airports' directly connected set
	 *         sizes.
	 */
	@Override
	public int setDirectlyConnected() {
		Collection<Airport> airportList = airports.values();
		int sum = 0;
		for (Airport a : airportList) {
			Set<Airport> set = directlyConnected(a);
			int size = set.size();
			directConnections.put(a, size);
			sum += size;
		}
		return sum;
	}

	/**
	 * This method creates a DAG containing all airports but only the flights flying
	 * to an airport destination with strictly higher number of directly connected
	 * airports than the origin airport. The {@link #setDirectlyConnected()} method
	 * must be called before this method is called.
	 * 
	 * @return int This returns the number of flights of the DAG.
	 */
	@Override
	public int setDirectlyConnectedOrder() {
		betterdirectedAcyclicGraph = new DirectedAcyclicGraph<Airport, FlightEdge>(FlightEdge.class);
		Collection<Airport> airportList = airports.values();
		for (Airport a : airportList) {
			betterdirectedAcyclicGraph.addVertex(a);
		}
		Collection<Flight> flightList = flights.values();
		for (Flight f : flightList) {
			Airport from = f.getFrom();
			int fromConnections = directConnections.get(from);
			Airport to = f.getTo();
			int toConnections = directConnections.get(to);
			if (toConnections > fromConnections) {
				betterdirectedAcyclicGraph.addEdge(from, to, new FlightEdge(f));
			}
		}
		return betterdirectedAcyclicGraph.edgeSet().size();
	}

	/**
	 * This method returns the set of airports reachable from the given airport that
	 * have strictly more direct connections. The
	 * {@link #setDirectlyConnectedOrder()} must be called before this method is
	 * called.
	 * 
	 * @return Set<Airport> This returns the set of airports reachable from the
	 *         given airport that have strictly more direct connections.
	 */
	@Override
	public Set<Airport> getBetterConnectedInOrder(Airport airport) {
		Set<Airport> betterConnected = betterdirectedAcyclicGraph.getDescendants(airport);
		airport.setDicrectlyConnectedOrder(betterConnected.size());
		return betterConnected;
	}

	/**
	 * A private method returns a list of airport codes which are the connection
	 * airports between the origin airport and destination airport with a minimum
	 * number of one connections and a given maximum number of connections in each
	 * path.
	 * 
	 * @param from The code of the origin airport.
	 * @param to   The code of the destination airport.
	 * @param wg   The weighted graph that stored all the airports and flights
	 *             information.
	 * @param num  The maximum number of connections / stops in each path.
	 * @return List<String> This returns the list of airport codes which are the
	 *         connection airports between the origin airport and destination
	 *         airport, excluding the codes of origin airport and destination
	 *         airport.
	 */
	private List<String> leastCostFixedHop(String from, String to, Graph<Airport, FlightEdge> wg, int num) {
		Airport fromA = airport(from);
		Airport toA = airport(to);
		AllDirectedPaths<Airport, FlightEdge> kPaths = new AllDirectedPaths<Airport, FlightEdge>(wg);
		List<GraphPath<Airport, FlightEdge>> kList = kPaths.getAllPaths(fromA, toA, true, num);
		List<GraphPath<Airport, FlightEdge>> kNumList = new LinkedList<GraphPath<Airport, FlightEdge>>();

		for (GraphPath<Airport, FlightEdge> gp : kList) {
			if (gp.getEdgeList().size() != 1) {
				kNumList.add(gp);
			}
		}

		List<String> codeList = new LinkedList<String>();
		for (GraphPath<Airport, FlightEdge> gp : kNumList) {
			List<Airport> list = gp.getVertexList();
			for (Airport ap : list) {
				String apStr = ap.getCode();
				if (!apStr.equalsIgnoreCase(from) && !apStr.equalsIgnoreCase(to) && !codeList.contains(apStr)) {
					codeList.add(apStr);
				}
			}
		}
		return codeList;
	}

	/**
	 * This method returns the airport code of a best airport for the meet up of two
	 * people located in two different airports (airport codes) accordingly to the
	 * journeys costs.
	 * 
	 * @param at1 The first origin airport code.
	 * @param at2 The second origin airport code.
	 * @return String This returns the code of a best airport for the meet up with
	 *         least cost journey.
	 * @throws FlyingPlannerException If there is no airport available for the meet
	 *                                up from two different airports.
	 */
	@Override
	public String leastCostMeetUp(String at1, String at2) throws FlyingPlannerException {
		List<String> codeList = new LinkedList<String>();
		int num = 0;
		int j = 0;

		do {
			codeList = leastCostFixedHop(at1, at2, costWeightedGraph, j);
			num = codeList.size();
			j++;
		} while (num < 30 && j < 6);

		List<Integer> costList = new LinkedList<Integer>();
		for (String code : codeList) {
			Journey j1 = leastCost(at1, code);
			Journey j2 = leastCost(at2, code);
			int cost = j1.totalCost() + j2.totalCost();
			costList.add(cost);
		}

		int minCost = Collections.min(costList);
		int index = costList.indexOf(minCost);
		String meetStop = codeList.get(index);

		return meetStop;
	}

	/**
	 * This method returns the airport code of a best airport for the meet up of two
	 * people located in two different airports (airport codes) accordingly to the
	 * number of connections.
	 * 
	 * @param at1 The first origin airport code.
	 * @param at2 The second origin airport code.
	 * @return String This returns the code of a best airport for the meet up with
	 *         least number of connections.
	 * @throws FlyingPlannerException If there is no airport available for the meet
	 *                                up from two different airports.
	 */
	@Override
	public String leastHopMeetUp(String at1, String at2) throws FlyingPlannerException {
		List<String> codeList = new LinkedList<String>();
		int num = 0;
		int j = 0;

		do {
			codeList = leastCostFixedHop(at1, at2, costWeightedGraph, j);
			num = codeList.size();
			j++;
		} while (num < 30 && j < 6);

		ArrayList<StopCostEntry> stopCostList = new ArrayList<StopCostEntry>();
		for (String code : codeList) {
			Journey j1 = leastHop(at1, code);
			Journey j2 = leastHop(at2, code);
			int cost = j1.totalCost() + j2.totalCost();
			int stop = j1.totalHop() + j2.totalHop();
			StopCostEntry s = new StopCostEntry(code, stop, cost);
			stopCostList.add(s);
		}

		stopCostList.sort(new StopCostSorting());
		String meetStop = stopCostList.get(0).getCode();

		return meetStop;
	}

	/**
	 * This method returns the airport code of a best airport for the earliest meet
	 * up of two people located in two different airports (airport codes) when
	 * departing at a given time.
	 * 
	 * @param at1       The first origin airport code.
	 * @param at2       The second origin airport code.
	 * @param startTime The given time for departing.
	 * @return String This returns the code of a best airport for the earliest meet
	 *         up.
	 * @throws FlyingPlannerException If there is no airport available for the meet
	 *                                up from two different airports.
	 */
	@Override
	public String leastTimeMeetUp(String at1, String at2, String startTime) throws FlyingPlannerException {
		List<String> codeList = new LinkedList<String>();
		int num = 0;
		int j = 0;

		do {
			codeList = leastCostFixedHop(at1, at2, costWeightedGraph, j);
			num = codeList.size();
			j++;
		} while (num < 30 && j < 6);

		List<Integer> timeList = new LinkedList<Integer>();
		String startTimeF = startTime.substring(0, 2) + ":" + startTime.substring(2);
		for (String code : codeList) {
			Journey j1 = leastTime(at1, code, 4);
			String ff1Str = j1.getFlights().get(0);
			Flight ff1 = flight(ff1Str);
			String ff1Time = ff1.getFromGMTime();
			int timeA1 = j1.calculateTime(startTimeF, ff1Time);
			int timeB1 = j1.totalTime();
			int time1 = timeA1 + timeB1;

			Journey j2 = leastTime(at2, code, 4);
			String ff2Str = j2.getFlights().get(0);
			Flight ff2 = flight(ff2Str);
			String ff2Time = ff2.getFromGMTime();
			int timeA2 = j2.calculateTime(startTimeF, ff2Time);
			int timeB2 = j2.totalTime();
			int time2 = timeA2 + timeB2;

			if (time1 > time2) {
				timeList.add(time1);
			} else {
				timeList.add(time2);
			}
		}

		int minTime = Collections.min(timeList);
		int index = timeList.indexOf(minTime);
		String meetStop = codeList.get(index);

		return meetStop;
	}

	/**
	 * This method returns a set of airport codes that available in the graph.
	 * 
	 * @return Set<String> This returns a set of airport codes.
	 */
	public Set<String> getAirportList() {
		Set<String> strList = airports.keySet();
		return strList;
	}

}
