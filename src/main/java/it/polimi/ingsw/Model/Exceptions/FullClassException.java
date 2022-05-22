package it.polimi.ingsw.Model.Exceptions;

/**
 * The classroom where we tried to add a student already contains the maximum number of them
 */
public class FullClassException extends Exception
{
    public FullClassException() { super(); }
}
