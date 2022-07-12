package F28DA_CW2;

import java.util.Scanner; import org.jgrapht.GraphPath; import org.jgrapht.alg.shortestpath.DijkstraShortestPath; 
import org.jgrapht.graph.DefaultWeightedEdge; import org.jgrapht.graph.SimpleDirectedWeightedGraph;

/**
 * @author Moses Varghese
 * 
 * FlyingPlannerMainPartA implements a simple directed weighted graph which consists of 5 airports and 12 edges with unique weights.
 * The user can input their start and destination airport, the program has checks to allow the user to re-enter their input
 * if their previous input was invalid. Once the inputs are valid, the program will find the shortest path between them.
 * This path is printed out for the user to see.
 * 
 * References:
 * (1) https://pencilprogrammer.com/java-programs/capitalize-first-letter-of-each-word/#:~:text=We%20can%20easily%20convert%20the,toUpperCase()%20on%20each%20word.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
public class FlyingPlannerMainPartA {

    private static SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> G;
    private static Scanner scan = new Scanner(System.in);
    private static String[] a = {"Edinburgh", "Heathrow", "Dubai", "Sydney", "Kuala Lumpur"}; // airports used in the graph
    private static String strtA, destA; // start airport, desitination airport

    public static void main(String[] args) {
        // build the graph, add the airports as vertexes and add all edges with their weights
        G = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        for (String v : a) G.addVertex(v); addAllEdges();
        System.out.print("\nThe following airports are used :\n");
        for (String v : G.vertexSet()) System.out.println("\t" + v); // print the airports used
        
        // get the start and destination airports
        System.out.print("\nPlease enter the start airport: ");
        strtA = inputCheck(strtA);
        System.out.print("\nPlease enter the destination airport: ");
        destA = inputCheck(destA);

        // user interface. get and print the shortest path to the user
        System.out.println("\nShortest (i.e. cheapest) path:");
        // get the shortest path from the start airport to the destination airport
        GraphPath<String, DefaultWeightedEdge> path = DijkstraShortestPath.findPathBetween(G, strtA, destA);
        String[] arr = path.toString().split(","); // convert the path to a String array
        // replace any brackets and format the colons to arrows in each index of the path array using regex (regular expressions)
        for (int i = 0; i < arr.length; i++) System.out.println((i+1) + ". " + arr[i].replaceAll("[()\\[\\]]", "").replace(":", " -> ").trim());
        System.out.println("Cost of shortest (i.e. cheapest) path =  £ " + path.getWeight());
        scan.close();
    }
    
    // helper method to add an edge to the graph and set the edge's weight
    private static void addEdge(String city1, String city2, int weight) {
        G.setEdgeWeight(G.addEdge(city1, city2), weight);
        G.setEdgeWeight(G.addEdge(city2, city1), weight);
    }
    
    // helper method to add all the edges to the graph with their weights
    private static void addAllEdges() {
        // add the edges and set the edge weights
        addEdge(a[0], a[1], 80);  //    Edinburgh <-> Heathrow       |  £80
        addEdge(a[1], a[2], 130); //     Heathrow <-> Dubai          | £130
        addEdge(a[1], a[3], 570); //     Heathrow <-> Sydney         | £570
        addEdge(a[2], a[4], 170); //        Dubai <-> Kuala Lumpur   | £170
        addEdge(a[2], a[0], 190); //        Dubai <-> Edinburgh      | £190
        addEdge(a[4], a[3], 150); // Kuala Lumpur <-> Sydney         | £150
    }
    
    // helper method to check the airport input from the user
    private static String inputCheck(String input) {
        boolean valid = false;
        while (!valid) {
            input = scan.nextLine().toLowerCase().trim(); // convert to lower case and remove unnecessary spaces
            // make the first letter the word(s) from the input capital, inspired from (1)
            String[] words = input.split("\\s"); input = "";
            for (String w : words) input += w.substring(0,1).toUpperCase() + w.substring(1) + " ";  
            input = input.trim(); // remove any trailing white spaces
            for (String s : a) if (s.equals(input)) valid = true; // check if the input is valid
            // if invalid, loop again until valid
            if (!valid) System.out.print("\nInvalid input. \nTry again (use one space separation if needed): "); }
        return input;
    }

}
