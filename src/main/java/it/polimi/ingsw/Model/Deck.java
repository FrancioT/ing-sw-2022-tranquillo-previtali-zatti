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

    /**
     * Constructor of the decks; it creates the deck by adding the needed standard cards
     */
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

    /**
     * Method used to discard a chosen card
     * @param pos the index of the card relatively to its current position
     * @return the card discarded
     * @throws IndexOutOfBoundsException Exception thrown if the index requested is unacceptable
     */
    StandardCard cardDiscarder(int pos) throws IndexOutOfBoundsException
    {
        if(pos>=handList.size() || pos<0) throw new IndexOutOfBoundsException();

        StandardCard s = handList.remove(pos);
        discardedCard=s;
        return s;
    }

    /**
     * Method used to obtain the MN value of the last card played by a player
     * @return the MN value
     * @throws EmptyException Exception thrown when there isn't a discarded card
     */
    int getLastCardMNValue() throws EmptyException
    {
        if(discardedCard==null) throw new EmptyException();
        return discardedCard.getMnValue();
    }

    /**
     * @return the last card played
     * @throws EmptyException Exception thrown when there isn't a discarded card
     */
    StandardCard getDiscardedCard() throws EmptyException
    {
        if(discardedCard==null) throw new EmptyException();
        return discardedCard;
    }

    /**
     * @return the list of the cards available
     */
    List<StandardCard> getHandCards() { return new ArrayList<>(handList); }

    /**
     * Method to print the deck
     */
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
