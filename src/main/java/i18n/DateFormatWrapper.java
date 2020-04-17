package i18n;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Wrapper for the <code>DateFormat</code> class to add support for LocalDateTime, LocalDate and LocalTime.
 */
class DateFormatWrapper extends Format {

    private static final long serialVersionUID = -7295077661315897883L;

    /**
     * Create a new <code>DateFormatWrapper</code> to wrap the given <code>DateFormat</code> date format.
     * 
     * @param dateFormat Wrapped <code>DateFormat</code> object
     */
    public DateFormatWrapper(DateFormat dateFormat) {
        this.innerFormat = dateFormat;
    }

    private final DateFormat innerFormat;

    /**
     * Convert a <code>LocalDateTime</code> object to a <code>Date</code> object using the zone ID from the wrapped date format.
     * 
     * @param localDateTime <code>LocalDateTime</code> object
     * @return <code>Date</code> object
     */
    private Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = innerFormat.getTimeZone().toZoneId();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        
        return Date.from(instant);
    }
    
    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        if (obj instanceof LocalDateTime) {
            obj = convertLocalDateTimeToDate((LocalDateTime) obj);
        } else if (obj instanceof LocalDate) {
            obj = convertLocalDateTimeToDate(((LocalDate) obj).atStartOfDay());
        } else if (obj instanceof LocalTime) {
            obj = convertLocalDateTimeToDate(((LocalTime) obj).atDate(LocalDate.now()));
        }
        
        return innerFormat.format(obj, toAppendTo, pos);
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        return innerFormat.parseObject(source, pos);
    }
    
}
