package F28DA_CW2;

import java.util.Scanner;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

/***********************************************************************************************************************
 * @author Riffat Khan (H00328074)
 * 
 * This class represents the direct flights between airports and their associated costs in a simple directed graph.
 * The graph consists of 5 airports as vertices, their direct flights as edges and cost as weights of the edges.
 * The user is asked to enter their departure airport. The program changes the input into a specific format and is
 * used to check if it exists in the graph. Program asks to re-enter departure airport if invalid, else proceeds
 * to asking for the destination airport and the whole input validation process is repeated.
 * The cheapest path is found using the Dijkstra's Shortest Path algorithm and presented to the user on the terminal. 
 * 
 ***********************************************************************************************************************/

public class FlyingPlannerMainPartA {
	
    // variables
	static SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
    static String startingPoint, destinationPoint;
    static Scanner airportScan = new Scanner(System.in);
    
    /**
     * Helper mathod to add a vertex in the graph.
     * @param vertexName is the name of the vertex
     * @return the string value of the vertex
     */
	public static String setVertex(String vertexName) {
		graph.addVertex(vertexName);
		return vertexName;
	}
	
    /**
     * Helper method to add 2 edges between a pair of 
     * vertices making it bidirectional and set its weight 
     * @param vertexName1 first vertex
     * @param vertexName2 second vertex
     * @param weight to set the weights of the two edges
     */
	public static void setEdgeAndWeight(String vertexName1, String vertexName2, int weight) {
		graph.setEdgeWeight(graph.addEdge(vertexName1, vertexName2), weight);
        graph.setEdgeWeight(graph.addEdge(vertexName2, vertexName1), weight);
	}

    /**
     * Helper method to alter the user input's in a
     * valid format and check if it exists in the graph
     * @param airport user's input
     * @return the correctly formatted version of the input or null
     */
    public static String validInput(String airport) {
        String nameCheck = "";
        // split the user input into an array using a regex to capture one or more in-between spaces
        // Reference: https://stackoverflow.com/questions/7899525/how-to-split-a-string-by-space
        String[] words = airport.strip().toLowerCase().split("\\s+");

        for (String word : words)
            // format the words and combine them in a variable 
            // Reference: https://stackoverflow.com/questions/3904579/how-to-capitalize-the-first-letter-of-a-string-in-java
            nameCheck += word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " ";

        // condition to check if airport does not exist in the graph
        if (!graph.containsVertex(nameCheck.strip())) return null;

        return nameCheck.strip();
    }

	public static void main(String[] args) {

        // adding vertices in the graph and 
        // assign them to the variables
        String v1 = setVertex("Edinburgh");
        String v2 = setVertex("Heathrow");
        String v3 = setVertex("Dubai");
        String v4 = setVertex("Kuala Lumpur");
        String v5 = setVertex("Sydney");

        // adding edges between vertices bidrectionally
        // and setting their weights 
        setEdgeAndWeight(v1, v2, 80);
        setEdgeAndWeight(v2, v3, 130);
        setEdgeAndWeight(v2, v5, 570);
        setEdgeAndWeight(v3, v4, 170);
        setEdgeAndWeight(v3, v1, 190);
        setEdgeAndWeight(v4, v5, 150);
        
        System.out.println("\nThe following airports are used: ");
        
        // iterate and print the airport names (vertices) from the graph to the terminal
        graph.vertexSet().forEach(airport -> System.out.printf("-> %s\n", airport));

        do {
	        System.out.print("\nEnter departure city: ");
            // assign the correctly formated departure input to a variable
	        startingPoint = validInput(airportScan.nextLine());
            // condition to check if null was returned and informing invalid input to the user
	        if (startingPoint == null) System.out.println("Departure input not valid! Please try again.");
        }while(startingPoint == null); // loop again if invalid input
        
        do {
	        System.out.print("Enter destination city: ");
            // assign the correctly formated destination input to a variable
	        destinationPoint = validInput(airportScan.nextLine());
            // condition to check if null was returned and informing invalid input to the user
	        if (destinationPoint == null) System.out.println("Destination input not valid! Please try again.\n");
        }while(destinationPoint == null); // loop again if invalid input
        
        // close the scanner
        airportScan.close();
        
    	System.out.println("\nShortest and cheapest path: ");
        // get the shortest path (cheapest) between two airports in the graph
    	GraphPath<String, DefaultWeightedEdge> cheapestPath = DijkstraShortestPath.findPathBetween(graph, startingPoint,destinationPoint);
    	// split paths into an array
        String[] path = cheapestPath.toString().split(",");

        for (int i = 0; i < path.length; i++) {
            // use regex to remove brackets and replace collon with an arrow
            // Reference: https://stackoverflow.com/questions/14442162/java-replace-all-square-brackets-in-a-string
            String edge = path[i].replaceAll("[\\[\\]()]", "").replace(":", "->");
            // path number
            String edgeNumber = i < 1 ? (i+1) + ". " + edge : (i+1) + "." + edge;
            System.out.println(edgeNumber);       
        }
        // output the cost of the cheapest path
        System.out.println("Cost of shortest and cheapest path =  £" + cheapestPath.getWeight() + "\n");      
	}
}