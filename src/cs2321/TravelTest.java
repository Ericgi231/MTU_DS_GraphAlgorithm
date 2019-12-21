package cs2321;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TravelTest {
	Travel T;
	@Before
	public void setUp() throws Exception {
		/*
		 *    A------1-----
		 *    |             \
 		 *    8              \ 
		 *    |               \
		 *    B --11-- C --1-- D
		 */
		String routes[][] = {  {"A","B","8"},
								 {"A","D","1"},
								 {"B","C","11"},
								 {"C","D","1"}};
		
		T = new Travel(routes);
	}


	
	@Test
	public void testDFSRoute1() {
		String[] expected= {"A","B", "C"};
		java.util.ArrayList<String> list = new java.util.ArrayList<String>();
		
		for (String s: T.DFSRoute("A", "C")) {
			list.add(s);
		};
		
		assertArrayEquals(expected, list.toArray() );
	}

	@Test
	public void testDFSRoute2() {
		String[] expected= {"C","B","A","D"};
		java.util.ArrayList<String> list = new java.util.ArrayList<String>();
		
		for (String s: T.DFSRoute("C", "D")) {
			list.add(s);
		};
		
		assertArrayEquals(expected, list.toArray() );
	}
	
	@Test
	public void testBFSRoute1() {
		String[] expected= {"A","B","C"};
		java.util.ArrayList<String> list = new java.util.ArrayList<String>();
		
		for (String s: T.BFSRoute("A", "C")) {
			list.add(s);
		};
		
		assertArrayEquals(expected, list.toArray() );
	}
	
	@Test
	public void testBFSRoute2() {
		String[] expected= {"A","B"};
		java.util.ArrayList<String> list = new java.util.ArrayList<String>();
		
		for (String s: T.BFSRoute("A", "B")) {
			list.add(s);
		};
		
		assertArrayEquals(expected, list.toArray() );
	}
	

	@Test
	public void testBFSRoute3() {
		String[] expected= {"C","D"};
		java.util.ArrayList<String> list = new java.util.ArrayList<String>();
		
		for (String s: T.BFSRoute("C", "D")) {
			list.add(s);
		};
		
		assertArrayEquals(expected, list.toArray() );
	}


	
	@Test
	public void testDijkstraRoute() {
		String[] expected= {"A","D","C"};
		java.util.ArrayList<String> list = new java.util.ArrayList<String>();
		DoublyLinkedList<String> path = new DoublyLinkedList<String>();
		int distance = T.DijkstraRoute("A", "C", path);
		
		assertEquals(2,distance);
		for (String s: path) {
			list.add(s);
		}
		
		assertArrayEquals(expected, list.toArray() );
	}
	
	@Test
	public void testDijkstraRoute1() {
		String[] expected= {"B","A","D","C"};
		java.util.ArrayList<String> list = new java.util.ArrayList<String>();
		DoublyLinkedList<String> path = new DoublyLinkedList<String>();
		int distance = T.DijkstraRoute("B", "C", path);
		
		assertEquals(10,distance);
		for (String s: path) {
			list.add(s);
		}
		
		assertArrayEquals(expected, list.toArray() );
	}

}
