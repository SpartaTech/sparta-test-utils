package test.com.github.spartatech.testutils.logback;

import org.junit.ComparisonFailure;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.spartatech.testutils.logback.UnitTestAsserterLogback;
import com.github.spartatech.testutils.logback.constant.ExpectValue;

import ch.qos.logback.classic.Level;

/** 
 * 
 * Unit tests for The Logback asserter.
 * 
 * @author Daniel Conde Diehl
 * 
 * History: 
 *    Dec 29, 2016 - Daniel Conde Diehl
 *  
 */
public class TestUnitTestAsserterLogback {
    
    public static final Logger LOGGER = LoggerFactory.getLogger(TestUnitTestAsserterLogback.class);
    
    @Test
    public void testLogByClass() {
        final String message = "teste message";
        
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message);
        
        LOGGER.info(message);
        
        spyAppender.assertLogExpectations();
    }
    
    @Test(expected=ComparisonFailure.class)
    public void testLogByClassLevelMismatch() {
        final String message = "teste message";
        
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.DEBUG, message);
        
        LOGGER.info(message);
        
        spyAppender.assertLogExpectations();
    }
    
    @Test(expected=ComparisonFailure.class)
    public void testLogByClassNoMessages() {
        final String message = "teste message";
        
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message);
        
        spyAppender.assertLogExpectations();
        
    }
    
    @Test(expected=ComparisonFailure.class)
    public void testLogByClassExtraMessages() {
        final String message = "teste message";
        
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message);
        
        LOGGER.info(message);
        LOGGER.info("other message");
        
        spyAppender.assertLogExpectations();
        
    }
    
    @Test(expected=ComparisonFailure.class)
    public void testLogByClassDifferentMessage() {
        final String message = "teste message";
        
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message);
        
        LOGGER.info("other message");
        
        spyAppender.assertLogExpectations();
        
    }
    
    @Test(expected=ComparisonFailure.class)
    public void testLogByClassNotInOrderMessages() {
        final String message = "teste message";
        
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, "other message");
        spyAppender.addExpectation(Level.INFO, message);
        
        LOGGER.info(message);
        LOGGER.info("other message");
        
        spyAppender.assertLogExpectations();
    }
    
    @Test
    public void testLogByClassInOrderMessages() {
        final String message = "teste message";
        
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message);
        spyAppender.addExpectation(Level.INFO, "other message");
        
        LOGGER.info(message);
        LOGGER.info("other message");
        
        spyAppender.assertLogExpectations();
    }
    
    @Test
    public void testLogByNameMessages() {
        final String message = "teste message";
        final String loggerName = "log-mock";
        
        
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(loggerName);
        spyAppender.addExpectation(Level.INFO, message);
        spyAppender.addExpectation(Level.INFO, "other message");
        
        final Logger logger = LoggerFactory.getLogger(loggerName);
        
        logger.info(message);
        logger.info("other message");
        
        spyAppender.assertLogExpectations();
    }
    
    @Test
    public void testLogByClassWithCorrectParameters() {
        final String message = "new message {}, {}";
        final int param1 = 1;
        final String param2 = "New Param";
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message, param1, param2);
        
        LOGGER.info(message, param1, param2);
        
        spyAppender.assertLogExpectations();
    }
    
    @Test(expected=ComparisonFailure.class)
    public void testLogByClassWithIncorrectLessParameters() {
        final String message = "new message {}, {}";
        final int param1 = 1;
        final String param2 = "New Param";
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message, param1, param2);
        
        LOGGER.info(message, param1);
        
        spyAppender.assertLogExpectations();
    }
    
    @Test(expected=ComparisonFailure.class)
    public void testLogByClassWithIncorrectMoreParameters() {
        final String message = "new message {}, {}";
        final int param1 = 1;
        final String param2 = "New Param";
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message, param1, param2);
        
        LOGGER.info(message, param1, param2, "bla");
        
        spyAppender.assertLogExpectations();
    }
    
    @Test(expected=ComparisonFailure.class)
    public void testLogByClassWithParametersWrongOrder() {
        final String message = "new message {}, {}";
        final int param1 = 1;
        final String param2 = "New Param";
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message, param1, param2);
        
        LOGGER.info(message, param2, param1);
        
        spyAppender.assertLogExpectations();
    }
    
    @Test
    public void testLogByClassWithParametersAny() {
        final String message = "new message {}, {}";
        final int param1 = 1;
        final String param2 = "New Param";
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message, param1, ExpectValue.ANY);
        
        LOGGER.info(message, param1, param2);
        
        spyAppender.assertLogExpectations();
    }
    
    @Test
    public void testLogByClassWithParametersNull() {
        final String message = "new message {}, {}";
        final int param1 = 1;
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message, param1, null);
        
        LOGGER.info(message, param1, null);
        
        spyAppender.assertLogExpectations();
    }
    
    @Test(expected=ComparisonFailure.class)
    public void testLogByClassWithParametersNullIncorrect() {
        final String message = "new message {}, {}";
        final int param1 = 1;
        final String param2 = "New Param";
        final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
        spyAppender.addExpectation(Level.INFO, message, param1, param2);
        
        LOGGER.info(message, param1, null);
        
        spyAppender.assertLogExpectations();
    }
    
}
