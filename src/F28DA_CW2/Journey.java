package F28DA_CW2;

import java.util.LinkedList;
import java.util.List;
import org.jgrapht.GraphPath;

/**
 * @author Riffat Khan (H00328074)
 * 
 * This class is used to store the journey and has methods to retrieve the journey's information.
 */

public class Journey implements IJourneyPartB<Airport, Flight>, IJourneyPartC<Airport, Flight> {
 
	// variables
	private GraphPath<Airport,Flight> graphPath;
	private int totalCost, airTime, connectingTime;
	
	// constructor
	public Journey(GraphPath<Airport,Flight> graphPath) {
		this.graphPath = graphPath;
	
	}
	
	/** 
	 * Get airport codes (stops) of the vertices in the graphpath journey
	 * @return airport codes
	 */
	@Override
	public List<String> getStops() {
		List<String> stops = new LinkedList<String>();
		graphPath.getVertexList().forEach(airport -> stops.add(airport.getCode()));
		return stops;
	}

	/**
	 *  Get flight codes (flights) of the edges in the graphpath journey
	 * @return flight codes
	 */
	@Override
	public List<String> getFlights() {
		List<String> flights = new LinkedList<String>();
		graphPath.getEdgeList().forEach(flight -> flights.add(flight.getFlightCode())); 
		return flights;
	}

	/**
	 * Get the total number of hops taken in the graphpath journey
	 * @return number of hops
	 */
	@Override
	public int totalHop() {		
		return this.graphPath.getEdgeList().size();
	}

	/**
	 * Sum the cost of all the flights in the graphpath journey
	 * @return total cost
	 */
	@Override
	public int totalCost() {
		totalCost = 0;
		this.graphPath.getEdgeList().forEach(flight -> totalCost += flight.getCost());	
		return totalCost;
	}

	/** 
	 * Get duration of flight in the air between departure and arrival airport 
	 * @return the time in air
	 */
	@Override
	public int airTime() {
		airTime = 0;
		graphPath.getEdgeList().forEach(flight -> airTime += getDuration(flight.getFromGMTime(), flight.getToGMTime()));
		return airTime;
	}

	/**
	 * Get time take to connect between two airports
	 * @return time in connecting
	 */
	@Override
	public int connectingTime() {
		connectingTime = 0;
		for(int i = 0; i < graphPath.getEdgeList().size() - 1; i++) {
			connectingTime += getDuration(graphPath.getEdgeList().get(i).getToGMTime(), graphPath.getEdgeList().get(i+1).getFromGMTime());
		}
		return connectingTime;
	}
	
	/**
	 * Get the sum of the duration of flight in the air and time between two airports
	 * @return total time of the journey
	 */
	@Override
	public int totalTime() {
		return airTime() + connectingTime();
	}

	// Helper methods 
	
	/**
	 * Get the edges (flights) in the journey
	 * @return list of edges
	 */
	public List<Flight> getEdges() {
		return this.graphPath.getEdgeList();
	}

	/**
	 * Get the duration between the two times in the format of minutes
	 * Reference: https://www.geeksforgeeks.org/difference-two-given-times/
	 * @param start is the start time
	 * @param end is the end time
	 * @return duration in minutes
	 */
	private int getDuration(String start, String end) {
		int startTime = Integer.parseInt(start);
		int endTime = Integer.parseInt(end);
		int startMinutes = ((startTime / 100) * 60) + (startTime % 100);
		int endMinutes = ((endTime / 100) * 60) + (endTime % 100);
		int duration;
		if(startMinutes > endMinutes)	{
			int subDuration = (startMinutes - endMinutes);
			duration = ((24*60) - subDuration);
		}
		
		else {
			duration = (endMinutes - startMinutes);
		}
		return duration;
	}
}
