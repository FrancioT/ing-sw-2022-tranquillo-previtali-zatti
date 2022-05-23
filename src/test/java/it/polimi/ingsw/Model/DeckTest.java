package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Exceptions.*;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    public void testCardDiscarderAndReturn() throws EmptyException
    {
        Deck deck = new Deck();

        try{
            deck.cardDiscarder(11);
            fail();
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

        rv = deck.cardDiscarder(2).getRoundValue();
        mnv = deck.getLastCardMNValue();
        assertEquals(rv, 5);
        assertEquals(mnv, 3);

        assertEquals(deck.getHandCards().size(), 7);
    }

    @Test
    public void printerTest() throws RunOutOfStudentsException, FullTowersException, RunOutOfTowersException, LinkFailedException {
        Deck deck = new Deck();
        deck.cardDiscarder(1);
        System.setOut(new PrintStream(new OutputStream()
        {
            public void close() {}
            public void flush() {}
            public void write(byte[] b) {}
            public void write(byte[] b, int off, int len) {}
            public void write(int b) {}
        }
        ));
        deck.deckPrinter();
    }
}