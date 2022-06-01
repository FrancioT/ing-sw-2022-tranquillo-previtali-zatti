package it.polimi.ingsw.Model.Exceptions;

/**
 * Exception thrown when there are no more students available in the bag
 */
public class RunOutOfStudentsException extends Exception
{
    public RunOutOfStudentsException() { super(); }
}
