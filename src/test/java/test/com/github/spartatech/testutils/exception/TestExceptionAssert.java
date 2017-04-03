/**
 * Sparta Software Co.
 * 2017
 */
package test.com.github.spartatech.testutils.exception;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import com.github.spartatech.testutils.exception.ExceptionAssert;



/**
 * Unit Tests for Exception Assert
 * 
 * @author Daniel Conde Diehl - Sparta Technology
 *
 * History:
 *  Apr 3, 2017 - Daniel Conde Diehl
 */

public class TestExceptionAssert extends ExceptionAssert {

	@Test
	public void testAssertExceptionMessageNoException () throws Exception {
		try {
			ExceptionAssert.assertExceptionMessage(new Exception("Not gonna happen"), false, 
										() -> {System.out.println("Test");});
			fail("No exception happened");
		} catch (AssertionError e) {
			Assert.assertEquals("Expected Exception: " + Exception.class , e.getMessage());
		}
	}
	

	@Test
	public void testAssertExceptionMessageDifferentException () throws Exception {
		try {
			ExceptionAssert.assertExceptionMessage(new NullPointerException(), false, 
										() -> {throw new Exception();});
			fail("No exception happened");
		} catch (AssertionError e) {
			Assert.assertEquals("expected:<class java.lang.NullPointerException> but was:<class java.lang.Exception>", e.getMessage());
		}
	}
	
	@Test
	public void testAssertExceptionMessageDifferentMessage () throws Exception {
		try {
			ExceptionAssert.assertExceptionMessage(new Exception("Message 1"), false, 
										() -> {throw new Exception("Message 2");});
			fail("No exception happened");
		} catch (AssertionError e) {
			Assert.assertEquals("expected:<Message [1]> but was:<Message [2]>", e.getMessage());
		}
	}
	
	@Test
	public void testAssertExceptionMessageSameMessageDontThrow () throws Exception {
		ExceptionAssert.assertExceptionMessage(new Exception("Message 1"), false, 
									() -> {throw new Exception("Message 1");});
	}
	
	@Test(expected=NumberFormatException.class)
	public void testAssertExceptionMessageSameMessageThrow () throws Exception {
		ExceptionAssert.assertExceptionMessage(new NumberFormatException(), true, 
									() -> {throw new NumberFormatException();});
	}
	
}
