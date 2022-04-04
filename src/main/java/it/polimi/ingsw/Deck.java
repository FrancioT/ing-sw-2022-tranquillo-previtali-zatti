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
        discardedStack=new Stack<>();

        handList.add(new StandardCard(1,1));
        handList.add(new StandardCard(2,1));
        handList.add(new StandardCard(3,2));
        handList.add(new StandardCard(4,2));
        handList.add(new StandardCard(5,3));
        handList.add(new StandardCard(6,3));
        handList.add(new StandardCard(7,4));
        handList.add(new StandardCard(8,4));
        handList.add(new StandardCard(9,5));
        handList.add(new StandardCard(10,5));
    }

    public StandardCard cardDiscarder(int pos) throws IndexOutOfBoundsException
    {
        if(pos>=handList.size() || pos<0) throw new IndexOutOfBoundsException();

        StandardCard s = handList.remove(pos);
        discardedStack.push(s);
        return s;
    }
    public int getLastCardMNValue() { return discardedStack.peek().getMnValue(); }
}
