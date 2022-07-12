# Flying Planner

## System Design

This implementation of Flying Planner uses the library JGraphT for the operations performed to make the program successful. JGrapht is a free Java class library providing graph data structures and algorithms. The airport and flight network are represented in a graph using the SimpleDirectedWeightedGraph and shortest paths are found using the DijkstraShortestPath algorithm provided by the GraphPath. Furthermore, DirectedAcyclicGraph class was used to contain only flights flying to an airport destination with strictly higher number of directly connected airports than the departure airport. A GraphPath was used for the trip path in the Journey class.

***Additional note*** : The IDE used is VS Code since my GitHub is connected to it which is used for version control.

### Part A Implementation
FlyingPlannerMainPartA Class: This class represents the direct flights between airports and their associated costs in a simple directed graph. The graph consists of 5 airports as vertices, their direct flights as edges and cost as weights of the edges. The user is asked to enter their departure airport. The program changes the input into a specific format and is used to check if it exists in the graph. Program asks to re-enter departure airport if invalid, else proceeds to asking for the destination airport and the whole input validation process is repeated. The cheapest path is found using the Dijkstra's Shortest Path algorithm and presented to the user on the terminal. Additional helper methods such as setVertex(), setEdgeAndWeight() and validInput() reduce code redundancy and are used in the main method.

```java
The following airports are used:
-> Edinburgh
-> Heathrow
-> Dubai
-> Kuala Lumpur
-> Sydney

Enter departure city: Dubai
Enter destination city: Edinburgh

Shortest and cheapest path:
1. Dubai -> Edinburgh
Cost of shortest and cheapest path =  £190.0
```

### Part B Implementation
***Airport Class*** : This class is used to create an airport object and implements methods from the given interfaces IAirportPartB and IAirportPartC. It contains the name of the airport, the airport code, and the location of the airport. The methods in the interface for Part B set or retrieve these details. A helper method is implemented to get the location of the airport and airport code in a certain format.

***Flight Class*** : This class is used to create a flight object and implements methods from the given interface IFlight. It contains the flight code, the departure time, the destination time, the cost, the departure airport, and the destination airport. The methods are used to set or retrieve these details.

***Journey Class for Part B***: This class is used to store the journey (trip) computed in the FlyingPlanner class in the form of a graph path. The methods are implemented from the given interfaces IJourneyPartB and IJourneyPartC to retrieve the journey's information. For Part B, the airport stops, flights, total cost, and time spent in the air have been implemented. Helper methods such as getDuration() which calculates the duration of its parameters in minutes and getEdges() were added to improve functionality.

***FlyingPlanner Class for Part B*** : This class represents the flight planner. It has a graph of all the airports as vertices and the edges between them as the flight paths. The class has a populate() to add airports and flightsfrom the given data in the dataset folder originating from the Open Flights open-source project. Hash Tables are used to prevent null keys and values (GeeksforGeeks, 2015). Part B requires the implementation of the least-cost method to find the cheapest journey between two airports using Dijkstra’s algorithm by setting the weights of the edges as the total cost. Another feature of this method is the choice of excluding certain airports before finding the most suitable journey. The implementation of this method is performed in the additional method getTrip() which takes the parameters of the leastCost() method and a character ‘C’ to indicate the functionality for the least cost journey. The getTrip() method is used to minimize code redundancy or assist in similar operations. It returns the computed journey.

***FlyingPlannerMainPartBC Class for Part B*** : This is the main class of the program. It contains the main method to run the interfaces for part B and C. The additional methods are used to assist in the running of the program. The user is prompted with a choice of which part of the program to run. According to the choice, the program will run the corresponding interface. Part B will run the interface for the user to first input the departure and destination airports until a valid code is entered respectively. Then input the choice of mode (i.e., least cost or least hop) to get the journey for their flight plan. The printModes() method displays the modes menu and the checker() method asks the user their input which is then checked with the available modes. The checker looks for the character of available modes present in the user’s input choice, if not present the program asks the user to re-enter. The program will output a visual representation of the found journey in each leg and its details at the end such as total cost, airtime, total time with connecting, and the number of hops. 

```java
Welcome to the Flying Planner!There are two interfaces:
-> Part B
-> Part C

Your choice of convenience: Part B

Enter departure airport code: NCL
Enter destination airport code: NTL

There are two modes:
-> Least Cost
-> Least Hops

Your choice of convenience: Least Cost

Journey for Newcastle Airport (NCL) to Newcastle Airport (NTL)

Leg    Leave               At         On           Arrive               At
------------------------------------------------------------------------------
1   Newcastle (NCL)       19:18     KL7893      Amsterdam (AMS)       20:04
2   Amsterdam (AMS)       07:47     CX0831      Hong Kong (HKG)       17:02
3   Hong Kong (HKG)       07:48     CX7100      Brisbane (BNE)        14:27
4   Brisbane (BNE)        16:28     QF0640      Newcastle (NTL)       17:29
------------------------------------------------------------------------------

Total Journey Cost =  £1035
Total Time in the Air = 1061 minutes
Total Time including connecting = 2771 minutes
Total Number of Hops = 4
```


### Part C Implementation
***Journey duration*** : The implementation of this feature is the completion of the Journey class. The total airtime is calculated in the method airTime() by finding the duration between each flight’s from (departure) and to (destination) times and summing them all together. The connecting time is calculated by finding the duration between each flight’s to time (destination) and the next flight’s from (departure) time and summing them all together. The total trip time is the sum of time spent on the flight (air) and time taken connecting between each flight. This feature is used in the partB() method of the FlyingPlannerMainPartBC class as it is best suited for the part B interface. The helper method getDuration() performs arithmetic on 24-hour clock times and returns in minutes.

***Least hops*** : The implementation of this feature is in the FlyingPlanner class. The path with the fewest number of changeovers is the result of the leastHop() method. This feature’s program is implemented in the shared method getTrip(). This takes the parameters of the leastHop() method and a character ‘H’ to indicate the functionality for the least hops journey. The program offers the possibility to exclude one or more airports from the journey search just like least cost. However, the weights of the edges in the graph are set to 1 for Dijkstra’s algorithm to find the shortest path. This feature is used in the partB() method of the FlyingPlannerMainPartBC class as one of the modes for the user to pick. It is best suited to part B’s interface for better interactivity. The method totalHops() in the Journey class retrieves the total number of hops (number of edgesin the graph path). This is presented to the user on the terminal in FlyingPlannerMainPartBC class. 

***Directly connected order*** : The implementation of this feature is in the Airport and FlyingPlanner class. The Airport class has the set of airports directly connected to the airport. The methods in the interface for Part C set or retrieve the relevent details. The method directlyConnected() is used to get airports that are directly connected if there are exists two flights connecting them in a single hop in both directions. This method is used by setDirectlyConnected() to add the airports in the set and return sum of all airports directly connected. Furthermore, the program finds the set of airports reachable from a given airport that have strictly more direct connections using a directed acyclic graph.

***Meet-Up search*** : The search for a meetup airport depending on the mode (i.e., least hop or least cost) for two people located at two different airports is implemented in the getMeetUp() method. This method is shared by both modes to reduce code redundancy. The least cost meetup uses the least cost method, and the least hop meetup uses the least hop method. The cheapest or shortest path is found first depending on the mode. The two starting airports are removed from the computed path to avoid the meetup airport resulting in one of the starting airports. After iterating through each stop in the journey path, the most convenient meetup airport code is returned.
