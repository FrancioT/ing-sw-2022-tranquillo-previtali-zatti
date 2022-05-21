package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Exceptions.EmptyException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Deck implements Serializable
{
    private List<StandardCard> handList;
    private StandardCard discardedCard;
    static final long serialVersionUID= 80220L;

    Deck()
    {
        handList=new ArrayList<>();
        discardedCard=null;

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

    StandardCard cardDiscarder(int pos) throws IndexOutOfBoundsException
    {
        if(pos>=handList.size() || pos<0) throw new IndexOutOfBoundsException();

        StandardCard s = handList.remove(pos);
        discardedCard=s;
        return s;
    }
    int getLastCardMNValue() throws EmptyException
    {
        if(discardedCard==null) throw new EmptyException();
        return discardedCard.getMnValue();
    }
    List<StandardCard> getHandCards() { return new ArrayList<>(handList); }

    public void deckPrinter()
    {
        String deckString = " ";

        for(StandardCard sC: handList)
        {
            deckString = deckString + sC.getRoundValue() + "--" + sC.getMnValue() + " ";
        }

        System.out.println("Your deck contains:" + deckString);

        if(this.discardedCard!=null)
            System.out.println("Your last card:" + this.discardedCard.getRoundValue() + "--" + this.discardedCard.getMnValue());

        System.out.println("\n");
    }
}
