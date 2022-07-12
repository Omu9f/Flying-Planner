package F28DA_CW2;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import org.junit.Before; import org.junit.Test;

import java.util.Set; import java.util.HashSet;

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
	@Test // PASSES
	public void flightJourneyTest() {
		try {
			String flightCode = "EK8042";
			Flight f = fi.flight(flightCode);
			Airport from = f.getFrom(), to = f.getTo();
			Journey j = fi.leastCost(from.getCode(), to.getCode());
			int totalTime = j.totalTime();
			assertEquals(201, totalTime);
			System.out.println(totalTime);
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	@Test // PASSES
	public void leastTimeTest1() {
		try {
			Journey j = fi.leastTime("NCL", "NTL");
			assertEquals(4, j.totalHop());
			assertEquals(1182, j.totalCost());
			assertEquals(1060, j.airTime());
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	@Test // PASSES
	public void leastTimeTest2() {
		try {
			Journey j = fi.leastTime("GIG", "DXB");
			assertEquals(2, j.totalHop());
			assertEquals(885, j.totalCost());
			assertEquals(774, j.airTime());
		} catch (FlyingPlannerException e) {
			fail();
		}
	}
	
	@Test // PASSES
	public void leastCostTest() {
		try {
			Journey j = fi.leastCost("NCL", "NTL");
			assertEquals(4, j.totalHop());
			assertEquals(1035, j.totalCost());

			j = fi.leastCost("DXB", "ZRH");
			assertEquals(2, j.totalHop());
			assertEquals(316, j.totalCost());

			j = fi.leastCost("INL", "KRT");
			assertEquals(4, j.totalHop());
			assertEquals(749, j.totalCost());

			j = fi.leastCost("CLE", "CLL");
			assertEquals(2, j.totalHop());
			assertEquals(129, j.totalCost());

			j = fi.leastCost("LHR", "AMD");
			assertEquals(3, j.totalHop());
			assertEquals(466, j.totalCost());

			j = fi.leastCost("ROB", "ROA");
			assertEquals(4, j.totalHop());
			assertEquals(711, j.totalCost());
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	@Test // PASSES
	public void leastHopTest() {
		try {
			Journey j = fi.leastHop("NCL", "NTL");
			assertEquals(3, j.totalHop());

			j = fi.leastHop("DXB", "ZRH");
			assertEquals(1, j.totalHop());

			j = fi.leastHop("INL", "KRT");
			assertEquals(4, j.totalHop());

			j = fi.leastHop("CLE", "CLL");
			assertEquals(2, j.totalHop());

			j = fi.leastHop("LHR", "AMD");
			assertEquals(2, j.totalHop());

			j = fi.leastHop("ROB", "ROA");
			assertEquals(3, j.totalHop());
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	@Test // PASSES
	public void directlyConnectedTest() {
		Airport a = fi.airport("HGH");
		Set<Airport> s = fi.directlyConnected(a);
		assertEquals(39, s.size());

		a = fi.airport("BSB");
	 	s = fi.directlyConnected(a);
		assertEquals(32, s.size());

		a = fi.airport("VAN");
	 	s = fi.directlyConnected(a);
		assertEquals(3, s.size());

		a = fi.airport("DLC");
	 	s = fi.directlyConnected(a);
		assertEquals(32, s.size());

		a = fi.airport("PRG");
	 	s = fi.directlyConnected(a);
		assertEquals(10, s.size());

		a = fi.airport("EDI");
	 	s = fi.directlyConnected(a);
		assertEquals(9, s.size());
	}

	@Test // PASSES
	public void betterConnectedInOrderTest() {
		Airport a = fi.airport("NCL");
		Set<Airport> betterConnected = fi.getBetterConnectedInOrder(a);
		assertEquals(24, betterConnected.size());

		a = fi.airport("NTL");
		betterConnected = fi.getBetterConnectedInOrder(a);
		assertEquals(32, betterConnected.size());

		a = fi.airport("BSB");
		betterConnected = fi.getBetterConnectedInOrder(a);
		assertEquals(22, betterConnected.size());

		a = fi.airport("LHR");
		betterConnected = fi.getBetterConnectedInOrder(a);
		assertEquals(8, betterConnected.size());
	}

	@Test // PASSES
	public void setDirectlyConnectedOrderTest() {
		int numOfFlights = fi.setDirectlyConnectedOrder();
		assertEquals(4756, numOfFlights);
	}
	
	@Test // PASSES
	public void betterConnectedInOrderLGWTest() {
		Airport lgw = fi.airport("LGW");
		Set<Airport> betterConnected = fi.getBetterConnectedInOrder(lgw);
		assertEquals(24, betterConnected.size());
	}

	@Test // PASSES
	public void leastCostMeetUpTest() {
		try {
			String meetUp = fi.leastCostMeetUp("NCL", "NTL");
			assertEquals("AMS", meetUp);
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	@Test // PASSES
	public void leastHopMeetUpTest() {
		try {
			String meetUp = fi.leastHopMeetUp("NCL", "NTL");
			assertEquals("DXB", meetUp);
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	@Test // PASSES
	public void leastTimeMeetUpTest() {
		try {
			String meetUp = fi.leastTimeMeetUp("IAH", "BEY", "0600");
			assertEquals("CDG", meetUp);
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	@Test // PASSES
	public void leastCostCustomTest() {
		FlyingPlanner fp = new FlyingPlanner();
		
		HashSet<String[]> airports = new HashSet<String[]>();
		String[] a1 = {"A1","City1","AirportName1"}; airports.add(a1);
		String[] a2 = {"A2","City2","AirportName2"}; airports.add(a2);
		String[] a3 = {"A3","City3","AirportName3"}; airports.add(a3);
		String[] a4 = {"A4","City4","AirportName4"}; airports.add(a4);
		String[] a5 = {"A5","City5","AirportName5"}; airports.add(a5);
		
		HashSet<String[]>  flights = new HashSet<String[]>();
		String[] f1 = {"F1","A1","1000","A2","1100","99"}; flights.add(f1);
		String[] f2 = {"F2","A1","1000","A3","1100","42"}; flights.add(f2);
		String[] f3 = {"F3","A3","1000","A2","1100","27"}; flights.add(f3);
		String[] f4 = {"F4","A4","1000","A1","1100","83"}; flights.add(f4);
		String[] f5 = {"F5","A4","1000","A3","1100","21"}; flights.add(f5);
		String[] f6 = {"F6","A3","1000","A5","1100","18"}; flights.add(f6);
		String[] f7 = {"F7","A5","1000","A1","1100","20"}; flights.add(f7);
		
		try {
			boolean check = fp.populate(airports, flights);
			assertTrue(check);

			Journey lc = fp.leastCost("A1", "A2");
			assertEquals(69, lc.totalCost());

			lc = fp.leastCost("A4", "A1");
			assertEquals(59, lc.totalCost());
		} catch (FlyingPlannerException e) {
			e.printStackTrace();
			fail();
		}
	}

}
