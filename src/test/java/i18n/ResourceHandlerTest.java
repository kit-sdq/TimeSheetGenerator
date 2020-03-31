package i18n;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Locale;

import org.junit.Test;

public class ResourceHandlerTest {
    
    @Test
    public void testChangeLocale() {
        // execute
        ResourceHandler.setLocale(Locale.GERMAN);
        // assert
        assertEquals(Locale.GERMAN, ResourceHandler.getLocale());
        // execute
        ResourceHandler.setLocale(Locale.ENGLISH);
        // assert
        assertEquals(Locale.ENGLISH, ResourceHandler.getLocale());
    }

    @Test
    public void testGetMessage() {
        // data
        ResourceHandler.setLocale(Locale.ROOT);
        // execute
        String result = ResourceHandler.getMessage("test");
        // assert
        assertEquals("Hello World!", result);
    }
    
    @Test
    public void testGetMessageEN() {
        // data
        ResourceHandler.setLocale(Locale.ENGLISH);
        // execute
        String result = ResourceHandler.getMessage("test");
        // assert
        assertEquals("Hello World!", result);
    }
    
    @Test
    public void testGetMessageDE() {
        // data
        ResourceHandler.setLocale(Locale.GERMAN);
        // execute
        String result = ResourceHandler.getMessage("test");
        // assert
        assertEquals("Hallo Welt!", result);
    }
    
    @Test
    public void testGetMessageCountryDE() {
        // data
        ResourceHandler.setLocale(Locale.GERMANY);
        // execute
        String result = ResourceHandler.getMessage("test");
        // assert
        assertEquals("Hallo Welt!", result);
    }

    @Test
    public void testGetMessageFallback() {
        // data
        ResourceHandler.setLocale(Locale.GERMAN);
        // execute
        String result = ResourceHandler.getMessage("fallback");
        // assert
        assertEquals("This is the fallback", result);
    }

    @Test
    public void testGetMessageArgs() {
        // data
        ResourceHandler.setLocale(Locale.ENGLISH);
        // execute
        String result = ResourceHandler.getMessage("args", "here");
        // assert
        assertEquals("Insert > here <", result);
    }

    @Test
    public void testGetMessageArgsMultiple() {
        // data
        ResourceHandler.setLocale(Locale.ENGLISH);
        // execute
        String result = ResourceHandler.getMessage("multiArgs", 1, 2);
        // assert
        assertEquals("1 + 1 = 2", result);
    }

    @Test
    public void testGetMessageWithoutFormat() {
        // data
        ResourceHandler.setLocale(Locale.ENGLISH);
        LocalDate date = LocalDate.of(2019, 7, 21);
        // execute
        String result = ResourceHandler.getMessage("dateWithoutFormat", date);
        // assert
        assertEquals("On 2019-07-21", result);
    }
    
    @Test
    public void testGetMessageWithFormatLocaleEN() {
        // data
        ResourceHandler.setLocale(Locale.ENGLISH);
        LocalDate date = LocalDate.of(2019, 7, 21);
        // execute
        String result = ResourceHandler.getMessage("dateWithFormat", date);
        // assert
        assertEquals("On Jul 21, 2019", result);
    }
    
    @Test
    public void testGetMessageWithFormatLocaleDE() {
        // data
        ResourceHandler.setLocale(Locale.GERMAN);
        LocalDate date = LocalDate.of(2019, 7, 21);
        // execute
        String result = ResourceHandler.getMessage("dateWithFormat", date);
        // assert
        assertEquals("Am 21.07.2019", result);
    }
    
    @Test
    public void testGetMessageWithFormatStyleLocalEN() {
        // data
        ResourceHandler.setLocale(Locale.ENGLISH);
        LocalDate date = LocalDate.of(2019, 7, 21);
        // execute
        String result = ResourceHandler.getMessage("dateWithLongFormat", date);
        // assert
        assertEquals("On July 21, 2019", result);
    }
    
    @Test
    public void testGetMessageWithFormatStyleLocaleDE() {
        // data
        ResourceHandler.setLocale(Locale.GERMAN);
        LocalDate date = LocalDate.of(2019, 7, 21);
        // execute
        String result = ResourceHandler.getMessage("dateWithLongFormat", date);
        // assert
        assertEquals("Am 21. Juli 2019", result);
    }
    
    @Test
    public void testGetMessageWithFormatStyleLocaleFallback() {
        // data
        ResourceHandler.setLocale(Locale.GERMAN);
        LocalDate date = LocalDate.of(2019, 7, 21);
        // execute
        String result = ResourceHandler.getMessage("dateWithLongFormatFallback", date);
        // assert
        assertEquals("Fallback to 21. Juli 2019", result);
    }
    
}
