package cs2321;

import net.datastructures.Edge;
import net.datastructures.Entry;
import net.datastructures.Vertex;

/**
 * @author Ruihong Zhang
 * Reference textbook R14.16 P14.81 
 *
 */
public class Travel {
	
	public AdjListGraph<String, Integer> graph;
	public HashMap<String, Vertex<String>> vertexMap = new HashMap<>();
	
	/**
	 * @param routes: Array of routes between cities. 
	 *                routes[i][0] and routes[i][1] represent the city names on both ends of the route. 
	 *                routes[i][2] represents the cost in string type. 
	 *                Hint: In Java, use Integer.valueOf to convert string to integer. 
	 */
	public Travel(String [][] routes) {

		graph = new AdjListGraph<String, Integer>();
		HashMap<String,String> uniqueLocations = new HashMap<String,String>();
		
		for(int i = 0; i < routes.length; i++) {
			uniqueLocations.put(routes[i][0],routes[i][0]);
			uniqueLocations.put(routes[i][1],routes[i][1]);
		}
		
		for(String i : uniqueLocations.values()) {
			vertexMap.put(i, graph.insertVertex(i));
		}
		
		for(int i = 0; i < routes.length; i++) {
			graph.insertEdge(vertexMap.get(routes[i][0]), vertexMap.get(routes[i][1]), Integer.valueOf(routes[i][2]));
		}
		
	}
	
	/**
	 * @param departure: the departure city name 
	 * @param destination: the destination city name
	 * @return Return the path from departure city to destination using Depth First Search algorithm. 
	 *         The path should be represented as ArrayList or DoublylinkedList of city names. 
	 *         The order of city names in the list should match order of the city names in the path.  
	 *         
	 * @IMPORTANT_NOTE: The outgoing edges should be traversed by the order of the city names stored in
	 *                 the opposite vertices. For example, if V has 3 outgoing edges as in the picture below,
	 *                           V
	 *                        /  |  \
	 *                       /   |   \
	 *                      B    A    F  
	 *              your algorithm below should visit the outgoing edges of V in the order of A,B,F.
	 *              This means you will need to create a helper function to sort the outgoing edges by 
	 *              the opposite city names.
	 *              	              
	 *              See the method sortedOutgoingEdges below. 
	 */
	public Iterable<String> DFSRoute(String departure, String destination ) {
		HashMap<Vertex<String>, Edge<Integer>> forest = new HashMap<>();
		DoublyLinkedList<String> path = new DoublyLinkedList<>();
		
		forest = DFSR(vertexMap.get(departure), vertexMap.get(departure), forest);
		
		Vertex<String> current = vertexMap.get(destination);
		Edge<Integer> e;
		path.addFirst(destination);
		while(current != vertexMap.get(departure)) {
			e = forest.get(current);
			current = graph.opposite(current, e);
			path.addFirst(current.getElement());
		}
		
		return path;
	}
	
	public HashMap<Vertex<String>, Edge<Integer>> DFSR(Vertex<String> d, Vertex<String> v, HashMap<Vertex<String>, Edge<Integer>> f){
		if(graph.outDegree(v) == 1 && v != d) {
			return f;
		}
		for(Edge<Integer> e : sortedOutgoingEdges(v)) {
			if(f.get(graph.opposite(v, e)) == null && graph.opposite(v, e) != d) {
				f.put(graph.opposite(v, e), e);
				f = DFSR(d, graph.opposite(v, e), f);
			}
		}
		return f;
	}
	
	/**
	 * @param departure: the departure city name 
	 * @param destination: the destination city name
     * @return Return the path from departure city to destination using Breadth First Search algorithm. 
	 *         The path should be represented as ArrayList or DoublylinkedList of city names. 
	 *         The order of city names in the list should match order of the city names in the path.  
	 *         
	 * @IMPORTANT_NOTE: The outgoing edges should be traversed by the order of the city names stored in
	 *                 the opposite vertices. For example, if V has 3 outgoing edges as in the picture below,
	 *                           V
	 *                        /  |  \
	 *                       /   |    \
	 *                      B    A     F  
	 *              your algorithm below should visit the outgoing edges of V in the order of A,B,F.
	 *              This means you will need to create a helper function to sort the outgoing edges by 
	 *              the opposite city names.
	 *              	             
	 *              See the method sortedOutgoingEdges below. 
	 */
	
	public Iterable<String> BFSRoute(String departure, String destination ) {
		HashMap<Vertex<String>, Edge<Integer>> forest = new HashMap<>();
		DoublyLinkedList<String> path = new DoublyLinkedList<>();
		
		DoublyLinkedList<Vertex<String>> temp = new DoublyLinkedList<>();
		temp.addFirst(vertexMap.get(departure));
		forest = BFSR(vertexMap.get(departure), temp, forest);
		
		Vertex<String> current = vertexMap.get(destination);
		Edge<Integer> e;
		path.addFirst(destination);
		while(current != vertexMap.get(departure)) {
			e = forest.get(current);
			current = graph.opposite(current, e);
			path.addFirst(current.getElement());
		}
		
		return path;
	}
	
	public HashMap<Vertex<String>, Edge<Integer>> BFSR(Vertex<String> d, DoublyLinkedList<Vertex<String>> vList, HashMap<Vertex<String>, Edge<Integer>> f){
		if(vList.isEmpty()) {
			return f;
		}
		DoublyLinkedList<Vertex<String>> list = new DoublyLinkedList<>();
		for(Vertex<String> v : vList) {
			if(!(graph.outDegree(v) == 1 && v != d)) {
				for(Edge<Integer> e : sortedOutgoingEdges(v)) {
					if(f.get(graph.opposite(v, e)) == null && graph.opposite(v, e) != d) {
						f.put(graph.opposite(v, e), e);
						list.addLast(graph.opposite(v, e));
					}
				}
			}
		}
		
		BFSR(d, list, f);
		
		return f;
	}
	
	/**
	 * @param departure: the departure city name 
	 * @param destination: the destination city name
	 * @param itinerary: an empty DoublylinkedList object will be passed in to the method. 
	 * 	       When a shorted path is found, the city names in the path should be added to the list in the order. 
	 * @return return the cost of the shortest path from departure to destination. 
	 *         
	 * @IMPORTANT_NOTE: The outgoing edges should be traversed by the order of the city names stored in
	 *                 the opposite vertices. For example, if V has 3 outgoing edges as in the picture below,
	 *                           V
	 *                        /  |  \
	 *                       /   |    \
	 *                      B    A     F  
	 *              your algorithm below should visit the outgoing edges of V in the order of A,B,F.
	 *              This means you will need to create a helper function to sort the outgoing edges by 
	 *              the opposite city names.
	 *              
	 *              See the method sortedOutgoingEdges below. 
	 */

	public int DijkstraRoute(String departure, String destination, DoublyLinkedList<String> itinerary ) {
		HashMap<Vertex<String>,Integer> d = new HashMap<>();
		HeapPQ<Integer, Vertex<String>> pq = new HeapPQ<>();
		HashMap<Vertex<String>, Edge<Integer>> forest = new HashMap<>();
		HashMap<Vertex<String>, Entry<Integer,Vertex<String>>> tokens = new HashMap<>(); 
		
		for(Vertex<String> v : graph.vertices()) {
			if(v.getElement() == departure) {
				d.put(v, 0);
				tokens.put(v, pq.insert(0, v));
			} else {
				d.put(v, Integer.MAX_VALUE);
				tokens.put(v, pq.insert(Integer.MAX_VALUE, v));
			}
		}
		
		Entry<Integer, Vertex<String>> e;
		Integer dist, newD, oldD;
		Vertex<String> vert, op;
		
		while(pq.size() > 0) {
			e = pq.removeMin();
			dist = e.getKey();
			vert = e.getValue();
			for(Edge<Integer> edge : sortedOutgoingEdges(vert)) {
				op = graph.opposite(vert,  edge);
				newD = dist + edge.getElement();
				oldD = d.get(op);
				if(newD < oldD) {
					d.put(op, newD);
					pq.replaceKey(tokens.get(op), newD);
					forest.put(op, edge);
				}
			}
		}
		
		Vertex<String> current = vertexMap.get(destination);
		Vertex<String> start = vertexMap.get(departure);
		while (current != start) {
			itinerary.addFirst(current.getElement());
			current = graph.opposite(current, forest.get(current));
		}
		itinerary.addFirst(start.getElement());
		
		return d.get(vertexMap.get(destination));
		
	}

	/**
	 * I strongly recommend you to implement this method to return sorted outgoing edges for vertex V
	 * You may use any sorting algorithms, such as insert sort, selection sort, etc.
	 * 
	 * @param v: vertex v
	 * @return a list of edges ordered by edge's name
	 */
	
	public Iterable<Edge<Integer>> sortedOutgoingEdges(Vertex<String> v)  {
		
		DoublyLinkedList<Edge<Integer>> ordered = new DoublyLinkedList<>();
		HashMap<String, Edge<Integer>> keys = new HashMap<>();
		QuickSort<String> sorter = new QuickSort<>();
		String[] names = new String[graph.outDegree(v)];
		
		for(Edge<Integer> e : graph.outgoingEdges(v)) {
			keys.put(graph.opposite(v, e).getElement(), e);
		}
		
		int i = 0;
		for(String n : keys.keySet()) {
			names[i] = n;
			i++;
		}
		
		sorter.sort(names);

		for(String n : names) {
			ordered.addLast(keys.get(n));
		}
		
		return ordered;
	}
	
}
