package checker;

import java.util.Collection;

public interface IChecker {

    public CheckerReturn check() throws CheckerException;
    
    public Collection<CheckerError> getErrors();

}
