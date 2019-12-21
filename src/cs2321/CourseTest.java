package cs2321;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CourseTest {
	Course T;
	@Before
	public void setUp() throws Exception {
		String[][]   list= {
           {"LA15"},
           {"LA16","LA15"},
           {"LA22"},
           {"LA31","LA15"},
           {"LA32","LA16","LA31"},
           {"LA126", "LA22", "LA32"}, 
           {"LA127","LA16"},
           {"LA141", "LA22", "LA16"}, 
           {"LA169","LA32"}
		};
		
		T=new Course(list);
	}
	
	@Test
	public void testWhichSemester1() {
		assertEquals(1, T.whichSemester("LA15"));
	}
	
	@Test
	public void testWhichSemester2() {
		assertEquals(2, T.whichSemester("LA16"));
	}

	@Test
	public void testWhichSemester3() {
		assertEquals(3, T.whichSemester("LA32"));
	}
	
	@Test
	public void testWhichSemester4() {
		assertEquals(4,T.whichSemester("LA126"));
	}
}
