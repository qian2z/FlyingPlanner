package F28DA_CW2;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class FlyingPlannerMainPartA {

	private static Graph<String, DefaultWeightedEdge> g = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

	public static void addAirport(String airport) {
		g.addVertex(airport);
	}

	public static void addBidirectionalFlight(String a1, String a2, double cost) {
		g.addEdge(a1, a2);
		g.setEdgeWeight(a1, a2, cost);
		g.addEdge(a2, a1);
		g.setEdgeWeight(a2, a1, cost);
	}

	public static Graph<String, DefaultWeightedEdge> getGraph() {
		return g;
	}

	public static void main(String[] args) {

//      The following code is from HelloJGraphT.java of the org.jgrapth.demo package
//		System.err.println("The example code is from HelloJGraphT.java from the org.jgrapt.demo package.");
//		System.err.println("Use similar code to build the small graph from Part A by hand.");
//		System.err.println("Note that you will need to use a different graph class as your graph is not just a Simple Graph.");
//		System.err.println("Once you understand how to build such graph by hand, move to Part B to build a more substantial graph.");
//      Code is from HelloJGraphT.java of the org.jgrapth.demo package (start)

		String a1 = "Edinburgh";
		String a2 = "Heathrow";
		String a3 = "Dubai";
		String a4 = "Sydney";
		String a5 = "Kuala Lumpur";

		/**
		 * Adding airports.
		 */

		addAirport(a1);
		addAirport(a2);
		addAirport(a3);
		addAirport(a4);
		addAirport(a5);

		/**
		 * Adding flights between airports.
		 */

		// Edinburgh a1 <-> Heathrow a2 (80)
		addBidirectionalFlight(a1, a2, 80);
		// Heathrow a2 <-> Dubai a3 (130)
		addBidirectionalFlight(a2, a3, 130);
		// Heathrow a2 <-> Sydney a4 (570)
		addBidirectionalFlight(a2, a4, 570);
		// Dubai a3 <-> Kuala Lumpur a5 (170)
		addBidirectionalFlight(a3, a5, 170);
		// Dubai a3 <-> Edinburgh a1 (190)
		addBidirectionalFlight(a3, a1, 190);
		// Kuala Lumpur a5 <-> Sydney a4 (150)
		addBidirectionalFlight(a5, a4, 150);

		/**
		 * Get all the airports that available in the graph.
		 */
		Set<String> vertexSet = g.vertexSet();

		/**
		 * Scanner to get user input.
		 */
		Scanner scan = new Scanner(System.in);

		String continueStr;
		do {
			// redefined printing
			System.out.println();
			System.out.println("Welcome to Flying Planner Main Part A!");
			System.out.println();
			System.out.println("The following airports are used: ");
			for (String airport : vertexSet) {
				System.out.println("\t" + airport);
			}
			String errStr = "Invalid airport. Please enter again!";
			String startStr = "Please enter the start airport: ";
			System.out.println();
			System.out.print(startStr);
			String startAP = scan.nextLine();
			while (!vertexSet.contains(startAP)) {
				System.err.println(errStr);
				System.out.print(startStr);
				startAP = scan.nextLine();
			}
			String desStr = "Please enter the destination airport: ";
			System.out.print(desStr);
			String desAP = scan.nextLine();
			while (!vertexSet.contains(desAP)) {
				System.err.println(errStr);
				System.out.print(desStr);
				desAP = scan.nextLine();
			}
			GraphPath<String, DefaultWeightedEdge> shortestPath = DijkstraShortestPath.findPathBetween(getGraph(),
					startAP, desAP);
			List<String> vertexList = shortestPath.getVertexList();
			System.out.println();
			if (vertexList.size() > 1) {
				System.out.println("Shortest (i.e. cheapest) path: ");
				for (int i = 0; i < vertexList.size() - 1; i++) {
					String startAirport = vertexList.get(i);
					String desAirport = vertexList.get(i + 1);
					System.out.println((i + 1) + ". " + startAirport + " -> " + desAirport);
				}
				double cost = shortestPath.getWeight();
				System.out.println("Cost of shortest (i.e. cheapest) path = £ " + cost);

			} else {
				System.out.println("Sorry, the journey is not available.");
			}

			System.out.println();
			System.out.print("Do you wish to continue? Enter y for yes: ");
			continueStr = scan.nextLine();

		} while (continueStr.equalsIgnoreCase("y"));

	}

}
