package cs2321;

import net.datastructures.Vertex;
import net.datastructures.Graph;
import net.datastructures.Position;
import net.datastructures.Vertex;
import net.datastructures.Edge;

/**
 * @author Ruihong Zhang
 * Reference: Textbook R-14.27 on page 679
 *
 */
public class Islands  {

	public Graph<Integer, Integer> graph;
	public HashMap<Integer, Vertex<Integer>> vertexMap = new HashMap<>();
	public HeapPQ<Integer, Edge<Integer>> edges = new HeapPQ<>();
	
	public DoublyLinkedList<Edge<Integer>> usedEdges = new DoublyLinkedList<>();
	public Partition<Vertex<Integer>> clouds = new Partition<>();
	public HashMap<Vertex<Integer>, Position<Vertex<Integer>>> cloudKeys = new HashMap<>();
	
	public class Partition<E> {
		private class Locator<E> implements Position<E>{
			public E element;
			public int size;
			public Locator<E> parent;
			public Locator(E e) {
				element = e;
				size = 1;
				parent = this;
			}
			public E getElement() {
				return element;
			}
		}
		
		public Position<E> makeCluster(E e){
			return new Locator<E>(e);
		}
		
		public Position<E> find(Position<E> p){
			Locator<E> loc = (Locator<E>)p;
			if(loc.parent != loc) {
				loc.parent = (Locator<E>)find(loc.parent);
			}
			return loc.parent;
		}
		
		public void union(Position<E> p, Position<E> q) {
			Locator<E> a = (Locator<E>)p;
			Locator<E> b = (Locator<E>)q;
			if(a != b) {
				if(a.size > b.size) {
					b.parent = a;
					a.size += b.size;
				} else {
					a.parent = b;
					b.size += a.size;
				}
			}
		}
	}
	
	/**
	 * @param numOfIslands: total number of islands. It will be numbered as 0,1,2,...
	 * @param distance: distance[i][j] represents the distance between island[i] and island[j]. 
	 * 					-1 means there is no edge between island[i] and island[j]. 
	 */
	public Islands(int numOfIslands, int distance[][]) {
		graph = new AdjListGraph<>();
		
		//add vertexes to map and graph
		for(int n = 0; n < numOfIslands; n++) {
			vertexMap.put(n, graph.insertVertex(n));
		}
		
		//add edges to graph
		for(int n = 0; n < numOfIslands; n++) {
			graph.insertVertex(n);
			for(int i = 0; i < numOfIslands; i++) {
				if(distance[n][i] != -1) {
					edges.insert(distance[n][i], graph.insertEdge(vertexMap.get(i), vertexMap.get(n), distance[n][i]));
				}
			}
		}
		
		//create clouds
		for(Vertex<Integer> n : vertexMap.values()) {
			cloudKeys.put(n, clouds.makeCluster(n));
		}
	}


	/**
	 * @return the cost of minimum spanning tree using Kruskal's algorithm. 
	 */
	public int Kruskal() {
		Edge<Integer> edge;
		Vertex<Integer>[] sides;
		
		//while un-checked edges exist
		while(!edges.isEmpty()) {
			//get smallest edge
			edge = edges.removeMin().getValue();
			
			//get vertex on both side of edge
			sides = graph.endVertices(edge);
			
			//if each side is in a different cloud, merge clouds, add edge to used edges
			//else do nothing and move on
			Position<Vertex<Integer>> a = clouds.find(cloudKeys.get(sides[0]));
			Position<Vertex<Integer>> b = clouds.find(cloudKeys.get(sides[1]));
			if(a != b) {
				usedEdges.addLast(edge);
				clouds.union(a, b);
			}
		}
		
		int mst = 0;
		for(Edge<Integer> n : usedEdges) {
			mst += n.getElement();
		}
		return mst;
	}
}
