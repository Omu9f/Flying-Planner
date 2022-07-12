package F28DA_CW2;

/**
 * @author Moses Varghese
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
public class Flight implements IFlight {
	
	private String flightCode, fromGMTime, toGMTime;
	private Airport to, from;
	private int cost;
	
	// constructor for a flight object
	public Flight(String code, Airport strtA, String strtT, Airport destA, String destT, int c) {
		flightCode = code;
		from = strtA;
		fromGMTime = strtT;
		to = destA;
		toGMTime = destT;
		cost = c;
	}

	@Override // getter for the flight's code
	public String getFlightCode() { return flightCode; }

	@Override // getter for the flight's desintation airport
	public Airport getTo() { return to; }

	@Override // getter for the flight's start airport
	public Airport getFrom() { return from; }

	@Override // getter for the flight's start time
	public String getFromGMTime() { return fromGMTime; }

	@Override // getter for the flight's arrival time
	public String getToGMTime() { return toGMTime; }

	@Override // getter for the flight's cost
	public int getCost() { return cost; }

}
