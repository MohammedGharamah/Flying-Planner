package F28DA_CW2;

import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Riffat Khan (H00328074)
 * Additional test cases.
 */

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

	/** 
	 * Test to check if the right number of stops and flight codes are returned
	 */
	@Test 
	public void getStopsFlightsTest() {
		try {
			Journey journey = fi.leastCost("EDI", "DXB");
			assertEquals(journey.getStops().size(), 4);
			assertEquals(journey.getFlights().size(), 3);

			journey = fi.leastHop("EDI", "DXB");
			assertEquals(journey.getStops().size(), 3);
			assertEquals(journey.getFlights().size(), 2);
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	/**
	 * Test to check if the total time is the same as airtime
	 * if there is a direct flight between the two airports
	 */
	@Test
	public void myLeastCostTimeTest() {
		try {
			Journey i = fi.leastCost("GIG", "DXB");
			assertEquals(860, i.airTime());
			assertEquals(0, i.connectingTime());
			assertEquals(860, i.totalTime());
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	/**
	 * Test to check if the correct airtime, connecting time 
	 * and total time is returned for least hops journey
	 */
	@Test
	public void leastHopTimeTest() {
		try {
			Journey i = fi.leastHop("NTL", "CTG");
			assertEquals(1051, i.airTime());
			assertEquals(3415, i.connectingTime());
			assertEquals(4466, i.totalTime());
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	/**
	 * Test to check if airports in the exclude list are 
	 * not included in the journey
	 */
	@Test
	public void leastHopExclTest() {
		try {
			List<String> exclude = new ArrayList<String>();
			exclude.add("LHR");
			exclude.add("PRG");
			exclude.add("LGW");
			exclude.add("LCY");
			exclude.add("DUS");
			exclude.add("FRA");
			exclude.add("WAW");
			exclude.add("AMS");
			exclude.add("CDG");
			exclude.add("IST");
			exclude.add("GLA");
			exclude.add("CWL");
			exclude.add("EWR");
			exclude.add("BOS");
			Journey i = fi.leastHop("DXB", "EDI", exclude);
			System.out.println(i.getStops());
			fail();
		} catch (FlyingPlannerException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test to check if the correct number of directly 
	 * connected (bidirectional) airports are returned
	 * and check for one of the correct airports in 
	 * the directly connected list
	 */
	@Test
	public void directlyConnectedNCLTest() {
		Airport ncl = fi.airport("NCL");
		Set<Airport> s = fi.directlyConnected(ncl);
		assertEquals(5, s.size());
		assertTrue(s.contains(fi.airport("DXB")));
	}

	/**
	 * Test to check if no edges exist to an 
	 * airport (vertex) origin with lower number of
	 * directly connected airports than the departure airport.
	 */
	@Test
	public void setDirectlyConnectedOrderTest() {
		fi.setDirectlyConnected();
		fi.setDirectlyConnectedOrder();
		assertEquals(fi.airport("NCL").getDirectlyConnectedOrder(), 0);
	}

	/**
	 * Test to check the number of airports reachable from the 
	 * given airport that have strictly more direct connections
	 * and check for one of the correct airports in the list
	 */
	@Test
	public void betterConnectedInOrderLHRTest() {
		fi.setDirectlyConnected();
		fi.setDirectlyConnectedOrder();
		Airport lhr = fi.airport("LHR");
		Set<Airport> s = fi.getBetterConnectedInOrder(lhr);
		assertEquals(8, s.size());
		assertTrue(s.contains(fi.airport("ATL")));
	}

	/**
	 * Test to check if the correct meet up airport is
	 * returned for the least cost meet up
	 */
	@Test
	public void leastCostMeetUpTest() {
		try {
			String meetUp = fi.leastCostMeetUp("NCL", "NTL");
			assertEquals("AMS", meetUp);
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	/**
	 * Test to check if the correct meet up airport is
	 * returned for the least hop meet up
	 */
	@Test
	public void myLeastHopMeetUpTest() {
		try {
			String meetUp = fi.leastHopMeetUp("NCL", "NTL");
			assertEquals("DXB", meetUp);
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	/**
	 * Test to method failure if the 
	 * airport code is/are invalid.
	 */
	@Test
	public void failWithWrongAirportTest() {
		try {
			fi.leastCost("NCL", "NML");
			fail();
			fi.leastHop("DXB", "AKA");
			fail();
			fi.leastCostMeetUp("ASA", "DXB");
			fail();
			fi.leastHopMeetUp("NML", "NCL");
			fail();
		} catch (FlyingPlannerException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test to check method failure if the
	 * same airport is passed in twice
	 * for meet up search 
	 */
	@Test
	public void failSameAirportInMeetUpTest() {
		try {
			fi.leastCostMeetUp("NCL", "NCL");
			fail();
			fi.leastHopMeetUp("DXB", "DXB");
			fail();
		} catch (FlyingPlannerException e) {
			assertTrue(true);
		}
	}
}
