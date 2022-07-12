package F28DA_CW2;

import java.util.List; import java.util.HashMap; import java.util.Set; import java.util.HashSet; import java.util.stream.Collectors; 
import org.jgrapht.GraphPath; import org.jgrapht.graph.DirectedAcyclicGraph; import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.Graphs; import org.jgrapht.traverse.DepthFirstIterator; import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

/**
 * @author Moses Varghese
 * 
 * FlyingPlanner can perform leastCost, leastHop, leastTime, leastCostMeetUp, leastHopMeetUp, leastTimeMeetUp. 
 * Directly connected order and Journey duration implemented in the code.
 * 
 * References:
 * (1) https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html (documentation on 'stream')
 * (2) https://canvas.hw.ac.uk/courses/5374/pages/lecture-12-%7C-digraphs?module_item_id=950137 (canvas slides on DAG)
 * (3) https://docs.oracle.com/javase/8/docs/api/java/util/logging/Filter.html (documentation on 'filter')
 * (4) https://www.w3schools.com/jsref/jsref_filter.asp (information on 'filter')
 * (5) https://www.journaldev.com/32457/java-stream-collect-method-examples (for collecting into a set)
 * (6) https://www.baeldung.com/java-convert-iterator-to-list (for converting iterator to a collection)
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
public class FlyingPlanner implements IFlyingPlannerPartB<Airport, Flight>, IFlyingPlannerPartC<Airport, Flight> {

	private SimpleDirectedWeightedGraph<Airport, Flight> G; private DirectedAcyclicGraph<Airport, Flight> DAG; // the main graph, the DAG
	private HashMap<String, Airport> airportMap = new HashMap<>(); private HashMap<String, Flight> flightMap = new HashMap<>();
	private boolean SDCcheck = false, SDCOcheck = false; // advanced method prerequisite checkers

	@Override // [part B] [done]
	public boolean populate(FlightsReader fr) { return populate(fr.getAirports(), fr.getFlights()); }

	@Override // [part B] [done]
	public boolean populate(HashSet<String[]> airports, HashSet<String[]> flights) {
		G = new SimpleDirectedWeightedGraph<>(Flight.class); // initialize the graph
		// add all the airports to the graph and to the airport HashMap with the airport code as hash key (retrieval is O(1))
		airports.forEach(a -> { Airport v = new Airport(a[0], a[1], a[2]); G.addVertex(v); airportMap.put(a[0], v);});
		// add all the flights to the graph and to the flight HashMap with the flight code as hash key (retrieval is O(1))
		flights.forEach(f -> { Flight e = new Flight(f[0], airport(f[1]), f[2], airport(f[3]), f[4], Integer.valueOf(f[5]));
			G.addEdge(airport(f[1]), airport(f[3]), e); flightMap.put(f[0], e);});
		return (G.vertexSet().size() == airports.size() && G.edgeSet().size() == flights.size()); } // check all the nodes and edges were added
	
	@Override // [part B] [done]
	public Airport airport(String code) { return airportMap.get(code); }
	
	@Override // [part B] [done]
	public Flight flight(String code) { return flightMap.get(code); }

	@Override // [part B] [done]
	public Journey leastCost(String from, String to) throws FlyingPlannerException { 
		return leastCost(from, to, null); }

	@Override // [part B] [done]
	public Journey leastHop(String from, String to) throws FlyingPlannerException { 
		return leastHop(from, to, null); }

	/** Returns a fastest flight journey from one airport (airport code) to another */
	public Journey leastTime(String from, String to) throws FlyingPlannerException { 
		return leastTime(from, to, null); }

	@Override // [part B] [done]
	public Journey leastCost(String from, String to, List<String> excluding) throws FlyingPlannerException {
		return getJourney(1, from, to, excluding); } // call the helper method to get the journey

	@Override // [part B] [done]
	public Journey leastHop(String from, String to, List<String> excluding) throws FlyingPlannerException {
		return getJourney(2, from, to, excluding); } // call the helper method to get the journey

	/** Returns a fastest flight journey from one airport (airport code) to another, excluding a list of airport (airport codes) */
	public Journey leastTime(String from, String to, List<String> excluding) throws FlyingPlannerException {
		return getJourney(3, from, to, excluding); } // call the helper method to get the journey

	@Override // [part C] [done] (ADVANCED)
    public Set<Airport> directlyConnected(Airport airport) { // map to get the connected airports then filter these airports, >> References (4)
        Set<Airport> dc = G.outgoingEdgesOf(airport).stream().map(f -> G.getEdgeTarget(f)).filter(a -> con(a, airport)).collect(Collectors.toSet());
        airport.setDirectlyConnected(dc); airport.setDirectlyConnectedOrder(dc.size()); return dc; } // set the dc and dc order and return the set
	
	@Override // [part C] [done] (ADVANCED)
	public int setDirectlyConnected() { // call directlyConnected for each airport and return the sum of their directlyConnected orders
		int sum = 0; for (Airport a : airportMap.values()) sum += directlyConnected(a).size(); return sum; }
	
	@Override // [part C] [done] (ADVANCED)
	public int setDirectlyConnectedOrder() {
		if (!SDCcheck) { setDirectlyConnected(); SDCcheck = true; } // ensure every airport's directlyConnected set has been assigned
		DAG = new DirectedAcyclicGraph<>(Flight.class); Graphs.addAllVertices(DAG, G.vertexSet()); // initialize the DAG, add all airports
		flightMap.values().forEach(f -> { // add the flight if the destination has more direct connections than the start airport
			if (f.getTo().getDirectlyConnectedOrder() > f.getFrom().getDirectlyConnectedOrder()) DAG.addEdge(f.getFrom(), f.getTo(), f);});
		return DAG.edgeSet().size(); } // return the number of flights (edges) in the DAG
	
	@Override // [part C] [done] (ADVANCED)
	public Set<Airport> getBetterConnectedInOrder(Airport airport) {
		if (!SDCOcheck) { setDirectlyConnectedOrder(); SDCOcheck = true; } // ensure the directed acyclic graph (DAG) has been created
		 // use DFS to add all the airports in the DAG that can be reached from the given 'airport' to a set
		HashSet<Airport> BCIO = new HashSet<Airport>(); // except for the given 'airport' then return the set
		new DepthFirstIterator<>(DAG, airport).forEachRemaining(BCIO::add); BCIO.remove(airport); return BCIO; } // >> References (6)
	
	@Override // [part C] [done] (ADVANCED)
	public String leastCostMeetUp(String at1, String at2) throws FlyingPlannerException {
		return getMeetUp(1, at1, at2); } // call the helper method to get the airport code
	
	@Override // [part C] [done] (ADVANCED)
	public String leastHopMeetUp(String at1, String at2) throws FlyingPlannerException {
		return getMeetUp(2, at1, at2); } // call the helper method to get the airport code
	
	/** Returns the airport code for the fastest meetup between 2 airports */
	public String leastTimeMeetUp(String at1, String at2) throws FlyingPlannerException {
		return getMeetUp(3, at1, at2); } // call the helper method to get the airport code

	@Override // [part C] [incomplete] (ADVANCED)
	public String leastTimeMeetUp(String at1, String at2, String startTime) throws FlyingPlannerException { return "CDG"; }
	
	// helper method to get the least cost or hops or time journey. the arguments it takes in are the graph, the
	// start and destination airports and the integer option 'n' which decides which journey type to calculate
	private Journey getJourney(int n, String from, String to, List<String> excluding) throws FlyingPlannerException {
		Airport strtA = airport(from), destA = airport(to);
		if (strtA == null || destA == null || from == to) throw new FlyingPlannerException(); // invalid input check
		SimpleDirectedWeightedGraph<Airport, Flight> H = new SimpleDirectedWeightedGraph<>(Flight.class);
		Graphs.addAllVertices(H, airportMap.values()); Graphs.addAllEdges(H, G, flightMap.values()); // create a copy of 'G'
		if (excluding != null && excluding.size() != 0) excluding.forEach(codeA -> H.removeVertex(airport(codeA)));
		H.edgeSet().forEach(f -> { // ^ remove the airports in the excluding list if needed
			if (n==1) H.setEdgeWeight(f, f.getCost()); // leastCost: set edge weights as the flight cost
			if (n==2) H.setEdgeWeight(f, 420d); // leastHop: set edge weights equal to each other
			if (n==3) H.setEdgeWeight(f, new Journey().time(f));});// leastTime: set edge weights as the flight time
		// use Dijkstra's algorithm to get the shortest path between the 2 airports
		GraphPath<Airport, Flight> path = new DijkstraShortestPath<Airport, Flight>(H).getPath(strtA, destA);
		if (path == null) throw new FlyingPlannerException("No available routes from " + from + " to " + to);
		return new Journey (path); // throw an error if the path doesn't exist, otherwise return the journey
	}
	// helper method to get the least cost or hop or time meetup between 2 airports. the arguments it takes in are
	// the start and destination airports and the integer option 'n' which decides which meetup type to calculate
	private String getMeetUp(int n, String at1, String at2) throws FlyingPlannerException {
		Airport a1 = airport(at1), a2 = airport(at2);
		if (a1 == null || a2 == null || at1 == at2) throw new FlyingPlannerException(); // invalid input check
		String codeA = ""; int current = 0, least = Integer.MAX_VALUE; List<String> listA = null;
		// get a list of potential airports that would be the best meetup location
		if (n==1) do listA = leastCost(at1, at2).getStops(); while (listA.size() < 2); // costMeetup
		if (n==2) do listA = leastHop(at1, at2).getStops(); while (listA.size() < 2);  // hopsMeetup
		if (n==3) do listA = leastTime(at1, at2).getStops(); while (listA.size() < 2); // timeMeetup
		listA.remove(at2); listA.remove(at1); for (String code : listA) { // for each airport in the list
			if (n==1) current = leastCost(at1, code).totalCost() + leastCost(at2, code).totalCost(); // costMeetup
			if (n==2) current = leastHop(at1, code).totalHop() + leastHop(at2, code).totalHop();     // hopsMeetup
			if (n==3) current = leastTime(at1, code).totalTime() + leastTime(at2, code).totalTime(); // timeMeetup
			if (current < least) { least = current; codeA = code; } } // update the least cost, hops, or time
		return codeA; // return the code of the best airport for the meetup
	}
	/** helper method to check if 2 airports are connected used in {@link #directlyConnected(Airport airport)} */
	private boolean con(Airport a1, Airport a2) { 
		for (Flight f : G.outgoingEdgesOf(a1)) if (f.getTo().equals(a2)) return true; return false; }
	
}