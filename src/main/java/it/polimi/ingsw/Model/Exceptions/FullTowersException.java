package it.polimi.ingsw.Model.Exceptions;

/**
 * Exception thrown when we try to add a tower to a player's dashboard, but he already has the maximum number of towers
 */
public class FullTowersException extends Exception
{
    public FullTowersException() { super(); }
}
