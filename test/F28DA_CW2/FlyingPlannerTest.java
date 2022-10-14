package F28DA_CW2;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class FlyingPlannerTest {

	FlyingPlanner fi;

	@Before
	public void initialize() {
		fi = new FlyingPlanner();
		try {
			fi.populate(new FlightsReader());
		} catch (FileNotFoundException | FlyingPlannerException e) {
			e.printStackTrace();
		}
	}

	// Add your own tests here
	// You can get inspiration from FlyingPlannerProvidedTest

	/**
	 * Part A Least Cost, Least Hops, Least Journey Time, Journey Duration Origin
	 * Airport = PET, Destination Airport = KUL
	 */

	@Test
	public void leastCostTest() {
		try {
			Journey i = fi.leastCost("PET", "KUL");
			assertEquals(5, i.totalHop());
			assertEquals(1200, i.totalCost());
			int airTime = 1380;
			assertEquals(airTime, i.airTime());
			int connectingTime = 2219;
			assertEquals(connectingTime, i.connectingTime());
			assertEquals(airTime + connectingTime, i.totalTime());
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	@Test
	public void leastHopTest() {
		try {
			Journey i = fi.leastHop("PET", "KUL");
			assertEquals(4, i.totalHop());
			assertEquals(1234, i.totalCost());
			int airTime = 1333;
			assertEquals(airTime, i.airTime());
			int connectingTime = 1636;
			assertEquals(connectingTime, i.connectingTime());
			assertEquals(airTime + connectingTime, i.totalTime());
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	@Test
	public void leastTimeTestA() {
		try {
			Journey i = fi.leastTime("PET", "KUL", 3);
			i.getStops();
			System.out.println(i.getStops());
			fail();
		} catch (FlyingPlannerException e) {
			assertTrue(true);
		}
	}

	@Test
	public void leastTimeTestB() {
		try {
			Journey i = fi.leastTime("PET", "KUL", 4);
			assertEquals(4, i.totalHop());
			assertEquals(1354, i.totalCost());
			int airTime = 1242;
			assertEquals(airTime, i.airTime());
			int connectingTime = 1713;
			assertEquals(connectingTime, i.connectingTime());
			assertEquals(airTime + connectingTime, i.totalTime());
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	@Test
	public void leastTimeTestC() {
		try {
			Journey i = fi.leastTime("PET", "KUL", 5);
			assertEquals(4, i.totalHop());
			assertEquals(1354, i.totalCost());
			int airTime = 1242;
			assertEquals(airTime, i.airTime());
			int connectingTime = 1713;
			assertEquals(connectingTime, i.connectingTime());
			assertEquals(airTime + connectingTime, i.totalTime());
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	@Test
	public void leastCostExcludingTestA() {
		List<String> excludingList = new LinkedList<String>();
		excludingList.add("CDG");
		excludingList.add("BKK");
		try {
			Journey j1 = fi.leastCost("PET", "KUL");
			assertEquals(5, j1.totalHop());
			assertEquals(1200, j1.totalCost());
			List<String> j1Stops = j1.getStops();
			assertTrue(j1Stops.contains(excludingList.get(0)));
			assertTrue(j1Stops.contains(excludingList.get(1)));

			Journey j2 = fi.leastCost("PET", "KUL", excludingList);
			assertEquals(4, j2.totalHop());
			assertEquals(1234, j2.totalCost());
			List<String> j2Stops = j2.getStops();
			assertFalse(j2Stops.contains(excludingList.get(0)));
			assertFalse(j2Stops.contains(excludingList.get(1)));
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	@Test
	public void leastCostExcludingTestB() {
		List<String> excludingList = new LinkedList<String>();
		excludingList.add("CDG");
		excludingList.add("BKK");
		excludingList.add("POA");
		excludingList.add("GRU");
		try {
			Journey j = fi.leastCost("PET", "KUL", excludingList);
			j.getStops();
			System.out.println(j.getStops());
			fail();
		} catch (FlyingPlannerException e) {
			assertTrue(true);
		}
	}

	@Test
	public void leastHopExcludingTestA() {
		List<String> excludingList = new LinkedList<String>();
		excludingList.add("GRU");
		excludingList.add("IST");
		try {
			Journey j1 = fi.leastHop("PET", "KUL");
			assertEquals(4, j1.totalHop());
			assertEquals(1234, j1.totalCost());
			List<String> j1Stops = j1.getStops();
			assertTrue(j1Stops.contains(excludingList.get(0)));
			assertTrue(j1Stops.contains(excludingList.get(1)));

			Journey j2 = fi.leastHop("PET", "KUL", excludingList);
			assertEquals(4, j2.totalHop());
			assertEquals(1269, j2.totalCost());
			List<String> j2Stops = j2.getStops();
			assertFalse(j2Stops.contains(excludingList.get(0)));
			assertFalse(j2Stops.contains(excludingList.get(1)));
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	@Test
	public void leastHopExcludingTestB() {
		List<String> excludingList = new LinkedList<String>();
		excludingList.add("CDG");
		excludingList.add("BKK");
		excludingList.add("POA");
		excludingList.add("GRU");
		try {
			Journey j = fi.leastHop("PET", "KUL", excludingList);
			j.getStops();
			System.out.println(j.getStops());
			fail();
		} catch (FlyingPlannerException e) {
			assertTrue(true);
		}
	}

	/**
	 * Part B Directly Connected Order
	 */

	@Test
	public void directlyConnectedTestA() {
		Airport dxb = fi.airport("DXB");
		Set<Airport> s = fi.directlyConnected(dxb);
		assertEquals(120, s.size());
	}

	@Test
	public void directlyConnectedTestB() {
		Airport edi = fi.airport("EDI");
		Set<Airport> s = fi.directlyConnected(edi);
		assertEquals(9, s.size());
	}

	/** Correction Test for Provided Test */
	@Test
	public void betterConnectedInOrderEDITest() {
		fi.setDirectlyConnected();
		fi.setDirectlyConnectedOrder();
		Airport edi = fi.airport("EDI");
		Set<Airport> betterConnected = fi.getBetterConnectedInOrder(edi);
		assertEquals(32, betterConnected.size());
	}

	/**
	 * Part C Meet-Up Search
	 */

	@Test
	public void leastCostMeetUpTest() {
		try {
			String meetUp = fi.leastCostMeetUp("PET", "KUL");
			assertEquals("GIG", meetUp);
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	@Test
	public void leastHopMeetUpTest() {
		try {
			String meetUp = fi.leastHopMeetUp("PET", "KUL");
			assertEquals("IST", meetUp);
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	@Test
	public void leastTimeMeetUpTest() {
		try {
			String meetUp = fi.leastTimeMeetUp("PET", "KUL", "0800");
			assertEquals("LIM", meetUp);
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	/** Correction Test for Provided Test */
	@Test
	public void leastTimeMeetUpTestA() {
		try {
			String meetUp = fi.leastTimeMeetUp("NCL", "NTL", "0600");
			assertEquals("BKK", meetUp);
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	/**
	 * Part C Custom Graph Test
	 */

	@Test
	public void leastCostCustomTest1() {
		/* Only one least cost path */
		FlyingPlanner fp = new FlyingPlanner();
		HashSet<String[]> airports = new HashSet<String[]>();
		String[] a1 = { "A1", "City1", "AirportName1" };
		airports.add(a1);
		String[] a2 = { "A2", "City2", "AirportName2" };
		airports.add(a2);
		String[] a3 = { "A3", "City3", "AirportName3" };
		airports.add(a3);
		HashSet<String[]> flights = new HashSet<String[]>();
		String[] f1 = { "F1", "A1", "1000", "A2", "1100", "500" };
		flights.add(f1);
		String[] f2 = { "F2", "A1", "1000", "A3", "1100", "50" };
		flights.add(f2);
		String[] f3 = { "F3", "A3", "1000", "A2", "1100", "50" };
		flights.add(f3);
		fp.populate(airports, flights);
		try {
			Journey lc = fp.leastCost("A1", "A2");
			assertEquals(100, lc.totalCost());
			assertEquals(2, lc.totalHop());
		} catch (FlyingPlannerException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void leastCostCustomTest2() {
		/*
		 * Two least cost paths with different number of hops, the path with least hops
		 * will be generated.
		 */
		FlyingPlanner fp = new FlyingPlanner();
		HashSet<String[]> airports = new HashSet<String[]>();
		String[] a1 = { "A1", "City1", "AirportName1" };
		airports.add(a1);
		String[] a2 = { "A2", "City2", "AirportName2" };
		airports.add(a2);
		String[] a3 = { "A3", "City3", "AirportName3" };
		airports.add(a3);
		HashSet<String[]> flights = new HashSet<String[]>();
		String[] f1 = { "F1", "A1", "1000", "A2", "1100", "100" };
		flights.add(f1);
		String[] f2 = { "F2", "A1", "1000", "A3", "1100", "50" };
		flights.add(f2);
		String[] f3 = { "F3", "A3", "1000", "A2", "1100", "50" };
		flights.add(f3);
		fp.populate(airports, flights);
		try {
			Journey lc = fp.leastCost("A1", "A2");
			assertEquals(100, lc.totalCost());
			assertEquals(1, lc.totalHop());
			List<String> flightCode = lc.getFlights();
			assertEquals("F1", flightCode.get(0));
		} catch (FlyingPlannerException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void leastCostCustomTest3() {
		/*
		 * Two least cost paths with same number of hops but with different total
		 * journey time, the path with least journey time will be generated.
		 */
		FlyingPlanner fp = new FlyingPlanner();
		HashSet<String[]> airports = new HashSet<String[]>();
		String[] a1 = { "A1", "City1", "AirportName1" };
		airports.add(a1);
		String[] a2 = { "A2", "City2", "AirportName2" };
		airports.add(a2);
		String[] a3 = { "A3", "City3", "AirportName3" };
		airports.add(a3);
		String[] a4 = { "A4", "City4", "AirportName4" };
		airports.add(a4);
		HashSet<String[]> flights = new HashSet<String[]>();
		String[] f1 = { "F1", "A1", "1000", "A4", "1100", "50" };
		flights.add(f1);
		String[] f2 = { "F2", "A1", "1000", "A3", "1100", "50" };
		flights.add(f2);
		String[] f3 = { "F3", "A3", "1000", "A2", "1100", "50" };
		flights.add(f3);
		String[] f4 = { "F4", "A4", "1000", "A2", "1030", "50" };
		flights.add(f4);
		fp.populate(airports, flights);
		try {
			Journey lc = fp.leastCost("A1", "A2");
			assertEquals(100, lc.totalCost());
			assertEquals(2, lc.totalHop());
			List<String> flightCode = lc.getFlights();
			assertEquals("F1", flightCode.get(0));
			assertEquals("F4", flightCode.get(1));
			assertEquals(1470, lc.totalTime());
		} catch (FlyingPlannerException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void leastHopCustomTest1() {
		/* Only one least hop path */
		FlyingPlanner fp = new FlyingPlanner();
		HashSet<String[]> airports = new HashSet<String[]>();
		String[] a1 = { "A1", "City1", "AirportName1" };
		airports.add(a1);
		String[] a2 = { "A2", "City2", "AirportName2" };
		airports.add(a2);
		String[] a3 = { "A3", "City3", "AirportName3" };
		airports.add(a3);
		HashSet<String[]> flights = new HashSet<String[]>();
		String[] f1 = { "F1", "A1", "1000", "A2", "1100", "500" };
		flights.add(f1);
		String[] f2 = { "F2", "A1", "1000", "A3", "1100", "50" };
		flights.add(f2);
		String[] f3 = { "F3", "A3", "1000", "A2", "1100", "50" };
		flights.add(f3);
		fp.populate(airports, flights);
		try {
			Journey lc = fp.leastHop("A1", "A2");
			assertEquals(500, lc.totalCost());
			assertEquals(1, lc.totalHop());
		} catch (FlyingPlannerException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void leastHopCustomTest2() {
		/*
		 * Two least hops paths with different journey cost, the path with least cost
		 * will be generated.
		 */
		FlyingPlanner fp = new FlyingPlanner();
		HashSet<String[]> airports = new HashSet<String[]>();
		String[] a1 = { "A1", "City1", "AirportName1" };
		airports.add(a1);
		String[] a2 = { "A2", "City2", "AirportName2" };
		airports.add(a2);
		String[] a3 = { "A3", "City3", "AirportName3" };
		airports.add(a3);
		String[] a4 = { "A4", "City4", "AirportName4" };
		airports.add(a4);
		HashSet<String[]> flights = new HashSet<String[]>();
		String[] f1 = { "F1", "A1", "1000", "A4", "1100", "50" };
		flights.add(f1);
		String[] f2 = { "F2", "A1", "1000", "A3", "1100", "50" };
		flights.add(f2);
		String[] f3 = { "F3", "A3", "1000", "A2", "1100", "50" };
		flights.add(f3);
		String[] f4 = { "F4", "A4", "1000", "A2", "1100", "49" };
		flights.add(f4);
		fp.populate(airports, flights);
		try {
			Journey lc = fp.leastHop("A1", "A2");
			assertEquals(99, lc.totalCost());
			assertEquals(2, lc.totalHop());
			List<String> flightCode = lc.getFlights();
			assertEquals("F1", flightCode.get(0));
			assertEquals("F4", flightCode.get(1));
		} catch (FlyingPlannerException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void leastHopCustomTest3() {
		/*
		 * Two least hop paths with same journey cost but with different total journey
		 * time, the path with least journey time will be generated.
		 */
		FlyingPlanner fp = new FlyingPlanner();
		HashSet<String[]> airports = new HashSet<String[]>();
		String[] a1 = { "A1", "City1", "AirportName1" };
		airports.add(a1);
		String[] a2 = { "A2", "City2", "AirportName2" };
		airports.add(a2);
		String[] a3 = { "A3", "City3", "AirportName3" };
		airports.add(a3);
		String[] a4 = { "A4", "City4", "AirportName4" };
		airports.add(a4);
		HashSet<String[]> flights = new HashSet<String[]>();
		String[] f1 = { "F1", "A1", "1000", "A4", "1100", "50" };
		flights.add(f1);
		String[] f2 = { "F2", "A1", "1000", "A3", "1030", "50" };
		flights.add(f2);
		String[] f3 = { "F3", "A3", "1100", "A2", "1130", "50" };
		flights.add(f3);
		String[] f4 = { "F4", "A4", "1200", "A2", "1300", "50" };
		flights.add(f4);
		fp.populate(airports, flights);
		try {
			Journey lc = fp.leastHop("A1", "A2");
			assertEquals(100, lc.totalCost());
			assertEquals(2, lc.totalHop());
			List<String> flightCode = lc.getFlights();
			assertEquals("F2", flightCode.get(0));
			assertEquals("F3", flightCode.get(1));
			assertEquals(90, lc.totalTime());
		} catch (FlyingPlannerException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void leastTimeCustomTest1() {
		/* Only one least journey path */
		FlyingPlanner fp = new FlyingPlanner();
		HashSet<String[]> airports = new HashSet<String[]>();
		String[] a1 = { "A1", "City1", "AirportName1" };
		airports.add(a1);
		String[] a2 = { "A2", "City2", "AirportName2" };
		airports.add(a2);
		String[] a3 = { "A3", "City3", "AirportName3" };
		airports.add(a3);
		HashSet<String[]> flights = new HashSet<String[]>();
		String[] f1 = { "F1", "A1", "1000", "A2", "1100", "500" };
		flights.add(f1);
		String[] f2 = { "F2", "A1", "1000", "A3", "1100", "50" };
		flights.add(f2);
		String[] f3 = { "F3", "A3", "1000", "A2", "1100", "50" };
		flights.add(f3);
		fp.populate(airports, flights);
		try {
			Journey lc = fp.leastHop("A1", "A2");
			assertEquals(500, lc.totalCost());
			assertEquals(60, lc.totalTime());
		} catch (FlyingPlannerException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void leastTimeCustomTest2() {
		/*
		 * Two least journey time paths with different journey cost, the path with least
		 * cost will be generated.
		 */
		FlyingPlanner fp = new FlyingPlanner();
		HashSet<String[]> airports = new HashSet<String[]>();
		String[] a1 = { "A1", "City1", "AirportName1" };
		airports.add(a1);
		String[] a2 = { "A2", "City2", "AirportName2" };
		airports.add(a2);
		String[] a3 = { "A3", "City3", "AirportName3" };
		airports.add(a3);
		String[] a4 = { "A4", "City4", "AirportName4" };
		airports.add(a4);
		HashSet<String[]> flights = new HashSet<String[]>();
		String[] f1 = { "F1", "A1", "1000", "A4", "1100", "50" };
		flights.add(f1);
		String[] f2 = { "F2", "A1", "1000", "A3", "1100", "49" };
		flights.add(f2);
		String[] f3 = { "F3", "A3", "1000", "A2", "1100", "50" };
		flights.add(f3);
		String[] f4 = { "F4", "A4", "1000", "A2", "1100", "50" };
		flights.add(f4);
		fp.populate(airports, flights);
		try {
			Journey lc = fp.leastHop("A1", "A2");
			assertEquals(99, lc.totalCost());
			assertEquals(1500, lc.totalTime());
			List<String> flightCode = lc.getFlights();
			assertEquals("F2", flightCode.get(0));
			assertEquals("F3", flightCode.get(1));
		} catch (FlyingPlannerException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void directlyConnectedCustomTest() {
		FlyingPlanner fp = new FlyingPlanner();
		HashSet<String[]> airports = new HashSet<String[]>();
		String[] a1 = { "A1", "City1", "AirportName1" };
		airports.add(a1);
		String[] a2 = { "A2", "City2", "AirportName2" };
		airports.add(a2);
		String[] a3 = { "A3", "City3", "AirportName3" };
		airports.add(a3);
		HashSet<String[]> flights = new HashSet<String[]>();
		String[] f1 = { "F1", "A1", "1000", "A2", "1100", "500" };
		flights.add(f1);
		String[] f2 = { "F2", "A2", "1000", "A3", "1100", "50" };
		flights.add(f2);
		String[] f3 = { "F3", "A2", "1000", "A1", "1100", "50" };
		flights.add(f3);
		fp.populate(airports, flights);

		Airport ap1 = fp.airport("A1");
		Set<Airport> s1 = fp.directlyConnected(ap1);
		assertEquals(1, s1.size());
		Airport ap2 = fp.airport("A2");
		Set<Airport> s2 = fp.directlyConnected(ap2);
		assertEquals(1, s2.size());
		Airport ap3 = fp.airport("A3");
		Set<Airport> s3 = fp.directlyConnected(ap3);
		assertEquals(0, s3.size());
	}

	@Test
	public void setDirectlyConnectedCustomTest() {
		FlyingPlanner fp = new FlyingPlanner();
		HashSet<String[]> airports = new HashSet<String[]>();
		String[] a1 = { "A1", "City1", "AirportName1" };
		airports.add(a1);
		String[] a2 = { "A2", "City2", "AirportName2" };
		airports.add(a2);
		String[] a3 = { "A3", "City3", "AirportName3" };
		airports.add(a3);
		HashSet<String[]> flights = new HashSet<String[]>();
		String[] f1 = { "F1", "A1", "1000", "A2", "1100", "500" };
		flights.add(f1);
		String[] f2 = { "F2", "A2", "1000", "A3", "1100", "50" };
		flights.add(f2);
		String[] f3 = { "F3", "A2", "1000", "A1", "1100", "50" };
		flights.add(f3);
		fp.populate(airports, flights);

		int sum = fp.setDirectlyConnected();
		assertEquals(2, sum);
	}

	@Test
	public void leastCostMeetUpCustomTest() {
		FlyingPlanner fp = new FlyingPlanner();
		HashSet<String[]> airports = new HashSet<String[]>();
		String[] a1 = { "A1", "City1", "AirportName1" };
		airports.add(a1);
		String[] a2 = { "A2", "City2", "AirportName2" };
		airports.add(a2);
		String[] a3 = { "A3", "City3", "AirportName3" };
		airports.add(a3);
		String[] a4 = { "A4", "City4", "AirportName4" };
		airports.add(a4);
		HashSet<String[]> flights = new HashSet<String[]>();
		String[] f1 = { "F1", "A1", "1000", "A4", "1100", "50" };
		flights.add(f1);
		String[] f2 = { "F2", "A1", "1000", "A3", "1100", "49" };
		flights.add(f2);
		String[] f3 = { "F3", "A2", "1000", "A4", "1100", "50" };
		flights.add(f3);
		String[] f4 = { "F4", "A2", "1000", "A3", "1100", "50" };
		flights.add(f4);
		String[] f5 = { "F5", "A4", "1000", "A2", "1100", "50" };
		flights.add(f5);
		String[] f6 = { "F6", "A3", "1000", "A2", "1100", "50" };
		flights.add(f6);
		fp.populate(airports, flights);
		try {
			String meetUp = fp.leastCostMeetUp("A1", "A2");
			assertEquals("A3", meetUp);
		} catch (FlyingPlannerException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void leastHopMeetUpCustomTest() {
		FlyingPlanner fp = new FlyingPlanner();
		HashSet<String[]> airports = new HashSet<String[]>();
		String[] a1 = { "A1", "City1", "AirportName1" };
		airports.add(a1);
		String[] a2 = { "A2", "City2", "AirportName2" };
		airports.add(a2);
		String[] a3 = { "A3", "City3", "AirportName3" };
		airports.add(a3);
		String[] a4 = { "A4", "City4", "AirportName4" };
		airports.add(a4);
		String[] a5 = { "A5", "City5", "AirportName5" };
		airports.add(a5);
		HashSet<String[]> flights = new HashSet<String[]>();
		String[] f1 = { "F1", "A1", "1000", "A3", "1100", "50" };
		flights.add(f1);
		String[] f2 = { "F2", "A1", "1000", "A4", "1100", "50" };
		flights.add(f2);
		String[] f3 = { "F3", "A2", "1000", "A4", "1100", "50" };
		flights.add(f3);
		String[] f4 = { "F4", "A3", "1000", "A5", "1100", "50" };
		flights.add(f4);
		String[] f5 = { "F5", "A4", "1000", "A5", "1100", "50" };
		flights.add(f5);
		String[] f6 = { "F6", "A5", "1000", "A2", "1100", "50" };
		flights.add(f6);
		String[] f7 = { "F7", "A2", "1000", "A5", "1100", "50" };
		flights.add(f7);
		String[] f8 = { "F8", "A5", "1000", "A3", "1100", "50" };
		flights.add(f8);
		String[] f9 = { "F9", "A4", "1000", "A2", "1100", "50" };
		flights.add(f9);
		fp.populate(airports, flights);
		try {
			String meetUp = fp.leastCostMeetUp("A1", "A2");
			assertEquals("A4", meetUp);
		} catch (FlyingPlannerException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void leastTimeMeetUpCustomTest() {
		FlyingPlanner fp = new FlyingPlanner();
		HashSet<String[]> airports = new HashSet<String[]>();
		String[] a1 = { "A1", "City1", "AirportName1" };
		airports.add(a1);
		String[] a2 = { "A2", "City2", "AirportName2" };
		airports.add(a2);
		String[] a3 = { "A3", "City3", "AirportName3" };
		airports.add(a3);
		String[] a4 = { "A4", "City4", "AirportName4" };
		airports.add(a4);
		String[] a5 = { "A5", "City5", "AirportName5" };
		airports.add(a5);
		HashSet<String[]> flights = new HashSet<String[]>();
		String[] f1 = { "F1", "A1", "1000", "A3", "1030", "50" };
		flights.add(f1);
		String[] f2 = { "F2", "A1", "1000", "A4", "1300", "50" };
		flights.add(f2);
		String[] f3 = { "F3", "A2", "1000", "A4", "1400", "50" };
		flights.add(f3);
		String[] f4 = { "F4", "A3", "1100", "A5", "1230", "50" };
		flights.add(f4);
		String[] f5 = { "F5", "A4", "1000", "A5", "1100", "50" };
		flights.add(f5);
		String[] f6 = { "F6", "A5", "1000", "A2", "1100", "50" };
		flights.add(f6);
		String[] f7 = { "F7", "A2", "1000", "A5", "1030", "50" };
		flights.add(f7);
		String[] f8 = { "F8", "A5", "1000", "A3", "1100", "50" };
		flights.add(f8);
		String[] f9 = { "F9", "A4", "1000", "A2", "1100", "50" };
		flights.add(f9);
		fp.populate(airports, flights);
		try {
			String meetUp = fp.leastTimeMeetUp("A1", "A2", "1030");
			assertEquals("A5", meetUp);
		} catch (FlyingPlannerException e) {
			e.printStackTrace();
			fail();
		}
	}

}
