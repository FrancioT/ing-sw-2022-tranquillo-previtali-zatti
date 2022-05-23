package it.polimi.ingsw.Model.Exceptions;

/**
 * There are no more cards in a player's deck or towers on a player's dashboard
 */
public class EmptyException extends Exception
{
    public EmptyException() { super(); }
}