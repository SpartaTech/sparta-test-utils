package test.com.github.spartatech.testutils.temporal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.spartatech.testutils.exception.FieldNotFoundException;
import com.github.spartatech.testutils.temporal.DateAssertUtils;

/** 
 * 
 * Unit tests for Date Assert Utils.
 * 
 * @author Daniel Conde Diehl
 * 
 * History: 
 *    Dec 29, 2016 - Daniel Conde Diehl
 *  
 */
public class TestDateAssertUtils extends DateAssertUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestDateAssertUtils.class);
	
    private static final String MESSAGE = "test message";
    
    @Test
    public void testAssertDateAssertNoFieldsSameDate() throws Exception {
        final Calendar cal = Calendar.getInstance();
        final Date expected = cal.getTime();
        final Date actual = cal.getTime();
        
        DateAssertUtils.assertDate(expected, actual);
    }
    
    @Test(expected=AssertionError.class)
    public void testAssertDateAssertNoFieldsDifferentDate() throws Exception {
        final Date expected = new Date();
        Thread.sleep(10);
        final Date actual = new Date();
        
        DateAssertUtils.assertDate(expected, actual);
    }

    @Test(expected=AssertionError.class)
    public void testAssertDateAssertNoFieldsDifferentDateWithMessage() throws Exception {
        final Date expected = new Date();
        Thread.sleep(10);
        final Date actual = new Date();
        try {
            DateAssertUtils.assertDate(MESSAGE, expected, actual);
        } catch (AssertionError e) {
            if (!e.getMessage().equals(MESSAGE)) {
                Assert.fail("Invalid Message");
            }
            throw e;
        }
    }
    
    @Test
    public void testAssertDateAssertFieldsMatching() throws Exception {
        final Calendar cal = Calendar.getInstance();
        final Date expected = cal.getTime();
        int hourDiff = -2;
        if (cal.get(Calendar.HOUR_OF_DAY) < 2) {
        	hourDiff = 2;
        }
    	cal.add(Calendar.HOUR_OF_DAY, hourDiff);
        final Date actual = cal.getTime();

        
        LOGGER.info("Fields Matching Unit Test. Expected:{}, actual: {}", expected, actual);
        DateAssertUtils.assertDate(expected, actual, Calendar.DATE, Calendar.MONTH, Calendar.YEAR);
    }
    
    @Test(expected=ComparisonFailure.class)
    public void testAssertDateAssertFieldsNoMatching() throws Exception {
        final Calendar cal = Calendar.getInstance();
        final Date expected = cal.getTime();
        int hourDiff = -2;
        if (cal.get(Calendar.HOUR_OF_DAY) < 2) {
        	hourDiff = 2;
        }
    	cal.add(Calendar.HOUR_OF_DAY, hourDiff);
        final Date actual = cal.getTime();

        try {
            DateAssertUtils.assertDate(expected, actual, Calendar.DATE, Calendar.MONTH, Calendar.YEAR, Calendar.HOUR_OF_DAY);
        } catch (ComparisonFailure e) {
            if (!e.getMessage().startsWith("Field HOUR_OF_DAY mismatch")) {
                Assert.fail("Invalid Message - " + e.getMessage());
            }
            if (!e.getExpected().equals(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)+(hourDiff *-1)))) {
                Assert.fail("Invalid expected "+ String.valueOf(cal.get(Calendar.HOUR_OF_DAY)+(hourDiff *-1)) + " But was: "+ e.getExpected());
            }
            
            if (!e.getActual().equals(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)))) {
                Assert.fail("Invalid actual "+ String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + " But was: "+ e.getActual());
            }
            
            throw e;
        }
    }
    
    @Test(expected=ComparisonFailure.class)
    public void testAssertDateAssertFieldsNoMatchingWithMessage() throws Exception {
        final Calendar cal = Calendar.getInstance();
        final Date expected = cal.getTime();
        int hourDiff = -2;
        if (cal.get(Calendar.HOUR_OF_DAY) < 2) {
        	hourDiff = 2;
        }
    	cal.add(Calendar.HOUR_OF_DAY, hourDiff);
        final Date actual = cal.getTime();
        
        try {
            DateAssertUtils.assertDate(MESSAGE, expected, actual, Calendar.DATE, Calendar.MONTH, Calendar.YEAR, Calendar.HOUR_OF_DAY);
        } catch (ComparisonFailure e) {
            if (!e.getMessage().startsWith(MESSAGE)) {
                Assert.fail("Invalid Message - " + e.getMessage());
            }
            if (!e.getExpected().equals(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)+(hourDiff *-1)))) {
                Assert.fail("Invalid expected "+ String.valueOf(cal.get(Calendar.HOUR_OF_DAY)+(hourDiff *-1)) + " But was: "+ e.getExpected());
            }
            
            if (!e.getActual().equals(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)))) {
                Assert.fail("Invalid actual "+ String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + " But was: "+ e.getActual());
            }
            
            throw e;
        }
    }
    
    @Test
    public void testAssertDateByFormatMatching() throws Exception {
        final Date expected = new Date();
        final Date actual = new Date();
        final String format = "yyyy-MM-dd";
        DateAssertUtils.assertDateByFormat(expected, actual, format);
    }
    
    @Test(expected=ComparisonFailure.class)
    public void testAssertDateByFormatNotMatching() throws Exception {
        final Calendar cal = Calendar.getInstance();
        final Date expected = cal.getTime();
        cal.add(Calendar.MINUTE, -2);
        final Date actual = cal.getTime();
        final String format = "yyyy-MM-dd HH:mm:ss";
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            DateAssertUtils.assertDateByFormat(expected, actual, format);
        } catch (ComparisonFailure e) {
            if (!e.getMessage().startsWith("Date mismatch")) {
                Assert.fail("Invalid Message - " + e.getMessage());
            }
            if (!e.getExpected().equals(sdf.format(expected))) {
                Assert.fail("Invalid expected "+ sdf.format(expected) + " But was: "+ e.getExpected());
            }
            
            if (!e.getActual().equals(sdf.format(actual))) {
                Assert.fail("Invalid actual "+ sdf.format(actual) + " But was: "+ e.getActual());
            }
            
            throw e;
        }
    }
    
    @Test(expected=ComparisonFailure.class)
    public void testAssertDateByFormatNotMatchingWithMessage() throws Exception {
        final Calendar cal = Calendar.getInstance();
        final Date expected = cal.getTime();
        cal.add(Calendar.MINUTE, -2);
        final Date actual = cal.getTime();
        final String format = "yyyy-MM-dd HH:mm:ss";
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            DateAssertUtils.assertDateByFormat(MESSAGE, expected, actual, format);
        } catch (ComparisonFailure e) {
            if (!e.getMessage().startsWith(MESSAGE)) {
                Assert.fail("Invalid Message - " + e.getMessage());
            }
            if (!e.getExpected().equals(sdf.format(expected))) {
                Assert.fail("Invalid expected "+ sdf.format(expected) + " But was: "+ e.getExpected());
            }
            
            if (!e.getActual().equals(sdf.format(actual))) {
                Assert.fail("Invalid actual "+ sdf.format(actual) + " But was: "+ e.getActual());
            }
            
            throw e;
        }
    }
    
    @Test(expected=FieldNotFoundException.class)
    public void testAssertDateInvalidField() throws FieldNotFoundException {
        final Calendar cal = Calendar.getInstance();
        final Date expected = cal.getTime();
        final Date actual = cal.getTime();
        
        DateAssertUtils.assertDate(expected, actual, 666);        
    }
    
}
