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
    
    public static WorkingArea parse(String s) {
        if (s.equalsIgnoreCase("gf")) {
            return GF;
        } else if (s.equalsIgnoreCase("ub")) {
            return UB;
        }
        throw new IllegalArgumentException("Invalid string: Cannot be parsed to WorkingArea.");
    }
}
