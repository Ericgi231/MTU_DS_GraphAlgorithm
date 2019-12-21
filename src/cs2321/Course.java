package cs2321;

import net.datastructures.Edge;
import net.datastructures.Graph;
import net.datastructures.Vertex;

/**
 * @author Ruihong Zhang
 * Reference: Text book: R14.17 on page 678
 *
 */
public class Course {

	public Graph<String, String> graph;
	public HashMap<String, Vertex<String>> vertexMap = new HashMap<>();
	
	/**
	 * @param courses: An array of course information. Each element in the array is also array:
	 * 				starts with the course name, followed by a list (0 or more) of prerequisite course names.
	 * 
	 */
	public Course(String courses[][]) {
		graph = new AdjListGraph<>(true);
		
		//add each unique course to the graph and store the vertices in a map for later use
		for(String[] i : courses) {
			vertexMap.put(i[0], graph.insertVertex(i[0]));
		}
		
		//add each unique edge to the graph
		for(String[] i : courses) {
			for(int n = 1; n < i.length; n++) {
				graph.insertEdge(vertexMap.get(i[n]), vertexMap.get(i[0]), "");
			}
		}
	}
	
	/**
	 * @param course
	 * @return find the earliest semester that the given course could be taken by a students after taking all the prerequisites. 
	 */
	//DFS
	//recursively check all required pre-reqs
	//travel up one path of pre-reqs until a class with no pre-reqs is reached
	//travel down the same path of pre-reqs until a class with more un-traveled pre-reqs
	//repeat while tracking along the way the largest path of pre-reqs
	public int whichSemester(String course) {
		
		int maxValue = 0;
		int value = 0;
		
		//base case if vertex has no incoming edges, return 1
		//implying that it would take 1 semester to take a course with no pre-reqs
		if(graph.inDegree(vertexMap.get(course)) == 0) {
			return 1;
		}
		
		//find out how many pre-reqs come before each of this classes pre-req
		for(Edge<String> i : graph.incomingEdges(vertexMap.get(course))) {
			value = whichSemester(graph.endVertices(i)[0].getElement().toString());
			if(value > maxValue) {
				maxValue = value;
			}
		}
		
		//return the largest amount of pre-reqs required before this class + 1
		return maxValue + 1;
	}
			
}
