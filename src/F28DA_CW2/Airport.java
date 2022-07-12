package F28DA_CW2;

import java.util.Set;

/**
 * @author Riffat Khan (H00328074)
 * 
 * This class is used to create an airport object.
 */

public class Airport implements IAirportPartB, IAirportPartC {
	
	// variables
	private String code, location, airportName;
	private Set<Airport> directlyConnected;
	private int order;
		
	// constructor
	public Airport(String code, String location, String airportName) {
		
		this.code = code;
		this.location = location;
		this.airportName = airportName;
	}
	
	// getters

	/**
	 * Method to retrieve the airport code
	 * @return the code
	 */
	@Override
	public String getCode() {
		return this.code;
	}

	/**
	 * Method to retrieve the airport name
	 * @return the airport name
	 */
	@Override
	public String getName() {
		return this.airportName;
	}

	/**
	 * Method to retrieve the order
	 * @return the order
	 */
	@Override
	public int getDirectlyConnectedOrder() {
		return this.order;
	}

	/**
	 * Method to get the set of directly connected airports
	 * @return the set of directly connected airports
	 */
	@Override
	public Set<Airport> getDicrectlyConnected() {
		return this.directlyConnected;
	}

	// setters

	/** Method to assign the directly connected airports set
	 * @param directlyConnected the set of directly connected airports
	 */
	@Override
	public void setDicrectlyConnected(Set<Airport> directlyConnected) {
		this.directlyConnected = directlyConnected;

	}

	/**
	 * Method to set the order
	 * @param order 
	 */
	@Override
	public void setDicrectlyConnectedOrder(int order) {
		this.order = order;

	}
	
	// Helper method

	/**
	 * Method to get the airport location with the airport code in brackets 
	 * @return the formatted airport location
	 */
	@Override
	public String toString() {
		return this.location + " (" + this.code + ")";
	}

}
