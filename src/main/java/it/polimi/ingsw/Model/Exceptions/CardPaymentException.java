package it.polimi.ingsw.Model.Exceptions;

/**
 * The player does not have the necessary amount of coins to pay the card
 */
public class CardPaymentException extends Exception
{
    public CardPaymentException() { super(); }
}
