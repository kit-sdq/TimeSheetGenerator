package data;

import i18n.ResourceHandler;

/**
 * Represents an area, environment respectively, an {@link Employee employee} works in.
 */
public enum WorkingArea {
    /**
     * GF represents "Grossforschungsbereich"
     */
    GF("gf"),
    /**
     * UB represents "Universitaetsbereich"
     */
    UB("ub");
    
    private String stringValue;
    
    private WorkingArea(String stringValue) {
        this.stringValue = stringValue;
    }
    
    /**
     * Returns the string value of the working area
     * @return String value
     */
    public String getStringValue() {
        return stringValue;
    }
    
    /**
     * Parses a given {@link String} to a {@link WorkingArea} element.
     * @param s - the string to be parsed.
     * @return A {@link WorkingArea} element parsed from a {@link String}.
     */
    public static WorkingArea parse(String s) {
        if (s.equalsIgnoreCase(GF.getStringValue())) {
            return GF;
        } else if (s.equalsIgnoreCase(UB.getStringValue())) {
            return UB;
        }
        throw new IllegalArgumentException(ResourceHandler.getMessage("error.workingarea.invalidParseInput"));
    }
}
