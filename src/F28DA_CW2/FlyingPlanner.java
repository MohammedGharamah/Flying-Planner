package F28DA_CW2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

/****************************************************************************************************************************************************
 * @author Riffat Khan (H00328074)
 * 
 * This class reperesents the flight planner. It has a graph of all the airports as vertices and the edges between them as the flight paths.
 * The class has methods to add airports and flights (given in the dataset), find the cheapest (least cost) or shortest journey (least hop) 
 * between two airports, the direct flight between two airports, and the cheapest or shortest meeting point between two airports. The journeys 
 * are found using Dijkstra's algorithm. Furthermore, the class has additional methods to minimize code redundancy or assist in other operations.
 * 
 ****************************************************************************************************************************************************/

/**
 * JGraphT references:
 * https://jgrapht.org/javadoc/org.jgrapht.core/org/jgrapht/Graphs.html
 * https://jgrapht.org/javadoc/org.jgrapht.core/org/jgrapht/graph/SimpleDirectedWeightedGraph.html
 * https://jgrapht.org/javadoc-1.4.0/org/jgrapht/graph/DirectedAcyclicGraph.html
 * https://jgrapht.org/javadoc/org.jgrapht.core/org/jgrapht/alg/shortestpath/DijkstraShortestPath.html
 */
public class FlyingPlanner implements IFlyingPlannerPartB<Airport,Flight>, IFlyingPlannerPartC<Airport,Flight> {

	// variables
    private SimpleDirectedWeightedGraph<Airport, Flight> graph;
	private DirectedAcyclicGraph<Airport, Flight> dag;
    private Hashtable<String, Airport> airportTable;
    private Hashtable<String, Flight> flightTable;
	private String meetUp = null;
	private double minimum = Double.MAX_VALUE;
	int sum;
	
	// constructor
	public FlyingPlanner() {
		graph = new SimpleDirectedWeightedGraph<Airport, Flight>(Flight.class);
		airportTable = new Hashtable<String, Airport>();
		flightTable = new Hashtable<String, Flight>();
	}

	/**
	 * Calls the populate method with the airports and flights (from the dataset) in the parameters. 
	 * @param fr flight reader
	 * @return true if the operation was successful.
	 */
	@Override
	public boolean populate(FlightsReader fr) {
		return populate(fr.getAirports(), fr.getFlights());
	}

	
	/**
	 * Populates the graph with the airports and flights information from a set of airports and a set of flights. 
	 * @param airports set of airports
	 * @param flights set of flights
	 * @return true if the operation was successful.
	 */
	@Override
	public boolean populate(HashSet<String[]> airports, HashSet<String[]> flights) {
		// Add airports to the graph
		airports.forEach(airport -> {
			// Create airport object
			Airport temporaryAirport = new Airport(airport[0], airport[1], airport[2]);
			// Add airport to the graph
			graph.addVertex(temporaryAirport);
			// Add airport to the airport table
			airportTable.put(temporaryAirport.getCode(), temporaryAirport);
		});
		
		// Add flights to the graph
		flights.forEach(flight -> {
			Airport departureAirport = airportTable.get(flight[1]);
			Airport destinationAirport = airportTable.get(flight[3]);
			// Create flight object
			Flight newFlight = new Flight(flight[0], departureAirport, flight[2], destinationAirport, flight[4], Integer.parseInt(flight[5]));
			// Add flight to the graph
			graph.addEdge(departureAirport, destinationAirport, newFlight);
			// Add flight to the flight table
			flightTable.put(flight[0], newFlight);
		});		
		return true;
	}

	/**
	 * Returns the airport object of the given airport code.
	 * @param airportCode code of the airport
	 * @return the airport
	 */
	@Override
	public Airport airport(String code) {
		return airportTable.get(code);
	}

	/**
	 * Returns the flight object of the given flight code.
	 * @param flightCode code of the flight
	 * @return the flight
	 */
	@Override
	public Flight flight(String code) {
		return flightTable.get(code);
	}

	/**
	 * Calls the leastCost method with the departure airport and destination airport in the parameters.
	 * @param from departure airport
	 * @param to destination airport
	 * @return the journey of the cheapest path between the two airports
	 */
	@Override
	public Journey leastCost(String from, String to) throws FlyingPlannerException {
		return leastCost(from, to, null);
	}

	/**
	 * Calls the leastHop method with the departure airport and destination airport in the parameters.
	 * @param from departure airport
	 * @param to destination airport
	 * @return the journey of the shortest path between the two airports
	 */
	@Override
	public Journey leastHop(String from, String to) throws FlyingPlannerException {
		return leastHop(from, to, null);
	}

	/**
	 * Calls the getJourney method with the parameters required for the least cost journey.
	 * @param from departure airport
	 * @param to destination airport
	 * @param exclude list of airports to exclude
	 * @return the journey of the cheapest path between the two airports
	 */
	@Override
	public Journey leastCost(String from, String to, List<String> excluding) throws FlyingPlannerException {
		return getTrip('C', from, to, excluding);
	}
	
	/**
	 * Calls the getJourney method with the parameters required for the least hop journey.
	 * @param from departure airport
	 * @param to destination airport
	 * @param exclude list of airports to exclude
	 * @return the journey of the shortest path between the two airports
	 */
	@Override
	public Journey leastHop(String from, String to, List<String> excluding) throws FlyingPlannerException {
		return getTrip('H', from, to, excluding);
	}

	/**
	 * Get airports that are directly connected if there exists two flights connecting them in a 
	 * single hop in both directions.
	 * @param airport airport to check
	 * @return the list of airports that are directly connected to the given airport
	 */
	@Override
	public Set<Airport> directlyConnected(Airport airport) {
		// create a set of airports
		Set<Airport> connectedAirports = new HashSet<Airport>();
		graph.vertexSet().forEach(vertex -> {
			// check if the vertex has edges both ways between it and the given airport
			if (graph.containsEdge(vertex, airport) && graph.containsEdge(airport, vertex)) {
				// add the vertex to the set
				connectedAirports.add(vertex);
			}
		});
		return connectedAirports;
	}

	/**
	 * Records for each airport object, the airport's directly connected set and its size. 
	 * @return the sum of all airports directly connected set sizes.
	 */
	@Override
	public int setDirectlyConnected() {
			graph.vertexSet().forEach(airport -> {
			airport.setDicrectlyConnected(directlyConnected(airport));
			// get the size of the airport's directly connected set and add it to the accumulator
			sum += directlyConnected(airport).size();
		});
		return sum;
	}

	/**
	 * Creates a new instance of dag containing airports but only the flights flying to an airport destination 
	 * with strictly higher number of directly connected airports than the origin airport. The 
	 * setDirectlyConnected method is called before this method is called.
	 * @returns the number of flights of the dag. 
	 */
	@Override
	public int setDirectlyConnectedOrder() {
		dag = new DirectedAcyclicGraph<>(Flight.class); 
		graph.edgeSet().forEach(edge -> {
			// checks if the origin airport has more directly connected airports than the destination airport
			if (graph.getEdgeSource(edge).getDicrectlyConnected().size() < graph.getEdgeTarget(edge).getDicrectlyConnected().size()) {
				// add it to the dag graph
				dag.addVertex(graph.getEdgeSource(edge));
				dag.addVertex(graph.getEdgeTarget(edge));
				dag.addEdge(graph.getEdgeSource(edge), graph.getEdgeTarget(edge), edge);
			}
		});
		
		return dag.edgeSet().size();
	}

	/**
	 * Gets the set of airports reachable from the given airport that have strictly more direct connections. 
	 * The setDirectlyConnectedOrder method must be called before this method.
	 * @param airport airport to check
	 * @return the set of airports.
	 */
	@Override
	public Set<Airport> getBetterConnectedInOrder(Airport airport) {
		// create a set of airports
		HashSet<Airport> betterConnected = new HashSet<Airport>();
		dag.vertexSet().forEach(destination -> {
			if (DijkstraShortestPath.findPathBetween(dag, airport, destination) != null) {
				// add the destination airport to the set from the dag
				betterConnected.add(destination);
			}
		}); 
		betterConnected.remove(airport);
		return betterConnected;
	}

	/**
	 * Returns the airport between the two airports that is cheapest to meet in.
	 * @param at1 first airport
	 * @param at2 second airport
	 * @return the meetup airport
	 */
	@Override
	public String leastCostMeetUp(String at1, String at2) throws FlyingPlannerException {
		return getMeetUp('C', at1, at2);
	}

	/**
	 * Returns the airport between the two airports that is the shortest to meet in.
	 * @param at1 first airport
	 * @param at2 second airport
	 * @return the meetup airport
	 */
	@Override
	public String leastHopMeetUp(String at1, String at2) throws FlyingPlannerException {
		return getMeetUp('H', at1, at2);
	}

	// not implemented
	@Override
	public String leastTimeMeetUp(String at1, String at2, String startTime) throws FlyingPlannerException {
		return null;
	}

	// Helper methods

	/**
	 * Checks if the given airport is in the graph.
	 * @param code code of the airport
	 * @return true if the airport is in the graph, false otherwise
	 */
	public boolean containsGraphVertex(String code) {
		return graph.containsVertex(airport(code));
	}

	/**
	 * Additional method to find the least cost or hop journey between two airports based on the mode provided.
	 * @param mode the mode of the journey
	 * @param from departure airport
	 * @param to destination airport
	 * @param excluding list of airports to exclude
	 * @return the journey between the two airports
	 * @throws FlyingPlannerException
	 */
	private Journey getTrip(char mode, String from, String to, List<String> excluding) throws FlyingPlannerException {
		// Check if the airports are valid
		if (!airportTable.containsKey(from) || !airportTable.containsKey(to)) {
			throw new FlyingPlannerException("Invalid airport code");
		}

		// Check if the airports are not the same
		if (from.equals(to)) {
			throw new FlyingPlannerException("Airport code should not be the same");
		}

		// Chceck if there are excluding airports provided
		if (excluding != null && excluding.size() > 0) {
			// Initialize a new graph to remove the excluded airports
			SimpleDirectedWeightedGraph<Airport, Flight> temporaryGraph = new SimpleDirectedWeightedGraph<Airport,Flight>(Flight.class);
			// Copy the graph to the temporary graph
			Graphs.addAllVertices(temporaryGraph, graph.vertexSet());
			Graphs.addAllEdges(temporaryGraph, graph, graph.edgeSet());
			
			// Remove the vertex and all its edges that are in the excluding list
			excluding.forEach(code -> {
				// Check if the airport is valid
				if (temporaryGraph.containsVertex(airport(code))) {
					// Remove the airport and all its edges
					temporaryGraph.removeAllEdges(temporaryGraph.edgesOf(airport(code)));
					temporaryGraph.removeVertex(airport(code));
				}
			});
			// Set the graph to the temporary graph with the excluded airports removed
			graph = temporaryGraph;
		}
		
		// Check if the graph is empty
		if (graph.vertexSet().isEmpty()) {
			throw new FlyingPlannerException("Graph is empty");
		}

		// Set the weights of the edges in the graph depending on the mode
		graph.edgeSet().forEach(edge -> {
			Flight flight = (Flight) edge;
			if (mode == 'C') {
				// Set the weight of the edge to the cost of the flight if mode is 'C'
				graph.setEdgeWeight(edge, flight.getCost());
			} else if (mode == 'H') {
				// Set the weight of the edge to 1 if mode is 'H'
				graph.setEdgeWeight(edge, 1);
			}
		});
		
		// Find the shortest path between the two airports
		GraphPath<Airport, Flight> shortestPath = DijkstraShortestPath.findPathBetween(graph, airport(from), airport(to));
		// Check if the shortest path is found
		if (shortestPath == null) {
			throw new FlyingPlannerException("Journey not found!");
		}
		return new Journey(shortestPath);
	}

	/**
	 * Additional method to find the meetup airport between two airports based on the mode provided.
	 * @param mode the mode of the journey
	 * @param at1 first airport
	 * @param at2 second airport
	 * @return the meetup airport
	 * @throws FlyingPlannerException
	 */
	private String getMeetUp(char mode, String at1, String at2) throws FlyingPlannerException {
		List<String> airports = new ArrayList<String>();
		// get the set of airports that in between at1 and at2
		airports.addAll(getTrip(mode, at1, at2, null).getStops());
		// remove the parameter airports from the list
		airports.remove(at1);
		airports.remove(at2);
		// if there are no in-between airports in the list, return null
		if (airports.size() == 0) return meetUp;

		airports.forEach(airport -> {
			double tempValue = 0;
			try {
				if (mode == 'C')
					// get the cost of the journey between the airports and the meetup airport 
					tempValue += leastCost(at1, airport).totalCost() + leastCost(airport, at2).totalCost();
				else if (mode == 'H')
					// get the hop of the journey between the airports and the meetup airport
					tempValue += leastHop(at1, airport).totalHop() + leastHop(airport, at2).totalHop();
				
				// if the cost is less than the current cost, set the meet-up to the airport
				if (tempValue < minimum) {
					minimum = tempValue;
					meetUp = airport;
				}
			} catch (FlyingPlannerException e) {}
		});
		if (meetUp == null) throw new FlyingPlannerException("Meet-up not found!");	
		// return the meetup airport
		return meetUp;	
	}
}
