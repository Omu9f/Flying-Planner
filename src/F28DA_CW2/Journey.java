package F28DA_CW2;

import java.util.List;
import java.util.LinkedList;
import org.jgrapht.GraphPath;

public class Journey implements IJourneyPartB<Airport, Flight>, IJourneyPartC<Airport, Flight> {

	private GraphPath<Airport, Flight> gp;

	// constructor for a journey object
	public Journey(GraphPath<Airport, Flight> graphPath) { gp = graphPath; }
	public Journey() {}

	@Override // [part B] [done]
	public List<String> getStops() {
		List<Airport> listA = gp.getVertexList(); // holds the list of airport objects
		LinkedList<String> listCode = new LinkedList<String>(); // will hold the list of airport codes
		for (int i = 0; i < listA.size(); i++) listCode.add(listA.get(i).getCode()); // fill the list
		return listCode; // the list of airport codes in the graph path travelled
	}

	@Override // [part B] [done]
	public List<String> getFlights() {
		List<Flight> listF = getEdges(); // holds the list of flight objects
		LinkedList<String> listCode = new LinkedList<String>(); // will hold the list of flight codes
		for (int i = 0; i < listF.size(); i++) listCode.add(listF.get(i).getFlightCode()); // fill the list
		return listCode; // the list of flight codes in the graph path travelled
	}

	// return the number of connections in the graph path traveled
	@Override // [part B] [done]
	public int totalHop() { return getEdges().size(); }

	@Override // [part B] [done]
	public int totalCost() {
		List<Flight> listF = getEdges(); int c = 0;
		// get the sum of costs of all the flights
		for (int i = 0; i < listF.size(); i++) c += listF.get(i).getCost();
		return c; // the total cost of all the flights involved in the graph path travelled
	}

	@Override // [part B] [done]
	public int airTime() {
		List<Flight> listF = getEdges(); int t = 0;
		for (int i = 0; i < listF.size(); i++) // add each flight's air time
			t += getMinutes(listF.get(i).getFromGMTime(), listF.get(i).getToGMTime());
		return t; // the time spent in the air in all the flights involved in the graph path travelled
	}

	@Override // [part C] [done] (ADVANCED)
	public int connectingTime() {
		List<Flight> listF = getEdges(); int t = 0;
		for (int i = 0; i < listF.size()-1; i++) // add each flight's connecting time
			t += getMinutes(listF.get(i).getToGMTime(), listF.get(i+1).getFromGMTime());
		return t; // the connecting time between flights in the graph path travelled
	}

	// returns the total time spent for the journey
	@Override // [part C] [done] (ADVANCED)
	public int totalTime() { return airTime() + connectingTime(); }

	// helper method to return a list of the flights involved in the graph path travelled
	public List<Flight> getEdges() { return gp.getEdgeList(); }

	// helper method that returns the number of minutes between two time strings in the format 'HHMM'
	private int getMinutes(String prev, String next) {
		// get the previous time (pT) and next time (nT) in hours and
		// then subtract these 2 times to get the time between them
		float pT = getHours(prev), nT = getHours(next), sub = nT - pT;
		// if the subtraction is negative, consider that
		// nT is in the next day by adding 24 hours to nT
		if (sub < 0) sub = (nT + 24) - pT;
		int hours = 0, minutes = 0;
		// count the hours taken between the two times
		while (sub >= 1f) { sub -= 1; hours++; }
		// if there is time remaining, round
		// the time to the nearest minute
		if (sub != 0) minutes = Math.round(sub * 60);
		// return the total number of minutes 
		// between the two given times
		return minutes + (hours * 60);
	}
	
	// helper method that takes a 4 digit time string in the format 'HHMM' and returns the hours
	private float getHours(String s) {
		return Float.parseFloat(s.substring(0, 2)) + Float.parseFloat(s.substring(2, 4)) / 60f;
	}
	
	// helper method to get the air time of a flight
	public int time(Flight f) {
		return getMinutes(f.getFromGMTime(), f.getToGMTime());
	}
}
