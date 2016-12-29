package com.github.spartatech.testutils.logback;

import java.util.LinkedList;
import java.util.List;

import org.junit.ComparisonFailure;
import org.slf4j.LoggerFactory;

import com.github.spartatech.testutils.logback.constant.ExpectValue;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.Status;


/** 
 * 
 * Logback Appender to inspect lines logged.
 * The way it works is:
 * - Instantiate a new {@link UnitTestAsserterLogbackAppender} giving the logger to be spied.
 * - Declare all your expectations using addExpectation
 * - call method to be tested
 * - call {@code UnitTestAsserterLogbackAppender.assertLogExpectations()}
 * 
 * @author Daniel Conde Diehl
 * 
 * History: 
 *    Dec 27, 2016 - Daniel Conde Diehl
 *  
 */
public class UnitTestAsserterLogbackAppender implements Appender<ILoggingEvent> {

    private LinkedList<LogEntryItem> expectations = new LinkedList<>();
    private LinkedList<ILoggingEvent> events = new LinkedList<>();
    
    private String logger;
    
    
    /**
     * Constructor receiving the logger as a String.
     * @param logger name as a String
     */
    public UnitTestAsserterLogbackAppender(String logger) {
        this.logger = logger;
        attachAppenderToLogback();
    }
    
    /**
     * Constructor receiving the logger as a class.
     * @param clazz Class that will be used as a logger name
     */
    public UnitTestAsserterLogbackAppender(Class<?> clazz) {
        this.logger = clazz.getName();
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
     * @throws AssertionError
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
    
    @Override
    public void doAppend(ILoggingEvent event) throws LogbackException {
        if (!event.getLoggerName().equals(logger)) return;
        events.add(event);
    }
    
    /**
     * Attached the log to the logback. 
     */
    private void attachAppenderToLogback() {
        Logger root = (Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.addAppender(this);
    }
    
    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public void setContext(Context context) {
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void addStatus(Status status) {
    }

    @Override
    public void addInfo(String msg) {
    }

    @Override
    public void addInfo(String msg, Throwable ex) {
    }

    @Override
    public void addWarn(String msg) {
    }

    @Override
    public void addWarn(String msg, Throwable ex) {
    }

    @Override
    public void addError(String msg) {
    }

    @Override
    public void addError(String msg, Throwable ex) {
    }

    @Override
    public void addFilter(Filter<ILoggingEvent> newFilter) {
    }

    @Override
    public void clearAllFilters() {
    }

    @Override
    public List<Filter<ILoggingEvent>> getCopyOfAttachedFiltersList() {
        return null;
    }

    @Override
    public FilterReply getFilterChainDecision(ILoggingEvent event) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {
    }
    
    class LogEntryItem {
        private Level level;
        private String message;
        private Object[] params;
        
        
        /**
         * @param level
         * @param message
         * @param params
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
