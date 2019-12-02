package data;

/**
 * Represents an area, environment respectively, an {@link Employee employee} works in.
 */
public enum WorkingArea {
    /**
     * GF represents "Grossforschungsbereich"
     */
    GF,
    /**
     * UB represents "Universitaetsbereich"
     */
    UB;
    
    /**
     * Parses a given {@link String} to a {@link WorkingArea} element.
     * @param s - the string to be parsed.
     * @return A {@link WorkingArea} element parsed from a {@link String}.
     */
    public static WorkingArea parse(String s) {
        if (s.equalsIgnoreCase("gf")) {
            return GF;
        } else if (s.equalsIgnoreCase("ub")) {
            return UB;
        }
        throw new IllegalArgumentException("Invalid string: Cannot be parsed to WorkingArea.");
    }
}
