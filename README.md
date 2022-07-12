# **Flying Planner**

## **Summary**
This report reviews a flying planner program that was developed using JgraphT, a Java library specialized 
for graphs and graph algorithms. A simple directed weighted graph was used to construct the API’s flight 
network and the API’s functionality included least cost, least hop, least time, and best meet up journeys
for airports in the network. A user interface was designed to make the program engaging and interactive.
The coursework consisted simple (part A), intermediate (part B), and advanced (part C) parts, all of which
were attempted.

---

## **Design Choices**
### *Airport.java*
The Airport class was used to represent airport objects, which were nodes in the graph network, and each 
object held the following information: airport code, airport name, airport location, a set of all the airports 
directly connected to this airport, and the size of this directly connected set.

### *Flight.java*
The Flight class was used to represent flight objects, which were edges in the graph network, and each 
flight object the following information: flight code, the flight’s departure airport, the flight’s departure 
GMT time, the flight’s destination airport, the flight’s destination GMT time, and the cost of the flight.

### *Journey.java*
The Journey class represented the journey travelled from one airport to another and it used a graph path 
to represent this journey. The class could calculate the following information about the journey: the 
airports travelled, the flights taken, the number of connections in the journey, the total cost of the 
journey, the time spent in the air, the time spent between connecting flights, and the total time of the 
journey. A helper method was added to the journey class to calculate the time taken for a single flight to 
travel from its departure airport to its destination airport.

### *FlyingPlannerMainPartA.java*
The FlyingPlannerMainPartA class implemented a user interface to find the least cost journey between 2 
airports. The class used a simple directed weighted graph with 5 airports and 12 flights. The interface 
required the user to enter their departure and destination airports. An input checker method was 
designed to allow the user to enter the airport names with any case and would prompt the user to re-enter if the input was invalid. Regular expressions and substrings were used in the input checker method
to remove trailing white spaces and handle the letter case.

### *FlyingPlannerMainPartBC.java*
The FlyingPlannerMainPartBC class housed the main user interface. The program prompted the user to 
choose methods from part B or part C. The allowable selections were: partx, x, n (replace x with b or c and 
replace n with 1 or 2). After choosing the section, the user was prompted to choose least cost, or least 
hops, or least time. The user was allowed to input the full method name, or the second word of the 
method (e.g., cost), or a number between 1 and 3. If the user selected part C, the meetup equivalent 
versions of the methods were given as a prompt (e.g., leastCostMeetUp).
A method printJourney was created to reduce code duplicity since both the part B and part C interfaces 
required printing the user’s travel journey. The method printed the journey information by legs, if for 
example, the journey required 3 flights to fly to the destination, the method would print 3 legs. The 
following information was printed for each leg: the leg number, the departure airport, the departing time, 
the flight code, the arrival airport, and the arrival time. After this the following information was printed: 
the journey’s total hops (number of legs), the total cost of the journey, and the total number of minutes 
spent in the air.

### *FlyingPlanner.java*
The FlyingPlanner class was the main program which performed the journey travel calculations. A dataset 
containing information on flights and airports, from the open-source project Open Flights, was used to 
build the flight network. Due to the HashMap data structure having O(1) time complexity of its content 
retrieval, HashMaps were used to store each airport and flight object for fast object retrieval and to
improve program performance.

The API was designed to perform the least cost, least hop, and least time journeys between 2 airports. 
The program was extended to calculate least cost, least hop, and least time meet-up for 2 airports. During 
the implementation process it was observed that the least cost, least hop, and least time methods and 
the meet-up methods had code repetition. Thus, two methods, getJourney and getMeetup, were created to handle these operations and reduce code duplicity.

FlyingPlanner could find the airports directly connected to a given airport as well as the number of these 
airports (the order) and then add these airports to a set. The set of directly connected airports was found 
using functional programming methods ‘map’ and ‘filter’. First the airports connected to the given airport 
was found using ‘map’ and then these airports were filtered to include only the airports connected with
a 2-way connection to the given airport. The functional programming approach was taken because of the 
simplicity in 2 operations being performed in 1 line of code. The directly connected airports was found for 
each airport and then a directed acyclic graph (DAG) was made according to the topological ordering of 
each airport’s directly connected orders.

---

## **Run the program**
There are 2 main methods in this project located in the following files: FlyingPlannerMainPartA.java and FlyingPlannerMainPartBC.java. Run these methods and try them out!

Sample run from FlyingPlannerMainPartBC.java:
```
Welcome to the Flying Planner! Calculate the best journey for your travels!
Pick a section to begin: 
1. Part B 
2. Part C
Your choice: 1                                 <--- user input

Part B selected.
Pick your method:
1. leastCost
2. leastHop
3. leastTime
Your choice: 1                                 <--- user input

Please enter the departure airport code: NCL   <--- user input

Please enter the destination airport code: NTL <--- user input

Journey for Newcastle Airport (NCL) to Newcastle Airport (NTL)
Leg    Leave                      At       On           Arrive                    At
----------------------------------------------------------------------------------------
1      Newcastle (NCL)            19:18    KL7893       Amsterdam (AMS)           20:04
----------------------------------------------------------------------------------------
2      Amsterdam (AMS)            07:47    CX0831       Hong Kong (HKG)           17:02
----------------------------------------------------------------------------------------
3      Hong Kong (HKG)            07:48    CX7100       Brisbane (BNE)            14:27
----------------------------------------------------------------------------------------
4      Brisbane (BNE)             16:28    QF0640       Newcastle (NTL)           17:29
----------------------------------------------------------------------------------------

Total Journey Hops = 4
Total Journey Cost =  £1035
Total Time in the Air = 1061
```

---

## **Conclusion**
Using advanced Java libraries and functional programming in the FlyingPlanner logic was a blast. Check the project files out and have fun!
