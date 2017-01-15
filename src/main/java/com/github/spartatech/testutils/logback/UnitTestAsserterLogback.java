package com.github.spartatech.testutils.logback;

import java.util.LinkedList;

import org.junit.ComparisonFailure;
import org.slf4j.LoggerFactory;

import com.github.spartatech.testutils.logback.constant.ExpectValue;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;


/** 
 * 
 * Logback utility to spy logs calls
 * The way it works is:
 * - Instantiate a new {@link UnitTestAsserterLogback} giving the logger to be spied.
 * - Declare all your expectations using addExpectation
 * - call method to be tested
 * - call {@code UnitTestAsserterLogback.assertLogExpectations()}
 * 
 * @author Daniel Conde Diehl
 * 
 * History: 
 *    Dec 27, 2016 - Daniel Conde Diehl
 *  
 */
public class UnitTestAsserterLogback  {

    private LinkedList<LogEntryItem> expectations = new LinkedList<>();
    private LinkedList<ILoggingEvent> events = new LinkedList<>();
    
    private UnitTestAsserterLogbackAppender appender;
    
    
    /**
     * Constructor receiving the logger as a String.
     * @param logger name as a String
     */
    public UnitTestAsserterLogback(String logger) {
        appender = new UnitTestAsserterLogbackAppender(logger, events);
        attachAppenderToLogback();
    }
    
    /**
     * Constructor receiving the logger as a class.
     * @param clazz Class that will be used as a logger name
     */
    public UnitTestAsserterLogback(Class<?> clazz) {
        appender = new UnitTestAsserterLogbackAppender(clazz, events);
        attachAppenderToLogback();
    }
    
    /**
     * Adds a new expectation to the logger. 
     * 
     * @param level expected for the log entry
     * @param logMessage message expected for the log entry
     * @param params list of parameters for the log entry.
     */
    public void addExpectation(Level level, String logMessage, Object...params) {
        expectations.add(new LogEntryItem(level, logMessage, params));
    }
    
    
    
    /**
     * Replay expectations to check if all logs happened.
     * Analyzes in order and all logs supposed to be there 
     * 
     * @throws AssertionError Throws an assertion error when the asserts fail
     */
    public void assertLogExpectations() throws AssertionError {
        if (events.size() != expectations.size()) {
            throw new ComparisonFailure("Invalid number of messages", String.valueOf(expectations.size()), String.valueOf(events.size()));
        }
        
        for (ILoggingEvent event : events) {

            LogEntryItem entry = expectations.remove();

            if (!entry.getMessage().equals(event.getMessage())) {
                throw new ComparisonFailure("Message mismatch", entry.getMessage(), event.getMessage());
            }
            
            if (entry.getLevel() != event.getLevel()) {
                throw new ComparisonFailure("LogLevel mismatch", entry.getLevel().toString(), event.getLevel().toString());
            }
            
            int expectedSize = entry.getParams() == null ? 0 : entry.getParams().length;
            int actualSize = event.getArgumentArray() == null ? 0 : event.getArgumentArray().length;
            if (expectedSize != actualSize) {
                throw new ComparisonFailure("Incorrect number of params", 
                                            entry.getParams()== null ? "0" : String.valueOf(entry.getParams().length) , 
                                            event.getArgumentArray() == null ? "0" : String.valueOf(event.getArgumentArray().length));
            }

            for (int i = 0; i < entry.getParams().length; i++) {
                Object expectedParam = entry.getParams()[i];
                Object actualParam = event.getArgumentArray()[i];

                if (ExpectValue.ANY == expectedParam) {
                    continue;
                }
                
                if (expectedParam == null && actualParam == null) {
                    continue;
                } else if (expectedParam == null && actualParam != null) {
                    throw new ComparisonFailure("Param [" + i + "] mismatch", "null", actualParam.toString());
                } else if (!expectedParam.equals(actualParam)) {
                    throw new ComparisonFailure("Param [" + i + "] mismatch", expectedParam == null ? "NULL" : expectedParam.toString(), actualParam == null ? "NULL" : actualParam.toString());
                }
            }
        }
    }    
    
    /**
     * Attached the log to the logback. 
     */
    private void attachAppenderToLogback() {
        Logger root = (Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.addAppender(appender);
    }
    
    /** 
     * 
     * Internal VO to carry log entries for asserting values.
     * 
     * @author Daniel Conde Diehl 
     * 
     * History: 
     *    Jan 15, 2017 - Daniel Conde Diehl
     *  
     */ 
    class LogEntryItem {
        private Level level;
        private String message;
        private Object[] params;
        
        /**
         * Constructor with all values.
         * 
         * @param level log loevel for the message
         * @param message text message
         * @param params params used in the log
         */
        public LogEntryItem(Level level, String message, Object[] params) {
            this.level = level;
            this.message = message;
            this.params = params;
        }
        
        /**
         * @return the level
         */
        public Level getLevel() {
            return level;
        }

        /**
         * @param level the level to set
         */
        public void setLevel(Level level) {
            this.level = level;
        }

        /**
         * @return the message
         */
        public String getMessage() {
            return message;
        }
        /**
         * @param message the message to set
         */
        public void setMessage(String message) {
            this.message = message;
        }
        /**
         * @return the params
         */
        public Object[] getParams() {
            return params;
        }
        /**
         * @param params the params to set
         */
        public void setParams(Object[] params) {
            this.params = params;
        }
    }
};
