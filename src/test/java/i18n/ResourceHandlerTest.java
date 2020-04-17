package i18n;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import i18n.ResourceHandler.ResourceHandlerInstance;

public class ResourceHandlerTest {
    
    @Before
    public void setUp() {
        resourceHandler = new ResourceHandlerInstance("i18n_test/MessageBundle");
    }
    
    private ResourceHandlerInstance resourceHandler;
    
    @Test
    public void testChangeLocale() {
        // execute
        resourceHandler.setLocale(Locale.GERMAN);
        // assert
        assertEquals(Locale.GERMAN, resourceHandler.getLocale());
        // execute
        resourceHandler.setLocale(Locale.ENGLISH);
        // assert
        assertEquals(Locale.ENGLISH, resourceHandler.getLocale());
    }

    @Test
    public void testGetMessage() {
        // data
        resourceHandler.setLocale(Locale.ROOT);
        // execute
        String result = resourceHandler.getMessage("test");
        // assert
        assertEquals("Hello Fallback!", result);
    }
    
    @Test
    public void testGetMessageEN() {
        // data
        resourceHandler.setLocale(Locale.ENGLISH);
        // execute
        String result = resourceHandler.getMessage("test");
        // assert
        assertEquals("Hello World!", result);
    }
    
    @Test
    public void testGetMessageDE() {
        // data
        resourceHandler.setLocale(Locale.GERMAN);
        // execute
        String result = resourceHandler.getMessage("test");
        // assert
        assertEquals("Hallo Welt!", result);
    }
    
    @Test
    public void testGetMessageCountryDE() {
        // data
        resourceHandler.setLocale(Locale.GERMANY);
        // execute
        String result = resourceHandler.getMessage("test");
        // assert
        assertEquals("Hallo Welt!", result);
    }

    @Test
    public void testGetMessageFallback() {
        // data
        resourceHandler.setLocale(Locale.GERMAN);
        // execute
        String result = resourceHandler.getMessage("fallback");
        // assert
        assertEquals("This is the fallback", result);
    }

    @Test
    public void testGetMessageArgs() {
        // data
        resourceHandler.setLocale(Locale.ENGLISH);
        // execute
        String result = resourceHandler.getMessage("args", "here");
        // assert
        assertEquals("Insert > here <", result);
    }

    @Test
    public void testGetMessageArgsMultiple() {
        // data
        resourceHandler.setLocale(Locale.ENGLISH);
        // execute
        String result = resourceHandler.getMessage("multiArgs", 1, 2);
        // assert
        assertEquals("1 + 1 = 2", result);
    }

    @Test
    public void testGetMessageWithoutFormat() {
        // data
        resourceHandler.setLocale(Locale.ENGLISH);
        LocalDate date = LocalDate.of(2019, 7, 21);
        // execute
        String result = resourceHandler.getMessage("dateWithoutFormat", date);
        // assert
        assertEquals("On 2019-07-21", result);
    }
    
    @Test
    public void testGetMessageWithFormatLocaleEN() {
        // data
        resourceHandler.setLocale(Locale.ENGLISH);
        LocalDate date = LocalDate.of(2019, 7, 21);
        // execute
        String result = resourceHandler.getMessage("dateWithFormat", date);
        // assert
        assertEquals("On Jul 21, 2019", result);
    }
    
    @Test
    public void testGetMessageWithFormatLocaleDE() {
        // data
        resourceHandler.setLocale(Locale.GERMAN);
        LocalDate date = LocalDate.of(2019, 7, 21);
        // execute
        String result = resourceHandler.getMessage("dateWithFormat", date);
        // assert
        assertEquals("Am 21.07.2019", result);
    }
    
    @Test
    public void testGetMessageWithFormatStyleLocalEN() {
        // data
        resourceHandler.setLocale(Locale.ENGLISH);
        LocalDate date = LocalDate.of(2019, 7, 21);
        // execute
        String result = resourceHandler.getMessage("dateWithLongFormat", date);
        // assert
        assertEquals("On July 21, 2019", result);
    }
    
    @Test
    public void testGetMessageWithFormatStyleLocaleDE() {
        // data
        resourceHandler.setLocale(Locale.GERMAN);
        LocalDate date = LocalDate.of(2019, 7, 21);
        // execute
        String result = resourceHandler.getMessage("dateWithLongFormat", date);
        // assert
        assertEquals("Am 21. Juli 2019", result);
    }
    
    @Test
    public void testGetMessageWithFormatStyleLocaleFallback() {
        // data
        resourceHandler.setLocale(Locale.GERMAN);
        LocalDate date = LocalDate.of(2019, 7, 21);
        // execute
        String result = resourceHandler.getMessage("dateWithLongFormatFallback", date);
        // assert
        assertEquals("Fallback to 21. Juli 2019", result);
    }
    
}
