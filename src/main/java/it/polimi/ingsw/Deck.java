package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Deck {
    private List<StandardCard> handList;
    private Stack<StandardCard> discardedStack;

    public Deck()
    {
        handList=new ArrayList<>();

        handList.add(new StandardCard(1,1));
        handList.add(new StandardCard(1,1));
        handList.add(new StandardCard(1,1));
        handList.add(new StandardCard(1,1));
        handList.add(new StandardCard(1,1));
        handList.add(new StandardCard(1,1));
        handList.add(new StandardCard(1,1));
        handList.add(new StandardCard(1,1));
        handList.add(new StandardCard(1,1));
        handList.add(new StandardCard(1,1));
    }

    public StandardCard cardDiscarder(int pos)
    {
        StandardCard s = handList.remove(pos);
        discardedStack.add(s);
        return s;
    }
}
