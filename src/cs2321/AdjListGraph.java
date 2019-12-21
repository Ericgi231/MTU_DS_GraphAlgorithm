package cs2321;

import net.datastructures.*;

/*
 * Implement Graph interface. A graph can be declared as either directed or undirected.
 * In the case of an undirected graph, methods outgoingEdges and incomingEdges return the same collection,
 * and outDegree and inDegree return the same value.
 * 
 * @author CS2321 Instructor
 */
public class AdjListGraph<V, E> implements Graph<V, E> {

	//vars
	//
	private boolean isDirected;
	private PositionalList<Vertex<V>> vertices = new DoublyLinkedList<>();
	private PositionalList<Edge<E>> edges = new DoublyLinkedList<>();
	
	//inner classes
	//
	private class InnerVertex<V> implements Vertex<V>{
		
		private V element;
		private Position<Vertex<V>> pos;
		private Map<Vertex<V>,Edge<E>> outgoing, incoming;
		
		public InnerVertex(V elem, boolean graphIsDirected) {
			element = elem;
			outgoing = new HashMap<Vertex<V>, Edge<E>>();
			if(graphIsDirected) {
				incoming = new HashMap<Vertex<V>, Edge<E>>();
			} else {
				incoming = outgoing;
			}
		}
		
		@Override
		public V getElement() {
			return element;
		}
		
		public void setPosition(Position<Vertex<V>> p) {
			pos = p;
		}
		
		public Position<Vertex<V>> getPosition(){
			return pos;
		}
		
		public Map<Vertex<V>,Edge<E>> getOutgoing(){
			return outgoing;
		}
		
		public Map<Vertex<V>,Edge<E>> getIncoming(){
			return incoming;
		}
		
	}
	
	private class InnerEdge<E> implements Edge<E>{
		
		private E element;
		private Position<Edge<E>> pos;
		private Vertex<V>[] endpoints;
		
		public InnerEdge(Vertex<V> u, Vertex<V> v, E elem) {
			element = elem;
			endpoints = (Vertex<V>[]) new Vertex[] {u,v};
		}
		
		public E getElement() {
			return element;
		}
		
		public Vertex<V>[] getEndpoints(){
			return endpoints;
		}
		
		public void setPosition(Position<Edge<E>> p ) {
			pos = p;
		}
		
		public Position<Edge<E>> getPosition(){
			return pos;
		}
		
	}
	
	//constructors
	//
	
	//set directed yes no
	public AdjListGraph(boolean directed) {
		isDirected = directed;
	}

	//default to not directed if not specified
	public AdjListGraph() {
		isDirected = false;
	}
	
	//utlity
	//
	private InnerEdge<E> validate(Edge<E> e){
		return (InnerEdge<E>)e;
	}
	
	private InnerVertex<V> validate(Vertex<V> v){
		return (InnerVertex<V>)v;
	}
	
	//functions
	//

	/* (non-Javadoc)
	 * @see net.datastructures.Graph#edges()
	 */
	public Iterable<Edge<E>> edges() {
		//returns all edges, so it has O(m) where m is count of edges
		return edges;
	}

	/* (non-Javadoc)
	 * @see net.datastructures.Graph#endVertices(net.datastructures.Edge)
	 */
	
	public Vertex[] endVertices(Edge<E> e) throws IllegalArgumentException {
		InnerEdge<E> edge = validate(e);
		return edge.getEndpoints();
	}

	/* (non-Javadoc)
	 * @see net.datastructures.Graph#insertEdge(net.datastructures.Vertex, net.datastructures.Vertex, java.lang.Object)
	 */
	
	public Edge<E> insertEdge(Vertex<V> u, Vertex<V> v, E o)
			throws IllegalArgumentException {
		if(getEdge(u,v) == null) {
			InnerEdge<E> e = new InnerEdge<>(u,v,o);
			e.setPosition(edges.addLast(e));
			InnerVertex<V> origin = validate(u);
			InnerVertex<V> dest = validate(v);
			origin.getOutgoing().put(v,e);
			dest.getIncoming().put(u,e);
			return e;
		} else {
			throw new IllegalArgumentException("Edge from u to v exists");
		}
	}

	/* (non-Javadoc)
	 * @see net.datastructures.Graph#insertVertex(java.lang.Object)
	 */
	
	public Vertex<V> insertVertex(V o) {
		InnerVertex<V> v = new InnerVertex<>(o, isDirected);
		v.setPosition(vertices.addLast(v));
		return v;
	}

	/* (non-Javadoc)
	 * @see net.datastructures.Graph#numEdges()
	 */
	
	public int numEdges() {
		return edges.size();
	}

	/* (non-Javadoc)
	 * @see net.datastructures.Graph#numVertices()
	 */
	
	public int numVertices() {
		return vertices.size();
	}

	/* (non-Javadoc)
	 * @see net.datastructures.Graph#opposite(net.datastructures.Vertex, net.datastructures.Edge)
	 */
	
	public Vertex<V> opposite(Vertex<V> v, Edge<E> e)
			throws IllegalArgumentException {
		InnerEdge<E> edge = validate(e);
		Vertex<V>[] endpoints = edge.getEndpoints();
		if(endpoints[0] == v) {
			return endpoints[1];
		} else if (endpoints[1] == v) {
			return endpoints[0];
		} else {
			throw new IllegalArgumentException("v is not incident to this edge");
		}
	}

	/* (non-Javadoc)
	 * @see net.datastructures.Graph#removeEdge(net.datastructures.Edge)
	 */
	
	public void removeEdge(Edge<E> e) throws IllegalArgumentException {
		InnerEdge<E> edge = validate(e);
		Vertex<V>[] verts = edge.getEndpoints();
		validate(verts[0]).getOutgoing().remove(verts[1]);
		validate(verts[1]).getIncoming().remove(verts[0]);
		edges.remove(edge.getPosition());
		edge.setPosition(null);
	}

	/* (non-Javadoc)
	 * @see net.datastructures.Graph#removeVertex(net.datastructures.Vertex)
	 */
	
	public void removeVertex(Vertex<V> v) throws IllegalArgumentException {
		//loops for all degree of outgoing and incoming edges
		InnerVertex<V> vert = validate(v);
		for(Edge<E> e : vert.getOutgoing().values()) {
			removeEdge(e);
		}
		for(Edge<E> e : vert.getIncoming().values()) {
			removeEdge(e);
		}
		vertices.remove(vert.getPosition());
	}

	/* 
     * replace the element in edge object, return the old element
     */
	
	public E replace(Edge<E> e, E o) throws IllegalArgumentException {
		removeEdge(e);
		InnerEdge<E> edge = validate(e);
		insertEdge(edge.getEndpoints()[0], edge.getEndpoints()[1], o);
		return edge.getElement();
	}

    /* 
     * replace the element in vertex object, return the old element
     */
	
	public V replace(Vertex<V> v, V o) throws IllegalArgumentException {
		//calls remove vertex
		removeVertex(v);
		InnerVertex<V> vertex = validate(v);
		insertVertex(o);
		return vertex.getElement();
	}

	/* (non-Javadoc)
	 * @see net.datastructures.Graph#vertices()
	 */
	
	public Iterable<Vertex<V>> vertices() {
		//returns list of all vertices
		return vertices;
	}

	@Override
	
	public int outDegree(Vertex<V> v) throws IllegalArgumentException {
		InnerVertex<V> vert = validate(v);
		return vert.getOutgoing().size();
	}

	@Override
	
	public int inDegree(Vertex<V> v) throws IllegalArgumentException {
		InnerVertex<V> vert = validate(v);
		return vert.getIncoming().size();
	}

	@Override
	
	public Iterable<Edge<E>> outgoingEdges(Vertex<V> v)
			throws IllegalArgumentException {
		//loops for degree of all outgoing edges
		InnerVertex<V> vert = validate(v);
		return vert.getOutgoing().values();
	}

	@Override
	
	public Iterable<Edge<E>> incomingEdges(Vertex<V> v)
			throws IllegalArgumentException {
		//loops for degree of all incoming edges
		InnerVertex<V> vert = validate(v);
		return vert.getIncoming().values();
	}

	@Override
	public Edge<E> getEdge(Vertex<V> u, Vertex<V> v)
			throws IllegalArgumentException {
		//loops through the degree of whichever is smaller, degree of vertex or degree of edges.
		InnerVertex<V> origin = validate(u);
		return origin.getOutgoing().get(v);
	}
	
}
