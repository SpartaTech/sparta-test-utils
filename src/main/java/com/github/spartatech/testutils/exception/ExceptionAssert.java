/**
 * Sparta Software Co.
 * 2017
 */
package com.github.spartatech.testutils.exception;

import static org.junit.Assert.fail;

import org.junit.Assert;

/**
 * Assertions for Exceptions.
 * 
 * @author Daniel Conde Diehl - Sparta Technology
 *
 * History:
 *  Apr 3, 2017 - Daniel Conde Diehl
 */

public abstract class ExceptionAssert {

	/**
	 * Asserts that the method executed throw the expected exception with the same message.
	 * 
	 * @param expected Expected exception with the message
	 * @param throwActualException whether we should stop the chain and swallow actual exception or throw after processing
	 * @param processable the execution to be checked 
	 * @throws Exception in case it was selected to throw actual exception
	 */
	public static void assertExceptionMessage (Throwable expected, boolean throwActualException, Processable processable) throws Exception {
		try {
			processable.process();
			fail("Expected Exception: " + expected.getClass());
		} catch (Exception actual) {
			Assert.assertEquals(expected.getClass(), actual.getClass());
			Assert.assertEquals(expected.getMessage(), actual.getMessage());
			if (throwActualException) {
				throw actual;
			}
		}
	}
	
}
