package F28DA_CW2;

import java.io.FileNotFoundException;
// extras
import java.util.List;
import java.util.Scanner;

/**
 * @author Moses Varghese
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
public class FlyingPlannerMainPartBC {

	private static FlyingPlanner fi;
	private static Scanner scan = new Scanner(System.in);
	private static boolean optionChosen = false;

	public static void main(String[] args) {

		fi = new FlyingPlanner();
		// Your implementation should be in FlyingPlanner.java,
		// this class is only to run the user interface of your program.
		try {
			fi.populate(new FlightsReader());
			// Implement here your user interface using the methods of Part B. You could
			// optionally expand it to use the methods of Part C.
			String invalidChoice = "Invalid selection. Exiting...", choice = "\nYour choice: ", option = "";
			
			// welcome message
			System.out.print("\n\nWelcome to the Flying Planner! Calculate the best journey for your travels!" +
			"\nPick a section to begin: " +
			"\n1. Part B " +
			"\n2. Part C" + choice);
			option = scan.nextLine().toLowerCase().trim().replaceAll(" ", "");

			// part B interface
			if (option.equals("partb") || option.equals("b") || option.equals("1")) {
				System.out.print("\nPart B selected. " +
				"\nPick your method: " +
				"\n1. leastCost " +
				"\n2. leastHop " +
				"\n3. leastTime" + choice);
				option = scan.nextLine().toLowerCase().trim().replaceAll(" ", "");

				// calculate journey
				if (option.equals("leastcost") || option.equals("cost") || option.equals("1")) partBInterface("leastCost");
				if (option.equals("leasthop") || option.equals("hop") || option.equals("2")) partBInterface("leastHop");
				if (option.equals("leasttime") || option.equals("time") || option.equals("3")) partBInterface("leastTime");
			}

			// part C interface
			else if (option.equals("partc") || option.equals("c") || option.equals("2")) {
				System.out.print("\nPart C selected. " +
				"\nPick your method: " +
				"\n1. leastCostMeetUp " +
				"\n2. leastHopMeetUp " +
				"\n3. leastTimeMeetUp" + choice);
				option = scan.nextLine().toLowerCase().trim().replaceAll(" ", "");

				// calculate best meetup
				if (option.equals("leastcostmeetup") || option.equals("cost") || option.equals("1")) partCInterface("leastCostMeetUp");
				if (option.equals("leasthopmeetup") || option.equals("hop") || option.equals("2")) partCInterface("leastHopMeetUp");
				if (option.equals("leasttimemeetup") || option.equals("time") || option.equals("3")) partCInterface("leastTimeMeetUp");
			}

			// invalid input
			if (!optionChosen) {
				System.out.println(invalidChoice);
				System.exit(0);
			}
		} catch (FileNotFoundException | FlyingPlannerException e) {
			e.printStackTrace();
		}
	}

	private static void partBInterface(String s) {
		Journey j = null; String strtCodeA = null, destCodeA = null;
		while (true) { // keep looping while the input(s) are invalid
			try {
				System.out.printf("\nPlease enter the departure airport code: ");
				strtCodeA = scan.nextLine().trim(); // scan the input and remove any trailing white spaces
				System.out.printf("\nPlease enter the destination airport code: ");
				destCodeA = scan.nextLine().trim(); // scan the input and remove any trailing white spaces

				// calculate the journey between the start and destination airports
				if (s.equals("leastCost")) j = fi.leastCost(strtCodeA, destCodeA);
				if (s.equals("leastHop")) j = fi.leastHop(strtCodeA, destCodeA);
				if (s.equals("leastTime")) j = fi.leastTime(strtCodeA, destCodeA);
				break; // if the inputs were valid, break out of the loop
			} catch (FlyingPlannerException e) {
				System.err.printf("\n\tInvalid airport code(s). Try again.\n");
			}
		}
		
		// print the journey information to the user
		printJourney(s, strtCodeA, destCodeA, j);
		optionChosen = true;
	}
	
	private static void partCInterface(String s) {
		String code1 = null, code2 = null, meetUp = null;
		Journey j1 = null, j2 = null;
		while (true) { // keep looping while the input(s) are invalid
			try {
				System.out.printf("\nPlease enter person1's airport code: ");
				code1 = scan.nextLine().trim(); // scan the input and remove any trailing white spaces
				System.out.printf("\nPlease enter person2's airport code: ");
				code2 = scan.nextLine().trim(); // scan the input and remove any trailing white spaces

				// calculate the best meetup airport between airport 1 and airport 2
				if (s.equals("leastCostMeetUp")) meetUp = fi.leastCostMeetUp(code1, code2);
				if (s.equals("leastHopMeetUp")) meetUp = fi.leastHopMeetUp(code1, code2);
				if (s.equals("leastTimeMeetUp")) meetUp = fi.leastHopMeetUp(code1, code2);

				// calculate the journeys taken by the 2 people
				j1 = fi.leastCost(code1, meetUp); j2 = fi.leastCost(code2, meetUp);
				break; // if the inputs were valid, break out of the loop
			} catch (FlyingPlannerException e) {
				System.err.printf("\n\tInvalid airport code(s). Try again.\n");
			}
		}

		// print the person1's journey information to the user
		System.out.println("\n\nThe best journey for person1 is: ");
		printJourney(s, code1, meetUp, j1);
		
		// print the person2's journey information to the user
		System.out.println("\n\nThe best journey for person2 is: ");
		printJourney(s, code2, meetUp, j2);

		// notify the user of the best meetup airport
		System.out.println("\nThe airport code for the best meet-up for airports " + code1 + " and " + code2 + ": " + meetUp);
		optionChosen = true;
	}
	
	private static void printJourney(String s, String strtCodeA, String destCodeA, Journey j) {
		Airport strtA = fi.airport(strtCodeA), destA = fi.airport(destCodeA);
		// print the journey to the user
		System.out.printf("\nJourney for %s (%s) to %s (%s)\n", strtA.getName(), strtA.getCode(), destA.getName(), destA.getCode());
		List<Flight> flightsList = j.getEdges();
		System.out.println("Leg    Leave		   	  At	   On		Arrive		   	  At");
		System.out.println("----------------------------------------------------------------------------------------");
		for (int i = 0; i < flightsList.size(); i++) {
			Flight f = flightsList.get(i); String strtT = f.getFromGMTime(), destT = f.getToGMTime();
			// use String.format to print each leg of the journey
			System.out.println(String.format("%-7s", i+1) +                            // leg
			String.format("%-27s", f.getFrom()) +                                      // leave
			String.format("%-9s", strtT.substring(0, 2) + ":" + strtT.substring(2)) +  // at
			String.format("%-13s", f.getFlightCode()) +                                // on
			String.format("%-26s", f.getTo()) +                                        // arrive
			String.format("%-6s", destT.substring(0, 2) + ":" + destT.substring(2)));  // at
			System.out.println("----------------------------------------------------------------------------------------");
		}
		System.out.println("\nTotal Journey Hops = " + j.totalHop());
		System.out.println("Total Journey Cost =  £" + j.totalCost());
		System.out.println("Total Time in the Air = " + j.airTime());
	}

}
