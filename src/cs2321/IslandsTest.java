package cs2321;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class IslandsTest {
	Islands T;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testIslandsSimple() {
		/*
		 *         A--1--B    
		 *         |     /
		 *         2    4
		 *         |  /
		 *         C   
		 * 
		 * 
		 */
		int[][] distance = { {-1,1,2},
				             {-1,-1,4},
	                         {-1,-1,-1}
	           };
		T = new Islands(3, distance);
		assertEquals(3,T.Kruskal());
	}

	@Test
	public void testIslandsMedium() {
		// SEE THE GRAPH IN https://en.wikipedia.org/wiki/Minimum_spanning_tree
		
		                     //A   B   C   D   E   F 
		int[][] distance = { {-1,  1,  -1,  3, -1, -1},
							 {-1, -1,   6,  5,  1, -1},
							 {-1, -1,  -1, -1,  5,  2},
							 {-1, -1,  -1, -1,  1, -1},
							 {-1, -1,  -1, -1, -1,  4},
							 {-1, -1,  -1, -1, -1, -1},
				};
		T = new Islands(6, distance);
		assertEquals(9,T.Kruskal());
	}
	
	@Test
	public void testIslandsTextBook() {
		//See textbook question R14.17
		
		                     //1    2     3   4     5   6    7    8
		int[][] distance = { {-1,  240, 210, 340, 280, 200, 345, 120},
							 {-1,   -1, 265, 175, 215, 180, 185, 155},
							 {-1,   -1,  -1, 260, 115, 350, 435, 195},
							 {-1,   -1,  -1,  -1, 160, 330, 295, 230},
							 {-1,   -1,  -1,  -1,  -1, 360, 400, 170},
							 {-1,   -1,  -1,  -1,  -1,  -1, 175, 205},
							 {-1,   -1,  -1,  -1,  -1,  -1,  -1, 305},
							 {-1,   -1,  -1,  -1,  -1,  -1,  -1,  -1}
				};
		T = new Islands(8, distance);
		assertEquals(1075,T.Kruskal());
	}

}
