package i18n;

import java.text.DateFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Static class providing localized messages from the i18n message bundles.
 */
public class ResourceHandler {
    
    /**
     * Path of the message bundles files in the resources without the localization postfix.
     */
    private static final String MESSAGE_BUNDLE_PATH = "i18n/MessageBundle";

    private static Locale locale;
    private static MessageFormat format;
    
    private static ResourceBundle resourceBundle;
    
    /**
     * Static initializer for all non-localized resources.
     * 
     * Calls <code>initialize</code> to initialize localized resources as well.
     */
    static {
        locale = Locale.getDefault();
        format = new MessageFormat("");
        
        initialize();
    }
    
    /**
     * Initiliazer for all localized resources.
     * 
     * May be called multiple times (whenever the <code>locale</code> object is changed).
     */
    private static void initialize() {
        format.setLocale(locale);
        
        resourceBundle = ResourceBundle.getBundle(MESSAGE_BUNDLE_PATH, locale);
    }
    
    /**
     * Replace the recognized formats in the format string
     * that are not or not entirely supported by the <code>MessageFormat</code> class.
     */
    private static void replaceUnsupportedFormats(MessageFormat format) {
        Format[] subformats = format.getFormats();
        
        for (int i = 0; i < subformats.length; i++) {
            if (subformats[i] instanceof DateFormat) {
                format.setFormat(i, new DateFormatWrapper((DateFormat) subformats[i]));
            }
        }
    }
    
    /**
     * Get the currently used locale.
     * 
     * @return Currently used locale
     */
    public static Locale getLocale() {
        return locale;
    }
    
    /**
     * Set the used locale.
     * 
     * The set locale will be the first choice for the i18n message bundle loaded.
     * 
     * @param locale Locale to use for the loading of i18n message bundles
     */
    public static void setLocale(final Locale locale) {
        ResourceHandler.locale = locale;
        
        initialize();
    }
    
    /**
     * Get a message string from the i18n message bundles.
     * 
     * The key will be searched in the i18n message bundle specified by the currently used locale
     * and all parent message bundles ("fallback").
     * 
     * The objects provided in <code>args</code> will be inserted in the loaded message string
     * with the format specified in the message string.
     * 
     * @param key Message key
     * @param args Objects to insert in the loaded message string
     * 
     * @return Loaded message string containing a string representation of the provided objects
     */
    public static String getMessage(final String key, final Object... args) {
        String message = resourceBundle.getString(key);
        
        format.applyPattern(message);
        replaceUnsupportedFormats(format);
        
        return format.format(args);
    }
    
}
