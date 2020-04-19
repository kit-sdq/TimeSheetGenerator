package i18n;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class DateFormatWrapperTest {

    @Test
    public void testPassthrough() {
        // data
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, Calendar.JULY, 11, 9, 30, 17);
        Date date = calendar.getTime();
        
        DateFormatWrapper wrapper = new DateFormatWrapper(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
        // execute
        String result = wrapper.format(date);
        // assert
        assertEquals("2019/07/11 09:30:17", result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDateFormatException() {
        // data
        LocalDateTime date = LocalDateTime.of(2019, 7, 11, 9, 30, 17);
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // execute
        format.format(date);
    }
    
    @Test
    public void testMessageFormatUsesDateFormat() {
        // execute
        MessageFormat messageFormat = new MessageFormat("{0, date}");
        // assert
        assertTrue(messageFormat.getFormats()[0] instanceof DateFormat);
    }
    
    @Test
    public void testLocalDateTime() {
        // data
        LocalDateTime date = LocalDateTime.of(2019, 7, 11, 9, 30, 17);
        DateFormatWrapper wrapper = new DateFormatWrapper(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
        // execute
        String result = wrapper.format(date);
        // assert
        assertEquals("2019/07/11 09:30:17", result);
    }
    
    @Test
    public void testLocalDate() {
        // data
        LocalDate date = LocalDate.of(2019, 7, 11);
        DateFormatWrapper wrapper = new DateFormatWrapper(new SimpleDateFormat("yyyy/MM/dd"));
        // execute
        String result = wrapper.format(date);
        // assert
        assertEquals("2019/07/11", result);
    }
    
    @Test
    public void testLocalTime() {
        // data
        LocalTime date = LocalTime.of(9, 30, 17);
        DateFormatWrapper wrapper = new DateFormatWrapper(new SimpleDateFormat("HH:mm:ss"));
        // execute
        String result = wrapper.format(date);
        // assert
        assertEquals("09:30:17", result);
    }
    
}
