package F28DA_CW2;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/*****************************************************************************************************************************************
 * @author Riffat Khan (H00328074) 
 * 
 * This is the main class of the program. It contains the main method to run the interfaces for part B and C. The additional methods
 * are used to assist in the running of the program. The user is prompted with a choice of which part of the program to run. According
 * to the choice, the program will run the corresponding interface. Part B will run the interface for the user to first input the
 * departure and destination airports, and then the choice of mode (i.e least cost or least hop) to get the journey for their flight
 * plan. The program will output a visual representation of the found journey and it's details such as total cost, air time, total 
 * time with connecting, and number of hops. 
 * Part C will run the interface for the user to first input the first airport and then the choice of mode for their flight plan. 
 * If the user chooses meet-up search mode then the program prompts for a second airport following with the type of journey 
 * (i.e least cost or least hop).The user is presented with a meet-up airport or directly connected airports with their names to the 
 * first airport if directly connected mode is chosen.
 * 
 *****************************************************************************************************************************************/

public class FlyingPlannerMainPartBC {

	// variables
	private static Scanner scan = new Scanner(System.in);
	private static Journey journey = null;
	private static String departureCode = null, destinationCode = null, userMode = null, meetup = null;
	static char part;
	static Set<Airport> dc = null;

	/**
	 * Helper method to check and proceed only if the user input is a valid option else ask for input again.
	 * @param mode1 
	 * @param mode2
	 * @return the entered option
	 */
	private static char checker(char mode1, char mode2)  {
		while (true) {
			System.out.print("\nYour choice of convenience: ");
			userMode = scan.nextLine().strip().toLowerCase();
			// check if mode 1 is not entered
			if (userMode.indexOf(mode1) == -1) {
				// check if mode 2 is not entered
				if (userMode.indexOf(mode2) == -1) {
					// repeat the process if mode 1 and mode 2 are not entered
					System.out.println("Invalid mode! Please try again.");
					continue;
				}
				// return mode 2 if entered
				else  System.out.println(); return mode2;
			}
			// return mode 1 if entered
			else System.out.println(); return mode1; 	
		}
	}

	/**
	 * Helper method to print the options available for the user to choose.
	 */
	private static void printModes() {
		System.out.print("\nThere are two modes: \n");
		System.out.print("-> Least Cost \n");
		System.out.print("-> Least Hops \n");
	}

	/**
	 * Helper method to perform the operations of part B.
	 * @param fi
	 * @throws FlyingPlannerException
	 */
	private static void partB(FlyingPlanner fi) throws FlyingPlannerException {
		do {
			// ask for departure airport code
			System.out.print("Enter departure airport code: ");
			departureCode = scan.nextLine().strip().toUpperCase();
			// check if the airport code is valid and if it is not, ask for input again
			if (!fi.containsGraphVertex(departureCode)) System.out.println("Departure input not valid! Please try again.\n");
		} while (!fi.containsGraphVertex(departureCode)); // repeat the process if the airport code is not valid
		
		do {
			// ask for destination airport code
			System.out.print("Enter destination airport code: ");
			destinationCode = scan.nextLine().strip().toUpperCase();
			// check if the airport code is valid and if it is not, ask for input again
			if (!fi.containsGraphVertex(destinationCode)) System.out.println("Destination input not valid! Please try again.\n");
		} while (!fi.containsGraphVertex(destinationCode)); // repeat the process if the airport code is not valid   

		// display available modes
		printModes();
		// check if the user input is any one of the two options
		part = checker('c', 'h');
		// get the journey according to the user's choice of convenience
		if (part == 'c') journey = fi.leastCost(departureCode, destinationCode);
		if (part == 'h') journey = fi.leastHop(departureCode, destinationCode);
	
		// the airports
		Airport originAirport = fi.airport(departureCode);
		Airport departureAirport = fi.airport(destinationCode);
	
		System.out.printf("Journey for %s (%s) to %s (%s)\n\n", originAirport.getName(), originAirport.getCode(), departureAirport.getName(), departureAirport.getCode());	
		// the titles of the table
		System.out.println("Leg    Leave		   At	      On	   Arrive		At");
		System.out.println("------------------------------------------------------------------------------");

		// the flights in the journey
		List<Flight> flightsList  = journey.getEdges();
		for(int i = 0; i < flightsList.size(); i++) {
			// print the journey
			// Reference: https://www.javatpoint.com/java-string-format
			String leg = String.format("%-4s", i+1);
			String leave = String.format("%-22s", flightsList.get(i).getFrom());
			String atLeave = String.format("%-10s", flightsList.get(i).getFromGMTime().substring(0, 2) + ":" + flightsList.get(i).getFromGMTime().substring(2));
			String on = String.format("%-12s", flightsList.get(i).getFlightCode());
			String arrive = String.format("%-22s", flightsList.get(i).getTo());
			String atArrive = String.format("%-6s", flightsList.get(i).getToGMTime().substring(0, 2) + ":" + flightsList.get(i).getToGMTime().substring(2));
			System.out.println(leg + leave + atLeave + on + arrive  + atArrive);
		}
		System.out.println("------------------------------------------------------------------------------");
		// print the total cost of the journey
		System.out.printf("\nTotal Journey Cost =  £%d", journey.totalCost());
		// print the time spent in the air
		System.out.printf("\nTotal Time in the Air = %d minutes", journey.airTime());
		// print the total time of the journey
		System.out.printf("\nTotal Time including connecting = %d minutes", journey.totalTime());
		// print the total number of hops
		System.out.printf("\nTotal Number of Hops = %d\n\n", journey.totalHop());
	}

	/**
	 * Helper method to perform the operations of part C.
	 * @param fi
	 * @throws FlyingPlannerException
	 */
	private static void partC(FlyingPlanner fi) throws FlyingPlannerException {
		// ask for the mode of convenience
		System.out.println("There are two modes: ");
		System.out.println("-> Directly Connected Airports");
		System.out.println("-> Meet Up Airport ");
		// check if the user input is any one of the two options
		part = checker('d', 'm');

		do {
			// ask for the airport code
			System.out.print("Enter airport code: ");
			departureCode = scan.nextLine().strip().toUpperCase();
			// check if the airport code is valid and if it is not, ask for input again
			if (!fi.containsGraphVertex(departureCode)) System.out.println("Airport code not valid! Please try again.\n");
		} while (!fi.containsGraphVertex(departureCode)); // repeat the process if the airport code is not valid

		// check if directly connected option is selected
		if (part == 'd') {
			dc = fi.directlyConnected(fi.airport(departureCode));
			System.out.printf("\nAirports with two-way flights to %s (%s): \n", fi.airport(departureCode).getName(), departureCode);
			dc.forEach(airport -> System.out.printf("-> %s (%s)\n", airport.getName(), airport.getCode()));
			System.out.println();
		}

		// check if meet up option is selected
		if (part == 'm') {
			do {
				// ask for the second airport code
				System.out.print("Enter second airport code: ");
				destinationCode = scan.nextLine().strip().toUpperCase();
				// check if the airport code is valid and if it is not, ask for input again
				if (!fi.containsGraphVertex(destinationCode)) System.out.println("Airport code not valid! Please try again.\n");
			} while (!fi.containsGraphVertex(destinationCode)); // repeat the process if the airport code is not valid

			// display available modes
			printModes();
			// check if the user input is any one of the two options
			part = checker('c', 'h');
			// get the meetup airport according to the user's choice of convenience
			if (part == 'c') meetup = fi.leastCostMeetUp(departureCode, destinationCode);
			if (part == 'h') meetup = fi.leastHopMeetUp(departureCode, destinationCode);
			// if the meetup airport is not found, print the message
			if (meetup == null) System.out.printf("No meetup airport found!");
			// else print the meetup airport
			else System.out.printf("The best meetup airport would be %s (%s).\n\n", meetup, fi.airport(meetup).getName());
		}
	}

	public static void main(String[] args) {
		FlyingPlanner fi;
		fi = new FlyingPlanner();
		try {
			fi.populate(new FlightsReader());
			System.out.println("Welcome to the Flying Planner!");
			// print the interface menu
			System.out.println("There are two interfaces: ");
			System.out.println("-> Part B");
			System.out.println("-> Part C");

			// check if the user input is any one of the two options
			part = checker('b', 'c'); 
			// call the method according to the user's choice of interface
			if (part == 'b') partB(fi); else partC(fi);
			// close the scanner
			scan.close();		
		} catch (FileNotFoundException | FlyingPlannerException e) {}
	}
}
