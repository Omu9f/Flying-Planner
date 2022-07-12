package F28DA_CW2;

import java.util.Set;

/**
 * @author Moses Varghese
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
public class Airport implements IAirportPartB, IAirportPartC {

	private String code, name, location;
	private Set<Airport> directlyConnected;
	private int order;
	
	// constructor for an airport object
	public Airport(String c, String l, String n) { code = c; location = l; name = n; }
	
	// getter for airport code
	@Override // [part B] [done]
	public String getCode() { return code; }

	// getter for airport name
	@Override // [part B] [done]
	public String getName() { return name; }

	// setter for the set of airports directly connected to the airports
	@Override // [part C] [done] (ADVANCED)
	public void setDirectlyConnected(Set<Airport> directlyConnected) { this.directlyConnected = directlyConnected; }

	// getter for the set of airports directly connected to the airports
	@Override // [part C] [done] (ADVANCED)
	public Set<Airport> getDirectlyConnected() {return directlyConnected;}

	// setter for the size of the set of airports directly connected to the airports
	@Override // [part C] [done] (ADVANCED)
	public void setDirectlyConnectedOrder(int order) {this.order = order;}

	// getter for the size of the set of airports directly connected to the airports
	@Override // [part C] [done] (ADVANCED)
	public int getDirectlyConnectedOrder() {return order;}

	@Override // helper method for part B interface
	public String toString() { return location + " (" + code + ")"; }
	
}
