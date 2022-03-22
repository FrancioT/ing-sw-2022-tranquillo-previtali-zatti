package it.polimi.ingsw;

import java.util.Stack;

public class Deck {
    private StandardCard[] handList;
    private Stack<StandardCard> discardedStack;

    public Deck()
    {

    }

    public StandardCard cardDiscarder(int pos)
    {
        discardedStack.add(handList[pos]);
    }
}
