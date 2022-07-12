package F28DA_CW2;

/**
 * @author Riffat Khan (H00328074)
 * 
 * This class is used to create a flight object.
 */

public class Flight implements IFlight {
	
	// variables
	private String flightCode, fromGMTime, toGMTime;
	private Airport from, to;
	private int cost;
	
	// constructor
	public Flight(String flightCode, Airport from, String fromGMTime, Airport to, String toGMTime, int cost) {
			this.flightCode = flightCode;
			this.from = from;
			this.fromGMTime = fromGMTime;
			this.to = to;
			this.toGMTime = toGMTime;
			this.cost = cost;
	}

	// getters

	/**
	 * Method to retrieve the flight code
	 * @return the code
	 */
	@Override
	public String getFlightCode() {
		return this.flightCode;
	}

	/**
	 * Method to retrieve the airport where the flight is landing on
	 * @return the airport
	 */
	@Override
	public Airport getTo() {
		return this.to;
	}

	/**
	 * Method to retrieve the airport where the flight is departing from
	 * @return the airport
	 */
	@Override
	public Airport getFrom() {
		return this.from;
	}

	/**
	 * Method to retrieve the time the flight is departing from
	 * @return the time
	 */
	@Override
	public String getFromGMTime() {
		return this.fromGMTime;
	}

	/**
	 * Method to retrieve the time the flight is landing on
	 * @return the time
	 */
	@Override
	public String getToGMTime() {
		return this.toGMTime;
	}

	/**
	 * Method to retrieve the cost of the flight
	 * @return the cost
	 */
	@Override
	public int getCost() {
		return this.cost;
	}
}
