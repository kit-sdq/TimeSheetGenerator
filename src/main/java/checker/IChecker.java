package checker;

import java.util.Collection;

import data.TimeSheet;

/**
 * A checker is able to check the validity of an {@link TimeSheet}.
 */
public interface IChecker {

    /**
     * Checks the validity of a {@link TimeSheet}.
     * @return If the {@link TimeSheet} is valid {@link CheckerReturn}.Valid is returned. Invalid otherwise.
     * @throws CheckerException if an error occurs while checking the {@link TimeSheet}.
     */
    public CheckerReturn check() throws CheckerException;
    
    /**
     * Returns a {@link Collection} of {@link CheckerError} elements that occurred while checking a {@link TimeSheet}.
     * @return A {@link Collection} of {@link CheckerError CheckerErrors}.
     */
    public Collection<CheckerError> getErrors();

}
