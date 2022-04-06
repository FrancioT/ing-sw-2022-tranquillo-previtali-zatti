package it.polimi.ingsw;

import it.polimi.ingsw.Model.Deck;
import it.polimi.ingsw.Model.Exceptions.EmptyException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    public void testCardDiscarderAndReturn() throws EmptyException
    {
        Deck deck = new Deck();

        try{
            deck.cardDiscarder(11);
            assertTrue(false);
        }
        catch (IndexOutOfBoundsException e){};

        int rv = 0;
        int mnv = 0;
        rv = deck.cardDiscarder(2).getRoundValue();
        mnv = deck.getLastCardMNValue();
        assertEquals(rv, 3);
        assertEquals(mnv, 2);
        mnv = deck.cardDiscarder(0).getMnValue();
        assertEquals(mnv, 1);
    }

}