package me.yourselvs;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	private Calc calc;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }
    
    
    protected void setUp() {
     calc = new Calc();
    }
    
    /**
     * Rigourous Test :-)
     */
    public void testAddPositives()
    {
    	
 
    	
        assertTrue( calc.add(3,4) == 7 );
    }
    public void testAddNegatives()
    {
    
    	assertTrue( calc.add(-3,  -4) == -7);
    }
    public void testDiv()
    {
    	assertTrue (calc.divide(15,5) == 3);
    }
    public void testDivByZero()
    {
    }
}
