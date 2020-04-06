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
    
    private static final String DEFAULT_MESSAGE_BUNDLE_PATH = "i18n/MessageBundle";
    
    protected static class ResourceHandlerInstance {
        
        protected ResourceHandlerInstance(String messageBundlePath) {
            this.messageBundlePath = messageBundlePath;
            
            locale = Locale.getDefault();
            
            loadResourceBundle();
        }
        
        private final String messageBundlePath;

        private Locale locale;
        private ResourceBundle resourceBundle;
        
        protected Locale getLocale() {
            return locale;
        }
        
        protected void setLocale(final Locale locale) {
            this.locale = locale;
            
            loadResourceBundle();
        }
        
        protected String getMessage(final String key, final Object... args) {
            String message = resourceBundle.getString(key);
            
            MessageFormat format = new MessageFormat("");
            format.setLocale(locale);
            format.applyPattern(message);
            replaceUnsupportedFormats(format);
            
            return format.format(args);
        }
        
        private void loadResourceBundle() {
            resourceBundle = ResourceBundle.getBundle(messageBundlePath, locale);
        }
        
        private static void replaceUnsupportedFormats(MessageFormat format) {
            Format[] subformats = format.getFormats();
            
            for (int i = 0; i < subformats.length; i++) {
                if (subformats[i] instanceof DateFormat) {
                    format.setFormat(i, new DateFormatWrapper((DateFormat) subformats[i]));
                }
            }
        }
        
    }
    
    private static ResourceHandlerInstance instance = new ResourceHandlerInstance(DEFAULT_MESSAGE_BUNDLE_PATH);
    
    /**
     * Get the currently used locale.
     * 
     * @return Currently used locale
     */
    public static Locale getLocale() {
        return instance.getLocale();
    }
    
    /**
     * Set the used locale.
     * 
     * The set locale will be the first choice for the i18n message bundle loaded.
     * 
     * @param locale Locale to use for the loading of i18n message bundles
     */
    public static void setLocale(final Locale locale) {
        instance.setLocale(locale);
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
        return instance.getMessage(key, args);
    }
    
}
